package healthwatcher.business.healthguide;

import lib.exceptions.ObjectAlreadyInsertedException;
import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.ObjectNotValidException;
import lib.exceptions.RepositoryException;
import lib.util.IteratorDsk;
import healthwatcher.data.ISpecialityRepository;
import healthwatcher.model.healthguide.MedicalSpeciality;

public class MedicalSpecialityRecord {

	private ISpecialityRepository repEspecialidade;

	public MedicalSpecialityRecord(ISpecialityRepository repEspecialidade) {
		this.repEspecialidade = repEspecialidade;
	}

	public IteratorDsk getListaEspecialidade() throws RepositoryException, ObjectNotFoundException {
		return repEspecialidade.getSpecialityList();
	}
	
	public void insert(MedicalSpeciality esp) throws ObjectNotValidException,
			ObjectAlreadyInsertedException, ObjectNotValidException, RepositoryException {
		repEspecialidade.insert(esp);
	}

	public void update(MedicalSpeciality esp) throws ObjectNotValidException,
			ObjectNotFoundException, ObjectNotValidException, RepositoryException {
		repEspecialidade.update(esp);
	}

	public MedicalSpeciality search(int codigo) throws ObjectNotFoundException, RepositoryException {
		return repEspecialidade.search(codigo);
	}
}
