package healthwatcher.aspects.distribution;

import healthwatcher.business.HealthWatcherFacade;

/**
 * An abstract server side distribution aspect. Concrete classes should use the
 * serverStart pointcut to start the distribution server.
 * 
 * This is the server part for distribution. The following tasks are done:
 * 
 * 1) makes the facade implement the remote interface
 * 2) makes model classes serializable (actually this is also needed in the client)
 * 3) creates a main method in the facade, to start the server
 * 4) publishes the facade as an rmi server when starting
 */
public abstract aspect HWServerDistribution {

	/**
	 * Declare the model classes serializable
	 */
	declare parents : healthwatcher.model..* implements java.io.Serializable;

	/**
	 * Creates main method to be able to start the server
	 */
	public static void HealthWatcherFacade.main(String[] args) {
		try {
			HealthWatcherFacade.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Publishes the server when it starts
	 */
	protected pointcut serverStart() : execution(static void HealthWatcherFacade.main(String[]));
	
	protected void facadeExceptionHandling(Throwable exception) {
		System.out.println("**************************************************");
		System.out.println("* Error during server initialization!            *");
		System.out.println("* The exception message is:                      *");
		System.out.println("      " + exception.getMessage());
		System.out.println("* You may have to restart the server or registry.*");
		System.out.println("**************************************************");
		exception.printStackTrace();
	}
}
