package healthwatcher.business.complaint;

import healthwatcher.data.ISymptomRepository;
import healthwatcher.model.complaint.Symptom;
import lib.exceptions.ExceptionMessages;
import lib.exceptions.ObjectAlreadyInsertedException;
import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.ObjectNotValidException;
import lib.util.IteratorDsk;


public class SymptomRecord {

	private ISymptomRepository rep;
	
	public SymptomRecord(ISymptomRepository rep) {
		this.rep = rep;
	}

	public void insert(Symptom symptom) throws ObjectAlreadyInsertedException, ObjectNotValidException {
		if (rep.exists(symptom.getCode())) {
			throw new ObjectAlreadyInsertedException(ExceptionMessages.EXC_JA_EXISTE);
		} else {
			rep.insert(symptom);
		}
	}

	public Symptom search(int code) throws ObjectNotFoundException {
		return rep.search(code);
	}

	public IteratorDsk getSymptomList() throws ObjectNotFoundException {
		return rep.getSymptomList();
	}

	public void update(Symptom symptom) throws ObjectNotFoundException,	ObjectNotValidException {
		rep.update(symptom);
	}
}