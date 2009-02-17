package healthwatcher.data;

import healthwatcher.model.complaint.Symptom;
import lib.exceptions.ObjectAlreadyInsertedException;
import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.ObjectNotValidException;
import lib.exceptions.RepositoryException;
import lib.util.IteratorDsk;

public interface ISymptomRepository {

	public void insert(Symptom s) throws ObjectNotValidException, ObjectAlreadyInsertedException,
			ObjectNotValidException, RepositoryException;

	public void update(Symptom s) throws ObjectNotValidException, ObjectNotFoundException,
			ObjectNotValidException, RepositoryException;

	public boolean exists(int code) throws RepositoryException;

	public void remove(int code) throws ObjectNotFoundException, RepositoryException;

	public Symptom search(int code) throws ObjectNotFoundException, RepositoryException;
	
	public IteratorDsk getSymptomList() throws ObjectNotFoundException, RepositoryException;
}