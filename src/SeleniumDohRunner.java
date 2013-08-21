import ml.options.Options;
import ml.options.Options.Multiplicity;
import ml.options.Options.Separator;

public class SeleniumDohRunner {
	
	Context runnerContext = null;
	
	public static void main(String args[]) {
		
		System.out.println("Start");

	    Options opt = new Options(args, 4);
	    
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
	    
	    runnerContext.setHostURL(opt.getSet().getData().get(0));
	    runnerContext.setHostPort(opt.getSet().getData().get(1));
	    runnerContext.setRunnerPath(opt.getSet().getData().get(2));
	    runnerContext.setTestModule(opt.getSet().getData().get(3));
	    
	    System.out.println(runnerContext);
	    
	    System.out.println("End");

	  }
}
