package lib.patterns.observer;

import java.rmi.RemoteException;

import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.ObjectNotValidException;
import lib.exceptions.RepositoryException;
import lib.exceptions.TransactionException;



public interface Observer {
	
	public void notify(Subject subject) throws RemoteException, TransactionException, ObjectNotFoundException, RepositoryException,ObjectNotValidException;

}
