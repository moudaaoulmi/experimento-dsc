package healthwatcher.business.factories;

import java.io.IOException;
import java.rmi.RemoteException;

import healthwatcher.Constants;
import healthwatcher.business.HealthWatcherFacade;
import healthwatcher.business.RMIFacadeAdapter;
import healthwatcher.view.IFacade;
import healthwatcher.view.RMIServletAdapter;
import lib.exceptions.CommunicationException;
import lib.exceptions.PersistenceMechanismException;

/**
 * A factory for a rmi facade
 */
public class RMIFacadeFactory extends AbstractFacadeFactory {

	public IFacade createClientFacade() throws CommunicationException, RemoteException, PersistenceMechanismException, IOException {
		//TODO ROBERTA
		//REMOVE DRITRIB.
		//return new RMIServletAdapter("//" + Constants.SERVER_NAME + "/" + Constants.SYSTEM_NAME);
		return HealthWatcherFacade.getInstance();
	}
	

	public void createServerFacade() throws CommunicationException {
		try {
			RMIFacadeAdapter.getInstance();
		} catch (Exception e) {
			throw new CommunicationException(e.getMessage());
		}
	}
}
