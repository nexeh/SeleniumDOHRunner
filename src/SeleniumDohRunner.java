import ml.options.Options;
import ml.options.Options.Multiplicity;
import ml.options.Options.Separator;

public class SeleniumDohRunner {

	Context runnerContext = null;

	public static void main(String args[]) {

		System.out.println("Start");

		Options opt = new Options(args, 5);

		opt.getSet().addOption("a", Multiplicity.ZERO_OR_ONE);
		opt.getSet().addOption("c", Separator.EQUALS, Multiplicity.ZERO_OR_ONE);
		opt.getSet().addOption("h", Separator.EQUALS, Multiplicity.ZERO_OR_ONE);
		opt.getSet().addOption("p", Separator.EQUALS, Multiplicity.ZERO_OR_ONE);

		if (!opt.check()) {
			System.out.println("Exit");
			System.exit(1);
		}

		Context runnerContext = new Context();

		if (opt.getSet().isSet("a")) {
			runnerContext.setAsync(true);
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

	public void run(Context runContext) {
		this.runnerContext = runContext;

		// build doh test harness url
		String dohUrl = buildDohUrl();

		// if selenium hub is being used
		if (this.runnerContext.isUsingSeleniumHub()) {
			// request a browser from the hub
		}

		// else
		else {
			// Create a driver directly
		}

		// if jscover is being used
		if (this.runnerContext.isUsingJscover()) {
			// navigate the browser to the js cover url
			// enter in the doh test url
		}

		else {
			// else jscover isnt being used
			// navaigate to the doh test harness
		}

		// wait to see if dojo, tests, and all required files load successfully
		// and handle it if it doesnt

		// wait for results then process
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

}
