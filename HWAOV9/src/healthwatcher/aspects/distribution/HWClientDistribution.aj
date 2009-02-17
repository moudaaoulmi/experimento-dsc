package healthwatcher.aspects.distribution;

import healthwatcher.view.IFacade;
import healthwatcher.view.command.CommandServlet;
import lib.exceptions.CommunicationException;
import lib.util.IteratorDsk;

/**
 * An abstract client distribution for the HW system. 
 * Concrete clients should use the facadeCalls() pointcut to intercept calls to the facade and 
 * distribute them over the network.
 */
public abstract aspect HWClientDistribution {
	
	// Makes soft the communication exceptions in the clients
	declare soft : CommunicationException : call(* IteratorDsk+.*(..));
    
    /**
     * Redirects the facade methods call to the distributed instance methods using reflection
     */
	protected pointcut facadeCalls() : call(* IFacade+.*(..)) && ! call(static * *.*(..)) && this(CommandServlet+);
	
	
    protected void initExceptionHandling(Throwable exception) {
    	String error =  "<p>****************************************************<br>" +
                 "Error during servlet initialization!<br>" +
                 "The exception message is:<br><dd>" + exception.getMessage() +
                 "<p>You may have to restart the servlet container.<br>" +
                 "*******************************************************";
        System.out.println(error);
    }
}
