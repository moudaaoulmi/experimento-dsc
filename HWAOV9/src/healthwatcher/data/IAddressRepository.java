package healthwatcher.data;

import healthwatcher.model.address.Address;
import lib.exceptions.ObjectAlreadyInsertedException;
import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.ObjectNotValidException;
import lib.exceptions.RepositoryException;

/**
 * Data collection interface to be implemented by concrete data
 * collections accessing any persistence mechanism.
 */
public interface IAddressRepository {

	public int insert(Address address) throws ObjectNotValidException,
			ObjectAlreadyInsertedException, ObjectNotValidException, RepositoryException;

	public void update(Address address) throws ObjectNotValidException,
			ObjectNotFoundException, ObjectNotValidException, RepositoryException;

	public boolean exists(int code) throws RepositoryException;

	public void remove(int code) throws ObjectNotFoundException, RepositoryException;

	public Address search(int complaint) throws ObjectNotFoundException, RepositoryException;

}