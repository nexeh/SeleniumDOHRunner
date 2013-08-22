import java.net.MalformedURLException;
import java.net.URL;

import ml.options.Options;
import ml.options.Options.Multiplicity;
import ml.options.Options.Separator;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumDohRunner {

	Context runnerContext = null;
	WebDriver driver = null;

	public static void main(String args[]) throws MalformedURLException {

		System.out.println("Start");

		Options opt = new Options(args, 5);

		opt.getSet().addOption("a", Multiplicity.ZERO_OR_ONE);
		opt.getSet().addOption("b", Separator.EQUALS, Multiplicity.ZERO_OR_ONE);
		opt.getSet().addOption("c", Separator.EQUALS, Multiplicity.ZERO_OR_ONE);
		opt.getSet().addOption("h", Separator.EQUALS, Multiplicity.ZERO_OR_ONE);
		opt.getSet().addOption("p", Separator.EQUALS, Multiplicity.ZERO_OR_ONE);
		opt.getSet().addOption("x", Multiplicity.ZERO_OR_ONE);

		if (!opt.check()) {
			System.out.println("Exit");
			System.exit(1);
		}

		Context runnerContext = new Context();

		if (opt.getSet().isSet("a")) {
			runnerContext.setAsync(true);
		}

		if (opt.getSet().isSet("b")) {
			String value = opt.getSet().getOption("b").getResultValue(0);
			runnerContext.setBrowser(value);
		}

		if (opt.getSet().isSet("c")) {
			String value = opt.getSet().getOption("c").getResultValue(0);
			runnerContext.setJsocverContent(value);
		}

		if (opt.getSet().isSet("h")) {
			String value = opt.getSet().getOption("h").getResultValue(0);
			runnerContext.setHubUrl(value);
		}

		if (opt.getSet().isSet("p")) {
			String value = opt.getSet().getOption("p").getResultValue(0);
			runnerContext.setPluginString(value);
		}

		if (opt.getSet().isSet("x")) {
			runnerContext.setCustomFirefoxProfile(true);
		}

		runnerContext.setHost(opt.getSet().getData().get(0));
		runnerContext.setHostPort(opt.getSet().getData().get(1));
		runnerContext.setRunnerPath(opt.getSet().getData().get(2));
		runnerContext.setTestModule(opt.getSet().getData().get(3));
		runnerContext.setPath(opt.getSet().getData().get(4));

		System.out.println(runnerContext);

		SeleniumDohRunner runner = new SeleniumDohRunner();
		runner.run(runnerContext);

		System.out.println("End");

	}

	public void run(Context runContext) throws MalformedURLException {
		this.runnerContext = runContext;

		// build doh test harness url
		String dohUrl = buildDohUrl();

		// if selenium hub is being used
		if (this.runnerContext.isUsingSeleniumHub()) {
			// request a browser from the hub
			this.driver = getRemoteDriver();
		}

		// else
		else {
			// Create a driver directly
		}

		// if jscover is being used
		if (this.runnerContext.isUsingJscover()) {

			// navigate to the jscoverage page. by starting our tests from the
			// jscoverage page we will be able to generate the coverage report
			driver.get(buildJscoverUrl());

			// Once the page is loaded, we need to find and clear the text in
			// the location field
			WebElement location = driver.findElement(By.id("location"));
			location.clear();

			// Fill in the location field with the doh test url that we require
			location.sendKeys(dohUrl);

			// click the button to have jscoverage start running our test
			// harness
			WebElement newWindowButton = driver.findElement(By
					.cssSelector("#locationDiv button"));
			newWindowButton.click();

			// Move into the iframe that contains the doh test harness
			driver.switchTo().frame(driver.findElement(By.id("browserIframe")));
		}

		else {
			// else jscover isnt being used
			// navaigate to the doh test harness
		}

		// wait to see if dojo, tests, and all required files load successfully
		// and handle it if it doesn't
		// TODO this is still the original code and isn't reusable by everyone.
		// it assumes there is
		// a plugin/patch that will added a loaded class to the dom when
		// everything is loaded. Fix this
		try {
			// Wait till the modules are traversed and the tests start. this is
			// were things could go wrong and prevent the test harness from
			// running
			WebDriverWait loadWait = new WebDriverWait(driver, 30);
			loadWait.until(ExpectedConditions.presenceOfElementLocated(By
					.id("loaded")));
		} catch (TimeoutException e) {

			// Attempt to get the browser log
			WebElement consoleLogNode = driver.findElement(By.id("consoleLog"));

			System.out
					.println("*****************************************************************");
			System.out.println("");
			System.out.println("ACTION REQUIRED");
			System.out.println("");
			System.out
					.println("Files did NOT succesfully load. This could be ");
			System.out
					.println("because there is a downward reference, a bad requires in a file, ");
			System.out
					.println("or anything else that could have caused a 404.");
			System.out.println("");
			System.out.println("CONSOLE LOG");
			System.out.println("");
			System.out.println(consoleLogNode.getText());
			System.out
					.println("*****************************************************************");
			closeBrowser();

			// exit with a code greater than 0 to break the build!
			System.exit(1);
		}

		// Wait till all the tests run
		WebDriverWait wait = new WebDriverWait(driver, 2800);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By
				.cssSelector(".inProgress td span")));

		// once the tests are complete we can rip out the results and print them
		// to the console so they are visible in CI Console
		System.out.println(this.getResultSummary(driver));
		System.out.println(this.getLog(driver));

		if (this.runnerContext.isUsingJscover()) {

			// Switch back to the window from the iframe containing the doh test
			// harness
			driver.switchTo().defaultContent();

			WebElement storeTab = driver.findElement(By.id("storeTab"));
			storeTab.click();

			wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By
					.id("storeButton")));

			WebElement storeBtn = driver.findElement(By.id("storeButton"));
			storeBtn.click();

			driver.switchTo().frame(driver.findElement(By.id("browserIframe")));
		}

		// break the build if there are failures
		this.handleFailures(driver);
	}

	private WebDriver getRemoteDriver() throws MalformedURLException {

		URL hubUrl = new URL(this.runnerContext.getHubUrl());

		DesiredCapabilities capabilities = getDesiredCapabilities();

		return new RemoteWebDriver(hubUrl, capabilities);
	}

	private DesiredCapabilities getDesiredCapabilities() {
		DesiredCapabilities capabilities = null;

		if (this.runnerContext.getBrowser().equalsIgnoreCase("firefox")) {
			capabilities = DesiredCapabilities.firefox();

			FirefoxProfile profile = new FirefoxProfile();

			profile.setPreference("dom.max_script_run_time", 0);
			capabilities.setCapability(FirefoxDriver.PROFILE, profile);

			return capabilities;
		}

		else if (this.runnerContext.getBrowser().equalsIgnoreCase("ie")) {
			return capabilities = DesiredCapabilities.internetExplorer();
		}

		else if (this.runnerContext.getBrowser().equalsIgnoreCase("chrome")) {
			return capabilities = DesiredCapabilities.chrome();
		}

		// TODO what should the default be?
		else {
			return capabilities = DesiredCapabilities.firefox();
		}
	}

	private String buildDohUrl() {
		StringBuilder dohUrl = new StringBuilder();

		dohUrl.append("http://");
		dohUrl.append(this.runnerContext.getHost());
		dohUrl.append(":");
		dohUrl.append(this.runnerContext.getHostPort());
		dohUrl.append("/");
		dohUrl.append(this.runnerContext.getRunnerPath());
		dohUrl.append("?");
		dohUrl.append("testModule=" + this.runnerContext.getTestModule() + "&");
		dohUrl.append("paths=" + this.runnerContext.getPath());

		if (this.runnerContext.isAsync()) {
			dohUrl.append("&async=1");
		}

		if (this.runnerContext.isUsingPlugins()) {
			dohUrl.append("&dohPlugins=" + this.runnerContext.getPluginString());
		}

		System.out.println("URL: " + dohUrl.toString());

		return dohUrl.toString();
	}

	private String buildJscoverUrl() {
		StringBuilder jscoverurl = new StringBuilder();

		jscoverurl.append("http://");
		jscoverurl.append(this.runnerContext.getHost());
		jscoverurl.append(":");
		jscoverurl.append(this.runnerContext.getHostPort());
		jscoverurl.append("/");
		jscoverurl.append(this.runnerContext.getJsocverContent());

		return jscoverurl.toString();
	}

	private void closeBrowser() {
		driver.quit();
	}

	public String getResultSummary(WebDriver driver) {
		WebElement results = driver.findElement(By.cssSelector(".inProgress"));
		return results.getText();
	}

	public String getLog(WebDriver driver) {
		WebElement results = driver.findElement(By.xpath("//*[@id='logBody']"));
		return results.getText();
	}

	public void handleFailures(WebDriver driver) {
		WebElement errors = driver
				.findElement(By
						.xpath("/html/body/table/tbody/tr[4]/td/div/table/tfoot/tr/td[2]/span"));
		WebElement failures = driver
				.findElement(By
						.xpath("/html/body/table/tbody/tr[4]/td/div/table/tfoot/tr/td[2]/span[2]"));

		System.out.println("errors: " + errors.getText());
		System.out.println("failures: " + failures.getText());

		int errorCount = Integer.parseInt(errors.getText());
		int failureCount = Integer.parseInt(failures.getText());

		// close the browser
		this.closeBrowser();

		System.exit(errorCount + failureCount);

	}

}
