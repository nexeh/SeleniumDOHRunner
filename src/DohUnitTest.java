import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class DohUnitTest {

	String seleniumURL;
	String module;
	String path;
	String port;
	boolean isAnalytics = false;
	
    public static void main(String[] args) throws Exception {
    	
    	if(args.length != 4) {
    		printHelp();
    		System.exit(1);
    	}
    	
        DohUnitTest unit = new DohUnitTest();
        unit.setup(args);
        unit.test();
     }
    
    public void setup(String[] args) {
    	this.seleniumURL = args[0];
    	this.module = args[1];
    	this.path = args[2];
    	this.port = args[3];
    	
    	// This is a hack for now to just get analytics tests running. I will revisit this in the future
    	if(this.module.contains("runner.html")) {
    		this.isAnalytics = true;
    	}
    }
    
    public void test() throws InterruptedException {
    	
    	// Open Firefox
    	ProfilesIni allProfiles = new ProfilesIni();
    	FirefoxProfile profile = allProfiles.getProfile("default");
    	profile.setPreference("network.http.use-cache", false);
    	WebDriver driver = new FirefoxDriver(profile);
        //WebDriver driver = new FirefoxDriver();
        
        // navigate to the jscoverage page. by starting our tests from the 
        // jscoverage page we will be able to generate the coverage report
        driver.get("localhost:"+this.port+"/jscoverage.html");
        
        // Once the page is loaded, we need to find and clear the text in the lcoation field
        WebElement location = driver.findElement(By.id("location"));
        location.clear();
        
        // Fill in the location field with the doh test url that we require
        if(this.isAnalytics) {
        	location.sendKeys(seleniumURL + module);
        } else {
        	location.sendKeys(seleniumURL + "/PmDojo/util/doh/runner.html?testModule=" + module + "&paths=test," + path + "&async=1&dohPlugins=lmig/patches/doh/DohRunner;lmig/patches/doh/DohRunnerCI");
        }
        
        System.out.println(seleniumURL + "/PmDojo/util/doh/runner.html?testModule=" + module + "&paths=test," + path + "&async=1&dohPlugins=lmig/patches/doh/DohRunner;lmig/patches/doh/DohRunnerCI");
        
        // click the button to have jscoverage start running our test harness
        WebElement newWindowButton = driver.findElement(By.cssSelector("#locationDiv button"));
        newWindowButton.click();
        
        try {
	        // Wait till the modules are traversed and the tests start. this is were things could go wrong and prevent the test harness from running
	        WebDriverWait loadWait = new WebDriverWait(driver, 30);
	        driver.switchTo().frame(driver.findElement(By.id("browserIframe")));
	        loadWait.until(ExpectedConditions.presenceOfElementLocated(By.id("loaded")));
        } catch(TimeoutException e) {
        	
        	// Attempt to get the browser log
        	WebElement consoleLogNode = driver.findElement(By.id("consoleLog"));
        	
        	System.out.println("*****************************************************************");
        	System.out.println("");
        	System.out.println("ACTION REQUIRED");
        	System.out.println("");
        	System.out.println("Files did NOT succesfully load. This could be " );
        	System.out.println("because there is a downward reference, a bad requires in a file, ");
			System.out.println("or anything else that could have caused a 404.");
			System.out.println("");
			System.out.println("CONSOLE LOG");
			System.out.println("");
			System.out.println(consoleLogNode.getText());
        	System.out.println("*****************************************************************");
        	closeBrowser(driver);
        	
        	// exit with a code greater than 0 to break the build!
        	System.exit(1);
        }
        
        // Wait till all the tests run
        WebDriverWait wait = new WebDriverWait(driver, 2800);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".inProgress td span")));
        
        // once the tests are complete we can rip out the results and print them to the console so they are visible in CI Console
        System.out.println(this.getResultSummary(driver));
        System.out.println(this.getLog(driver));
        
        driver.switchTo().defaultContent();
        
        WebElement storeTab = driver.findElement(By.id("storeTab"));
        storeTab.click();
        
        wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("storeButton")));
        
        WebElement storeBtn = driver.findElement(By.id("storeButton"));
        storeBtn.click();
        
        // break the build if there are failures
        driver.switchTo().frame(driver.findElement(By.id("browserIframe")));
        this.handleFailures(driver);
    }
    
//    public void test() throws InterruptedException {
//    	// The Firefox driver supports javascript 
//        WebDriver driver = new FirefoxDriver();
//        
//        // Open Dojo's DOH Runner HTML and pass it the module to run
//        // This 'if' is a hack for now to just get analytics tests running. I will revisit this in the future
//        if(this.isAnalytics) {
//        	driver.get(seleniumURL + module);
//        } else {
//        	driver.get(seleniumURL + "/PmDojo/util/doh/runner.html?testModule=" + module + "&paths=test," + path + "&async=1&dohPlugins=lmig/patches/doh/DohRunner");
//        }
//        
//        // Wait till all the tests run
//        WebDriverWait wait = new WebDriverWait(driver, 300);
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/table/tbody/tr[4]/td/div/table/tfoot/tr")));
//        
//        // once the tests are complete we can rip out the results and print them to the console so they are visible in CI Console
//        System.out.println(this.getResultSummary(driver));
//        System.out.println(this.getLog(driver));
//        
//        // close the browser
//        this.closeBrowser(driver);
//        
//        // break the build if there are failures
//        this.handleFailures(driver);
//    }
    
    private static void printHelp() {
    	System.out.println("Three params are expected to be given:");
    	System.out.println("");
    	System.out.println("\tURL: The url you want selenium tests to run against. Exmaple: 'http://vddk03p-1679714:8080'");
    	System.out.println("\tModule: The module string to give to doh. Exmaple: 'test.unit.lmig.pm.internet.esales.desktop.auto.model.module'");
    	System.out.println("\tPath: The path string to give to doh. Exmaple: '/PmESalesDesktopAutoClient/test'");
    	System.out.println("");
    	System.out.println("These are used to build out the following string:");
    	System.out.println("http://vddk03p-1679714:8080/PmDojo/util/doh/runner.html?testModule=test.unit.lmig.pm.internet.esales.desktop.auto.view.module&paths=test,/PmESalesDesktopAutoClient/test");
    }
    
    public void wait(int amountOfTime) {
    	try {
			Thread.sleep(amountOfTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    public void closeBrowser(WebDriver driver) {
    	driver.close();
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
    	WebElement errors = driver.findElement(By.xpath("/html/body/table/tbody/tr[4]/td/div/table/tfoot/tr/td[2]/span"));
    	WebElement failures = driver.findElement(By.xpath("/html/body/table/tbody/tr[4]/td/div/table/tfoot/tr/td[2]/span[2]"));
    	
    	System.out.println("errors: " + errors.getText());
    	System.out.println("failures: " + failures.getText());
    	
    	int errorCount = Integer.parseInt(errors.getText());
    	int failureCount = Integer.parseInt(failures.getText());
    	
        // close the browser
        this.closeBrowser(driver);
    	
    	System.exit(errorCount + failureCount);
    	
    }
    
    private String parseLogForErrors(String log) {
    	return log;
    }
}
