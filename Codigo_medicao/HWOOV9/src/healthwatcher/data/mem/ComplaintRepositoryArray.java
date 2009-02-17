package healthwatcher.data.mem;

import healthwatcher.data.IComplaintRepository;
import healthwatcher.model.complaint.Complaint;
import lib.exceptions.ObjectAlreadyInsertedException;
import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.RepositoryException;
import lib.util.IteratorDsk;



public class ComplaintRepositoryArray implements IComplaintRepository {

	private Complaint[] vetor;

	private int indice;

	private int ponteiro;

	public ComplaintRepositoryArray() {
		vetor = new Complaint[5000];
		indice = 0;
	}

	//nao levanta objeto invalido exception
	public int insert(Complaint q) throws RepositoryException, ObjectAlreadyInsertedException {
		synchronized (this) {
			if (q == null) {
				throw new IllegalArgumentException();
			}
			this.vetor[indice] = q;
			indice++;
			return vetor[indice - 1].getCodigo();
		}
	}

	public void update(Complaint q) throws RepositoryException, ObjectNotFoundException {
		synchronized (this) {
			int i = getIndex(q.getCodigo());
			if (i == indice) {
				throw new ObjectNotFoundException("Complaint not found");
			} else {
				vetor[i] = q;
			}
		}
	}

	private int getIndex(int code) {
		synchronized (this) {
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

	public boolean exists(int codigo) throws RepositoryException {
		synchronized (this) {
			int i = getIndex(codigo);
			return (i != indice);
		}
	}

	public Complaint search(int codigo) throws RepositoryException, ObjectNotFoundException {
		synchronized (this) {
			Complaint response = null;
			int i = getIndex(codigo);
			if (i == indice) {
				throw new ObjectNotFoundException("Complaint not found");
			} else {
				response = vetor[i];
			}
			return response;
		}
	}

	public void reset() {
		synchronized (this) {
			this.ponteiro = 0;
		}
	}

	public Object next() {
		synchronized (this) {
			if (ponteiro >= indice) {
				return null;
			} else {
				return vetor[ponteiro++];
			}
		}
	}

	public boolean hasNext() {
		synchronized (this) {
			return ponteiro < indice;
		}
	}

	public void remove(int codigo) throws RepositoryException, ObjectNotFoundException {
		synchronized (this) {
			int i = getIndex(codigo);
			if (i == indice) {
				throw new ObjectNotFoundException("Complaint not found");
			} else {
				vetor[i] = vetor[indice - 1];
				indice = indice - 1;
			}
		}
	}

	public IteratorDsk getComplaintList() {
		synchronized (this) {
			return null;
		}
	}
}