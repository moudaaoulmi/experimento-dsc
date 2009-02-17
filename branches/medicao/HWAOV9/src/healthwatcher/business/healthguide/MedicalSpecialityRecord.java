package healthwatcher.business.healthguide;

import healthwatcher.data.ISpecialityRepository;
import healthwatcher.model.healthguide.MedicalSpeciality;
import lib.exceptions.ObjectAlreadyInsertedException;
import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.ObjectNotValidException;
import lib.util.IteratorDsk;

public class MedicalSpecialityRecord {

	private ISpecialityRepository repEspecialidade;

	public MedicalSpecialityRecord(ISpecialityRepository repEspecialidade) {
		this.repEspecialidade = repEspecialidade;
	}

	public IteratorDsk getListaEspecialidade() throws ObjectNotFoundException {
		return repEspecialidade.getSpecialityList();
	}
	
	public void insert(MedicalSpeciality esp) throws ObjectAlreadyInsertedException, ObjectNotValidException {
		repEspecialidade.insert(esp);
	}

	public void update(MedicalSpeciality esp) throws ObjectNotFoundException, ObjectNotValidException {
		repEspecialidade.update(esp);
	}

	public MedicalSpeciality search(int codigo) throws ObjectNotFoundException {
		return repEspecialidade.search(codigo);
	}
}
