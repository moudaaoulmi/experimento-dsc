package healthwatcher.aspects.distribution;

import healthwatcher.business.HealthWatcherFacade;

import java.rmi.server.UnicastRemoteObject;

/**
 * A concrete server side aspect which starts an RMI Server
 */
public aspect RMIServerDistribution extends HWServerDistribution {

	/**
	 * Forces the facade to implement a remote interface, needed for RMI
	 */
	declare parents: HealthWatcherFacade implements IRemoteFacade;

	/**
	 * Publishes the server when it starts
	 */
	void around() : serverStart() {
		try {
			System.out.println("Creating RMI server...");
			UnicastRemoteObject.exportObject(HealthWatcherFacade.getInstance());
			System.out.println("Object exported");
			System.out.println(healthwatcher.Constants.SYSTEM_NAME);
			java.rmi.Naming.rebind("//" + healthwatcher.Constants.SERVER_NAME + "/"
					+ healthwatcher.Constants.SYSTEM_NAME, HealthWatcherFacade.getInstance());
			System.out.println("Server created and ready.");
		} catch (java.rmi.RemoteException rmiEx) {
			facadeExceptionHandling(rmiEx);
		} catch (java.net.MalformedURLException rmiEx) {
			facadeExceptionHandling(rmiEx);
		}
	}

}
