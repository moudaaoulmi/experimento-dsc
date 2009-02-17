package healthwatcher.business.healthguide;

import healthwatcher.data.IHealthUnitRepository;
import healthwatcher.model.healthguide.HealthUnit;
import lib.exceptions.ObjectAlreadyInsertedException;
import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.ObjectNotValidException;
import lib.exceptions.RepositoryException;
import lib.util.ConcreteIterator;
import lib.util.IteratorDsk;

public class HealthUnitRecord {

	private IHealthUnitRepository healthUnitRep;

	public HealthUnitRecord(IHealthUnitRepository repUnidadeSaude) {
		this.healthUnitRep = repUnidadeSaude;
	}

	public void update(HealthUnit unit) throws RepositoryException, ObjectNotFoundException,
			ObjectNotValidException {
		healthUnitRep.update(unit);
	}

	public IteratorDsk searchSpecialityByHealthUnit(int code) throws ObjectNotFoundException,
			RepositoryException {
		HealthUnit us = healthUnitRep.search(code);
		return new ConcreteIterator(us.getSpecialities());
	}

	public IteratorDsk searchHealthUnitsBySpeciality(int code) throws ObjectNotFoundException,
			RepositoryException {
		return healthUnitRep.getHealthUnitListBySpeciality(code);
	}

	public IteratorDsk getHealthUnitList() throws RepositoryException, ObjectNotFoundException {
		return healthUnitRep.getHealthUnitList();
	}

	public IteratorDsk getPartialHealthUnitList() throws RepositoryException,
			ObjectNotFoundException {
		return healthUnitRep.getPartialHealthUnitList();
	}

	public HealthUnit search(int healthUnitCode) throws ObjectNotFoundException,
			RepositoryException {
		return healthUnitRep.search(healthUnitCode);
	}
	
	public void insert(HealthUnit us) throws ObjectNotValidException,
			ObjectAlreadyInsertedException, ObjectNotValidException, RepositoryException {
		healthUnitRep.insert(us);
	}
}