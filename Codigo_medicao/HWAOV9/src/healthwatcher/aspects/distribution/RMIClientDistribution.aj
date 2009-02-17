package healthwatcher.aspects.distribution;

import java.rmi.Remote;
import java.rmi.RemoteException;

import lib.distribution.rmi.IteratorRMISourceAdapter;
import lib.distribution.rmi.IteratorRMITargetAdapter;
import lib.distribution.rmi.MethodExecutor;
import lib.util.LocalIterator;

/**
 * A concrete distribution aspect for HW clients, using RMI.
 * 
 */
public aspect RMIClientDistribution extends HWClientDistribution {

	// Makes soft remote exceptions raised in this aspect
	declare soft : RemoteException : within(HWClientDistribution+);
	
	/**
	 * Keeps the remote instance
	 */
	protected IRemoteFacade facade = null;
	
	/**
     * Redirects the facade methods call to the distributed instance methods using reflection
     */
	Object around() : facadeCalls() {
	    Object obj = MethodExecutor.invoke(
	    	getRemoteFacade(), thisJoinPoint.getSignature().getName(), thisJoinPoint.getArgs());
	    if (obj instanceof LocalIterator) {
			IteratorRMITargetAdapter iteratorTA = new IteratorRMITargetAdapter((LocalIterator) obj, 3);
			return new IteratorRMISourceAdapter(iteratorTA, (LocalIterator) obj, 3);
	    }
	    return obj;
    }
    
    /**
     * Retrieves the remote instance, creating if needed
     */
	private synchronized Remote getRemoteFacade() {        
		if (facade == null) {
			try {
	            System.out.println("About to lookup...");
	            facade = (IRemoteFacade) java.rmi.Naming.lookup("//" + healthwatcher.Constants.SERVER_NAME + "/" + healthwatcher.Constants.SYSTEM_NAME);
	            System.out.println("Remote DisqueSaude found");
	        } catch (java.rmi.RemoteException rmiEx) {
	            initExceptionHandling(rmiEx);
	        } catch (java.rmi.NotBoundException rmiEx) {
	            initExceptionHandling(rmiEx);
	        } catch (java.net.MalformedURLException rmiEx) {
	            initExceptionHandling(rmiEx);
	        }
		}
		return facade;
    }
}
