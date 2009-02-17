package healthwatcher.data;

import healthwatcher.model.complaint.Complaint;
import lib.exceptions.ObjectAlreadyInsertedException;
import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.ObjectNotValidException;
import lib.exceptions.RepositoryException;
import lib.util.IteratorDsk;

/**
 * Data collection interface to be implemented by concrete data
 * collections accessing any persistence mechanism.
 */
public interface IComplaintRepository {

	public int insert(Complaint complaint) throws ObjectNotValidException,
			ObjectAlreadyInsertedException, ObjectNotValidException, RepositoryException;

	public void update(Complaint complaint) throws ObjectNotValidException,
			ObjectNotFoundException, ObjectNotValidException, RepositoryException;

	public boolean exists(int code) throws RepositoryException;

	public void remove(int code) throws ObjectNotFoundException, RepositoryException;

	public Complaint search(int complaint) throws ObjectNotFoundException, RepositoryException;

	public IteratorDsk getComplaintList() throws ObjectNotFoundException, RepositoryException;
}