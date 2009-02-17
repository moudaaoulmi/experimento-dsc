package healthwatcher.data.mem;

import healthwatcher.data.IEmployeeRepository;
import healthwatcher.model.employee.Employee;
import lib.exceptions.ObjectNotFoundException;

public class EmployeeRepositoryArray implements IEmployeeRepository {

	private static final long serialVersionUID = 1L;

	private Employee[] vector;

	private int index;

	private int iteratorIndex;

	public EmployeeRepositoryArray() {
		vector = new Employee[5000];
		index = 0;
	}

	public void insert(Employee employee) {
		synchronized (this) {
			if (employee == null) {
				throw new IllegalArgumentException();
			}
			this.vector[index] = employee;
			index++;
		}
	}

	public void update(Employee employee) throws ObjectNotFoundException {
		synchronized (this) {
			int i = getIndex(employee.getLogin());
			if (i == index) {
				throw new ObjectNotFoundException("Employee not found");
			} else {
				vector[i] = employee;
			}
		}
	}

	public boolean exists(String login) {
		synchronized (this) {
			int i = getIndex(login);
			return (i != index);
		}
	}

	public Employee search(String login) throws ObjectNotFoundException {
		synchronized (this) {
			Employee response = null;
			int i = getIndex(login);
			if (i == index) {
				throw new ObjectNotFoundException("Employee not found");
			} else {
				response = vector[i];
			}
			return response;
		}
	}

	public void remove(String login) throws ObjectNotFoundException {
		synchronized (this) {
			int i = getIndex(login);
			if (i == index) {
				throw new ObjectNotFoundException("Employee not found");
			} else {
				vector[i] = vector[index - 1];
				index = index - 1;
			}
		}
	}

	private int getIndex(String login) {
		synchronized (this) {
			String temp;
			boolean flag = false;
			int i = 0;
			while ((!flag) && (i < index)) {
				temp = vector[i].getLogin();
				if (temp.equals(login)) {
					flag = true;
				} else {
					i = i + 1;
				}
			}
			return i;
		}
	}

	public void reset() {
		synchronized (this) {
			this.iteratorIndex = 0;
		}
	}

	public Object next() {
		synchronized (this) {
			if (iteratorIndex >= index) {
				return null;
			} else {
				return vector[iteratorIndex++];
			}
		}
	}

	public boolean hasNext() {
		synchronized (this) {
			return (iteratorIndex < index);
		}
	}
}