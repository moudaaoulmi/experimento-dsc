package healthwatcher.data.mem;

import healthwatcher.data.IDiseaseRepository;
import healthwatcher.model.complaint.DiseaseType;

import java.util.Arrays;

import lib.exceptions.ObjectAlreadyInsertedException;
import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.ObjectNotValidException;
import lib.exceptions.RepositoryException;
import lib.util.ConcreteIterator;
import lib.util.IteratorDsk;

public class DiseaseTypeRepositoryArray implements IDiseaseRepository {

	private DiseaseType[] vetor;

	private int indice;

	private int ponteiro;

	public DiseaseTypeRepositoryArray() {
		vetor = new DiseaseType[100];
		indice = 0;
	}

	public void update(DiseaseType tp) throws RepositoryException, ObjectNotFoundException,
			ObjectNotValidException {
		int i = getIndex(tp.getCode());
		if (i == indice) {
			throw new ObjectNotFoundException("Disease not found");
		} else {
			vetor[i] = tp;
		}
	}

	public boolean exists(int codigo) throws RepositoryException {
		int i = getIndex(codigo);
		return (i != indice);
	}

	//metodo adicionado para uso dentro do iterator
	public boolean hasNext() {
		return ponteiro < indice;
	}

	public IteratorDsk getDiseaseTypeList() throws RepositoryException, ObjectNotFoundException {
		return new ConcreteIterator(Arrays.asList(vetor));
	}

	public void reset() {
		this.ponteiro = 0;
	}

	public void insert(DiseaseType tp) throws RepositoryException, ObjectAlreadyInsertedException {
		if (tp == null) {
			throw new IllegalArgumentException();
		}
		this.vetor[indice] = tp;
		indice++;
	}

	public DiseaseType search(int code) throws RepositoryException, ObjectNotFoundException {
		DiseaseType response = null;
		int i = getIndex(code);
		if (i == indice) {
			throw new ObjectNotFoundException("Disease not found");
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
			throw new ObjectNotFoundException("Disease not found");
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
