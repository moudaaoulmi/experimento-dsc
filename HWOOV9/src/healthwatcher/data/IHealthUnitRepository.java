package healthwatcher.data;

import lib.exceptions.ObjectAlreadyInsertedException;
import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.ObjectNotValidException;
import lib.exceptions.RepositoryException;
import lib.util.IteratorDsk;
import healthwatcher.model.healthguide.HealthUnit;

public interface IHealthUnitRepository {

	public void insert(HealthUnit us) throws ObjectNotValidException,
			ObjectAlreadyInsertedException, ObjectNotValidException, RepositoryException;

	public void update(HealthUnit us) throws ObjectNotValidException, ObjectNotFoundException,
			ObjectNotValidException, RepositoryException;

	public boolean exists(int num) throws RepositoryException;

	public void remove(int code) throws ObjectNotFoundException, RepositoryException;

	public HealthUnit search(int code) throws ObjectNotFoundException, RepositoryException;

	public IteratorDsk getHealthUnitList() throws ObjectNotFoundException, RepositoryException;

	public IteratorDsk getPartialHealthUnitList() throws ObjectNotFoundException,
			RepositoryException;

	public IteratorDsk getHealthUnitListBySpeciality(int codEspecialidade)
			throws ObjectNotFoundException, RepositoryException;

}
