package lib.distribution.rmi;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;

import lib.exceptions.CommunicationException;
import lib.util.IteratorDsk;
import lib.util.LocalIterator;


public class IteratorRMISourceAdapter implements IteratorDsk, Serializable {

	private IIteratorRMITargetAdapter targetAdapter;

	private Object[] cache;

	private int index; // where to read next in the cache

	public IteratorRMISourceAdapter(IIteratorRMITargetAdapter targetAdapter,
			LocalIterator iterator, int cacheSize) {

		this.targetAdapter = targetAdapter;
		cache = new Object[cacheSize];

		for (int i = 0; (i < cacheSize) && iterator.hasNext(); i++) {
			cache[i] = iterator.next();

		}

		index = 0;
	}

	public void print() {

		for (int i = 0; cache[i] != null; i++) {
			System.out.println("cache[" + i + "] = " + cache[i]);
		}
	}

	public void connect() throws CommunicationException {

		try {
			targetAdapter = (IIteratorRMITargetAdapter) Naming.lookup("//"
					+ healthwatcher.Constants.SERVER_NAME + "/" + healthwatcher.Constants.SYSTEM_NAME);
		} catch (Exception e) {
			throw new CommunicationException(e.getMessage());
		}
	}

	public Object next() throws CommunicationException {

		if (index < cache.length) {
			if (cache[index] != null) {
				return cache[index++];
			} else {
				return null;
			}
		} else if (cache.length == 0) {
			return null;
		} else { // a fault happened
			try {
				cache = targetAdapter.getNext();
				index = 0;

				return next();
			} catch (RemoteException e) {
				e.printStackTrace();

				throw new CommunicationException(e.getMessage());
			}
		}
	}

	public boolean hasNext() throws CommunicationException {

		if (index < cache.length) {
			if (cache[index] != null) {
				return true;
			} else {
				return false;
			}
		} else if (cache.length == 0) {
			return false;
		} else { // a fault happened
			try {
				cache = targetAdapter.getNext();
				index = 0;

				return hasNext();
			} catch (RemoteException e) {
				e.printStackTrace();

				throw new CommunicationException(e.getMessage());
			}
		}
	}

	public void close() throws CommunicationException {

		try {
			cache = null;

			targetAdapter.close();
		} catch (RemoteException e) {
			throw new CommunicationException(e.getMessage());
		}
	}

	public void remove() throws CommunicationException {
	}
}