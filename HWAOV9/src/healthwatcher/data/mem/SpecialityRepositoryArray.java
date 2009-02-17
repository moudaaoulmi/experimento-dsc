package healthwatcher.data.mem;

import healthwatcher.data.ISpecialityRepository;
import healthwatcher.model.healthguide.MedicalSpeciality;

import java.util.Arrays;

import lib.exceptions.ObjectAlreadyInsertedException;
import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.RepositoryException;
import lib.util.ConcreteIterator;
import lib.util.IteratorDsk;

public class SpecialityRepositoryArray implements ISpecialityRepository {

	private MedicalSpeciality[] vetor;

	private int indice;

	private int ponteiro;

	public SpecialityRepositoryArray() {
		vetor = new MedicalSpeciality[100];
		indice = 0;
	}

	public void update(MedicalSpeciality specialty) throws RepositoryException,
			ObjectNotFoundException {
		int i = getIndex(specialty.getCodigo());
		if (i == indice) {
			throw new ObjectNotFoundException("Specialty not found");
		} else {
			vetor[i] = specialty;
		}
	}

	public boolean exists(int code) throws RepositoryException {

		boolean flag = false;

		for (int i = 0; i < indice; i++) {
			if (this.vetor[i].getCodigo() == code) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	public boolean hasNext() {
		return ponteiro < indice;
	}

	public IteratorDsk getSpecialityList() throws RepositoryException, ObjectNotFoundException {
		if (indice == 0)
			throw new ObjectNotFoundException("There isn't registered Health units");
		return new ConcreteIterator(Arrays.asList(vetor));
	}

	public void reset() {
		this.ponteiro = 0;
	}

	public void insert(MedicalSpeciality specialty) throws RepositoryException,
			ObjectAlreadyInsertedException {
		if (specialty == null) {
			throw new IllegalArgumentException();
		}
		this.vetor[indice] = specialty;
		indice++;
	}

	public MedicalSpeciality search(int code) throws RepositoryException, ObjectNotFoundException {
		MedicalSpeciality response = null;
		int i = getIndex(code);
		if (i == indice) {
			throw new ObjectNotFoundException("Specialty not found");
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
			throw new ObjectNotFoundException("Specialty not found");
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
			temp = vetor[i].getCodigo();
			if (temp == code) {
				flag = true;
			} else {
				i = i + 1;
			}
		}
		return i;
	}
}