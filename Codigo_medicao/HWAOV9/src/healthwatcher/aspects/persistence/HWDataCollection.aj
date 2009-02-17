package healthwatcher.aspects.persistence;

import healthwatcher.business.complaint.ComplaintRecord;
import healthwatcher.business.complaint.DiseaseRecord;
import healthwatcher.business.complaint.SymptomRecord;
import healthwatcher.business.employee.EmployeeRecord;
import healthwatcher.business.healthguide.HealthUnitRecord;
import healthwatcher.business.healthguide.MedicalSpecialityRecord;
import healthwatcher.data.factories.AbstractRepositoryFactory;
import lib.exceptions.RepositoryException;
import lib.persistence.IPersistenceMechanism;

/**
 * This aspect intercepts Record creations and inserts Records with the proper repository
 * instance, depending if it is persistent or not
 */
public aspect HWDataCollection {
	
	private interface SystemRecord {};
	
	declare parents: ComplaintRecord ||
					 MedicalSpecialityRecord ||
					 HealthUnitRecord ||
					 EmployeeRecord ||
					 SymptomRecord ||
					 DiseaseRecord implements SystemRecord;

	Object around(): call(SystemRecord+.new(..)) &&	! within(HWDataCollection+) {
		return getSystemRecord(thisJoinPoint.getSignature().getDeclaringType());
	}
	

    declare soft: RepositoryException : execution(* SystemRecord+.*(..));
    
	/**
	 * Creates a system record depending on the class type
	 */
	protected SystemRecord getSystemRecord(Class type) {

		AbstractRepositoryFactory factory = AbstractRepositoryFactory.getRepositoryFactory();
		
		if (type.equals(ComplaintRecord.class)) {
			return new ComplaintRecord(factory.createComplaintRepository());
		} else  if (type.equals(HealthUnitRecord.class)) {
			return new HealthUnitRecord(factory.createHealthUnitRepository());
		} else  if (type.equals(MedicalSpecialityRecord.class)) {
			return new MedicalSpecialityRecord(factory.createMedicalSpecialityRepository());
		} else  if (type.equals(EmployeeRecord.class)) {
			return new EmployeeRecord(factory.createEmployeeRepository());
		} else if(type.equals(DiseaseRecord.class)){
			return new DiseaseRecord(factory.createDiseaseRepository());
		} else if(type.equals(SymptomRecord.class)){
			return new SymptomRecord(factory.createSymptomRepository());
		}
		return null;
	}

 	/**
 	 * Returns the persistence mechanism in use
 	 */
 	protected IPersistenceMechanism getPm() {
 		return HWPersistence.aspectOf().getPm();
 	}
}
