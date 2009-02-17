package healthwatcher.data.mem;

import java.util.Arrays;

import healthwatcher.data.ISymptomRepository;
import healthwatcher.model.complaint.Symptom;
import lib.exceptions.ObjectAlreadyInsertedException;
import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.RepositoryException;
import lib.util.ConcreteIterator;
import lib.util.IteratorDsk;

public class SymptomRepositoryArray implements ISymptomRepository {

	private Symptom[] vetor;

	private int indice;

	private int ponteiro;

	public SymptomRepositoryArray() {
		vetor = new Symptom[100];
		indice = 0;
	}

	public void update(Symptom s) throws RepositoryException, ObjectNotFoundException {
		int i = getIndex(s.getCode());
		if (i == indice) {
			throw new ObjectNotFoundException("Symptom not found");
		} else {
			vetor[i] = s;
		}
	}

	public boolean exists(int codigo) throws RepositoryException {
		boolean flag = false;

		for (int i = 0; i < indice; i++) {
			if (this.vetor[i].getCode() == codigo) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	public boolean hasNext() {
		return ponteiro < indice;
	}

	public void reset() {
		this.ponteiro = 0;
	}

	public void insert(Symptom symptom) throws RepositoryException, ObjectAlreadyInsertedException {
		if (symptom == null) {
			throw new IllegalArgumentException();
		}
		this.vetor[indice] = symptom;
		indice++;
	}

	public Symptom search(int code) throws RepositoryException, ObjectNotFoundException {
		Symptom response = null;
		int i = getIndex(code);
		if (i == indice) {
			throw new ObjectNotFoundException("Symptom not found");
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
			throw new ObjectNotFoundException("Symptom not found");
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
	
	public IteratorDsk getSymptomList() throws RepositoryException,	ObjectNotFoundException {
		return new ConcreteIterator(Arrays.asList(vetor));
	}
}
