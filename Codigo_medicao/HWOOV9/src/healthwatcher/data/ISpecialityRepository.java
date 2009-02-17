package healthwatcher.data;

import lib.exceptions.ObjectAlreadyInsertedException;
import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.ObjectNotValidException;
import lib.exceptions.RepositoryException;
import lib.util.IteratorDsk;
import healthwatcher.model.healthguide.MedicalSpeciality;

public interface ISpecialityRepository {

	public void insert(MedicalSpeciality esp) throws ObjectNotValidException,
			ObjectAlreadyInsertedException, ObjectNotValidException, RepositoryException;

	public void update(MedicalSpeciality esp) throws ObjectNotValidException,
			ObjectNotFoundException, ObjectNotValidException, RepositoryException;

	public boolean exists(int num) throws RepositoryException;

	public void remove(int codigo) throws ObjectNotFoundException, RepositoryException;

	public IteratorDsk getSpecialityList() throws ObjectNotFoundException, RepositoryException;

	public MedicalSpeciality search(int codigo) throws ObjectNotFoundException, RepositoryException;

}
