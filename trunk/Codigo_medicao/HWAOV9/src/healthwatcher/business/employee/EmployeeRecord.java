package healthwatcher.business.employee;

import healthwatcher.data.IEmployeeRepository;
import healthwatcher.model.employee.Employee;
import lib.exceptions.ExceptionMessages;
import lib.exceptions.ObjectAlreadyInsertedException;
import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.ObjectNotValidException;

public class EmployeeRecord {

	private IEmployeeRepository employeeRepository;

	public EmployeeRecord(IEmployeeRepository rep) {
		this.employeeRepository = rep;
	}

	public Employee search(String login) throws ObjectNotFoundException {
		return employeeRepository.search(login);
	}

	public void insert(Employee employee) throws ObjectNotValidException,
			ObjectAlreadyInsertedException, ObjectNotValidException {
		if (employeeRepository.exists(employee.getLogin())) {
			throw new ObjectAlreadyInsertedException(ExceptionMessages.EXC_JA_EXISTE);
		} else {
			employeeRepository.insert(employee);
		}
	}

	public void update(Employee employee) throws ObjectNotValidException, ObjectNotFoundException,
			ObjectNotValidException {
		employeeRepository.update(employee);
	}
}