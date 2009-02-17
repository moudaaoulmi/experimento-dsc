package healthwatcher.aspects.concurrency;

import healthwatcher.business.complaint.DiseaseRecord;
import healthwatcher.business.complaint.SymptomRecord;
import healthwatcher.business.employee.EmployeeRecord;
import healthwatcher.model.complaint.DiseaseType;
import healthwatcher.model.complaint.Symptom;
import healthwatcher.model.employee.Employee;
import lib.concurrency.ConcurrencyManager;

/**
 * This aspect synchronizes a method from employee record using a concurrency manager
 */
public aspect HWManagedSynchronization {

	private ConcurrencyManager emplyoeeManager = new ConcurrencyManager();
	private ConcurrencyManager diseaseManager = new ConcurrencyManager();
	private ConcurrencyManager symptomManager = new ConcurrencyManager();
	
	public pointcut synchronizationPoints(Object o) :
		(execution(* EmployeeRecord.insert(Employee)) ||
		 execution(* DiseaseRecord.insert(DiseaseType)) ||
		 execution(* SymptomRecord.insert(Symptom)) )&& args(o);
	
	before(Object o) : synchronizationPoints(o) {
		ConcurrencyManager manager = getManager(o);
		if (manager != null) {
			manager.beginExecution(getKey(o));
		}
	}
	
	after(Object o) : synchronizationPoints(o) {
		ConcurrencyManager manager = getManager(o);
		if (manager != null) {
			manager.endExecution(getKey(o));
		}
	}
	
	protected ConcurrencyManager getManager(Object o) {
		if (o instanceof Employee) {
			return emplyoeeManager;
		}
		if (o instanceof DiseaseType) {
			return diseaseManager;
		}
		if (o instanceof Symptom) {
			return symptomManager;
		}
		return null;
	}
	
	protected Object getKey(Object o) {
		if (o instanceof Employee) {
			return ((Employee) o).getLogin();
		}
		if (o instanceof DiseaseType) {
			return "" + ((DiseaseType) o).getCode();
		}
		if (o instanceof Symptom) {
			return "" + ((Symptom) o).getCode();
		}
		return null;
	}
	
}
