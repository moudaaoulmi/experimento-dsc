package healthwatcher.data;

import lib.exceptions.ObjectAlreadyInsertedException;
import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.ObjectNotValidException;
import lib.exceptions.RepositoryException;
import healthwatcher.model.employee.Employee;

public interface IEmployeeRepository {

	public void insert(Employee employee) throws ObjectNotValidException,
			ObjectAlreadyInsertedException, ObjectNotValidException, RepositoryException;

	public void update(Employee employee) throws ObjectNotValidException, ObjectNotFoundException,
			ObjectNotValidException, RepositoryException;

	public boolean exists(String login) throws RepositoryException;

	public void remove(String login) throws ObjectNotFoundException, RepositoryException;

	public Employee search(String login) throws ObjectNotFoundException, RepositoryException;
}