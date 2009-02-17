package healthwatcher.business.complaint;

import healthwatcher.data.IDiseaseRepository;
import healthwatcher.model.complaint.DiseaseType;
import lib.exceptions.ExceptionMessages;
import lib.exceptions.ObjectAlreadyInsertedException;
import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.ObjectNotValidException;
import lib.util.IteratorDsk;



public class DiseaseRecord {

	private IDiseaseRepository diseaseRep;

	public DiseaseRecord(IDiseaseRepository repTipoDoenca) {
		this.diseaseRep = repTipoDoenca;
	}

	public DiseaseType search(int codigo) throws ObjectNotFoundException {
		return diseaseRep.search(codigo);
	}

	public IteratorDsk getDiseaseTypeList() throws ObjectNotFoundException {
		return diseaseRep.getDiseaseTypeList();
	}
	
	public void insert(DiseaseType td) throws ObjectAlreadyInsertedException, 
		ObjectNotValidException {
		
		if (diseaseRep.exists(td.getCode())) {
			throw new ObjectAlreadyInsertedException(ExceptionMessages.EXC_JA_EXISTE);
		} else {
			this.diseaseRep.insert(td);
		}
	}
}