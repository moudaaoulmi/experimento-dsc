package healthwatcher.data.mem;

import healthwatcher.data.IHealthUnitRepository;
import healthwatcher.model.healthguide.HealthUnit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lib.exceptions.ObjectAlreadyInsertedException;
import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.RepositoryException;
import lib.util.ConcreteIterator;
import lib.util.IteratorDsk;

public class HealthUnitRepositoryArray implements IHealthUnitRepository {

	private HealthUnit[] vetor;

	private int indice;

	private int ponteiro; //para navegacao

	public HealthUnitRepositoryArray() {
		vetor = new HealthUnit[100];
		indice = 0;
	}

	public void update(HealthUnit unit) throws RepositoryException, ObjectNotFoundException {
		int i = getIndex(unit.getCode());
		if (i == indice) {
			throw new ObjectNotFoundException("Health unit not found");
		} else {
			vetor[i] = unit;
		}
	}

	public boolean exists(int code) throws RepositoryException {
		boolean flag = false;

		for (int i = 0; i < indice; i++) {
			if (this.vetor[i].getCode() == code) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	public boolean hasNext() {
		return ponteiro < indice;
	}

	public IteratorDsk getHealthUnitList() throws RepositoryException, ObjectNotFoundException {
		if (indice == 0)
			throw new ObjectNotFoundException("There isn't registered Health units");
		return new ConcreteIterator(Arrays.asList(vetor));
	}

	public IteratorDsk getPartialHealthUnitList() throws RepositoryException,
			ObjectNotFoundException {
		return new ConcreteIterator(Arrays.asList(vetor));
	}

	public IteratorDsk getHealthUnitListBySpeciality(int code) throws RepositoryException,
			ObjectNotFoundException {
		int aux = 0;
		List response = new ArrayList();

		while (aux < indice) {

			if (vetor[aux].hasSpeciality(code)) {
				response.add(vetor[aux]);
			}
			aux++;
		}

		if (! response.isEmpty()) {
			return new ConcreteIterator(response);
		} else {
			throw new ObjectNotFoundException(
					"There isn't registered health units for the specialty");
		}
	}

	public void reset() {
		this.ponteiro = 0;
	}

	public void insert(HealthUnit unit) throws RepositoryException, ObjectAlreadyInsertedException {
		if (unit == null) {
			throw new IllegalArgumentException();
		}
		this.vetor[indice] = unit;
		indice++;
	}

	public HealthUnit search(int code) throws RepositoryException, ObjectNotFoundException {
		HealthUnit response = null;
		int i = getIndex(code);
		if (i == indice) {
			throw new ObjectNotFoundException("Health unit not found");
		} else {
			response = vetor[i];
		}
		return response;
	}

	public Object next() {
		if (ponteiro >= indice) {
			return null;
		} else {
			return vetor[ponteiro++];
		}
	}

	public void remove(int code) throws RepositoryException, ObjectNotFoundException {
		int i = getIndex(code);
		if (i == indice) {
			throw new ObjectNotFoundException("Health unit not found");
		} else {
			vetor[i] = vetor[indice - 1];
			indice = indice - 1;
		}
	}

	private int getIndex(int code) {
		int temp;
		boolean flag = false;
		int i = 0;
		while ((!flag) && (i < indice)) {
			temp = vetor[i].getCode();
			if (temp == code) {
				flag = true;
			} else {
				i = i + 1;
			}
		}
		return i;
	}
}
