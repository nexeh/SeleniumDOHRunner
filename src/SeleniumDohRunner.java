import ml.options.Options;
import ml.options.Options.Multiplicity;
import ml.options.Options.Separator;

public class SeleniumDohRunner {
	public static void main(String args[]) {
		
		System.out.println("Start");

	    Options opt = new Options(args, 4);
	    
	    //opt.getSet().addOption("a", Multiplicity.ZERO_OR_ONE);
	    //opt.getSet().addOption("c", Multiplicity.ZERO_OR_ONE);
	    //opt.getSet().addOption("h", Multiplicity.ZERO_OR_ONE);
	    opt.getSet().addOption("p", Multiplicity.ZERO_OR_ONE);

	    if (!opt.check()) {
	    	System.out.println("Exit");
	      System.exit(1);
	    }


	    if (opt.getSet().isSet("c")) {
	      String value = opt.getSet().getOption("c").getResultValue(0);  
	      System.out.println("c:  " + value);
	    }
	    
	    if (opt.getSet().isSet("h")) {
	      String value = opt.getSet().getOption("h").getResultValue(0);  
	      System.out.println("h:  " + value);
	    }
	    
	    if (opt.getSet().isSet("p")) {
	      String value = opt.getSet().getOption("p").getResultValue(0);  
	      System.out.println("p:  " + value);
	    }
	    
	    System.out.println("End");

	  }
}
