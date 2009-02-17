package healthwatcher.business.factories;

import java.io.IOException;
import java.rmi.RemoteException;

import healthwatcher.view.IFacade;
import lib.exceptions.CommunicationException;
import lib.exceptions.PersistenceMechanismException;

/**
 * Defines methods for facade factories
 */
public abstract class AbstractFacadeFactory {
	
	public abstract IFacade createClientFacade() throws CommunicationException, RemoteException, PersistenceMechanismException, IOException;
	
	public abstract void createServerFacade() throws CommunicationException;
}
