package healthwatcher.business;

import healthwatcher.business.complaint.ComplaintRecord;
import healthwatcher.business.complaint.DiseaseRecord;
import healthwatcher.business.complaint.SymptomRecord;
import healthwatcher.business.employee.EmployeeRecord;
import healthwatcher.business.healthguide.HealthUnitRecord;
import healthwatcher.business.healthguide.MedicalSpecialityRecord;
import healthwatcher.model.complaint.Complaint;
import healthwatcher.model.complaint.DiseaseType;
import healthwatcher.model.complaint.Symptom;
import healthwatcher.model.employee.Employee;
import healthwatcher.model.healthguide.HealthUnit;
import healthwatcher.model.healthguide.MedicalSpeciality;
import healthwatcher.view.IFacade;
import lib.exceptions.InsertEntryException;
import lib.exceptions.ObjectAlreadyInsertedException;
import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.ObjectNotValidException;
import lib.util.IteratorDsk;

public class HealthWatcherFacade implements IFacade {
	
	private static HealthWatcherFacade singleton; // padrao singleton
	
	private ComplaintRecord complaintRecord;

	private HealthUnitRecord healthUnitRecord;

	private MedicalSpecialityRecord specialityRecord;

	private DiseaseRecord diseaseRecord;

	private EmployeeRecord employeeRecord;

	private SymptomRecord symptomRecord;
	
	private HealthWatcherFacade() {
		this.complaintRecord = new ComplaintRecord(null);
		this.healthUnitRecord = new HealthUnitRecord(null);
		this.specialityRecord = new MedicalSpecialityRecord(null);
		this.diseaseRecord = new DiseaseRecord(null);
		this.employeeRecord = new EmployeeRecord(null);
		this.symptomRecord = new SymptomRecord(null);
	}

	public synchronized static HealthWatcherFacade getInstance() {
		if (singleton == null) {
			singleton = new HealthWatcherFacade();
		}
		return singleton;
	}
			
	public void updateHealthUnit(HealthUnit unit) throws ObjectNotFoundException, ObjectNotValidException {
		healthUnitRecord.update(unit);
	}

	public void updateComplaint(Complaint complaint) throws ObjectNotFoundException, ObjectNotValidException {
		complaintRecord.update(complaint);
	}

	public IteratorDsk searchSpecialitiesByHealthUnit(int code) throws ObjectNotFoundException {
		return healthUnitRecord.searchSpecialityByHealthUnit(code);
	}

	public Complaint searchComplaint(int code) throws ObjectNotFoundException {
		return complaintRecord.search(code);
	}

	public DiseaseType searchDiseaseType(int code) throws ObjectNotFoundException {
		return diseaseRecord.search(code);
	}

	public IteratorDsk searchHealthUnitsBySpeciality(int code) throws ObjectNotFoundException {
		return healthUnitRecord.searchHealthUnitsBySpeciality(code);
	}

	public IteratorDsk getSpecialityList() throws ObjectNotFoundException {
		return specialityRecord.getListaEspecialidade();
	}

	public IteratorDsk getDiseaseTypeList() throws ObjectNotFoundException {
		return diseaseRecord.getDiseaseTypeList();
	}

	public HealthUnit searchHealthUnit(int healthUnitCode) throws ObjectNotFoundException {
		return healthUnitRecord.search(healthUnitCode);
	}

	public IteratorDsk getHealthUnitList() throws ObjectNotFoundException {
		return healthUnitRecord.getHealthUnitList();
	}

	public IteratorDsk getPartialHealthUnitList() throws ObjectNotFoundException {
		return healthUnitRecord.getPartialHealthUnitList();
	}

	public void insert(Employee employee) throws ObjectAlreadyInsertedException,
			ObjectNotValidException {
		employeeRecord.insert(employee);
	}

	public int insertComplaint(Complaint complaint) throws ObjectAlreadyInsertedException, ObjectNotValidException {
		return complaintRecord.insert(complaint);
	}

	public Employee searchEmployee(String login) throws ObjectNotFoundException {
		return employeeRecord.search(login);
	}

	public IteratorDsk getComplaintList() throws ObjectNotFoundException {
		return complaintRecord.getComplaintList();
	}

	public void updateEmployee(Employee employee) throws ObjectNotFoundException,
			ObjectNotValidException {
		employeeRecord.update(employee);
	}
	
	public void insert(HealthUnit us) throws InsertEntryException, ObjectAlreadyInsertedException,
			ObjectNotValidException {
		healthUnitRecord.insert(us);
	}

	public void insert(MedicalSpeciality speciality) throws ObjectAlreadyInsertedException,
			ObjectNotValidException, InsertEntryException {
		specialityRecord.insert(speciality);
	}

	public void insert(Symptom symptom) throws ObjectAlreadyInsertedException,
			InsertEntryException, ObjectNotValidException {
		symptomRecord.insert(symptom);
	}

	public Symptom searchSymptom(int numSymptom) throws ObjectNotFoundException {
		return symptomRecord.search(numSymptom);
	}

	public IteratorDsk getSymptomList() throws ObjectNotFoundException {
		return symptomRecord.getSymptomList();
	}

	public void updateSymptom(Symptom symptom) throws ObjectNotFoundException,
			ObjectNotValidException {
		symptomRecord.update(symptom);
	}

	public MedicalSpeciality searchSpecialitiesByCode(int numSpeciality)
			throws ObjectNotFoundException {
		return specialityRecord.search(numSpeciality);
	}

	public void updateMedicalSpeciality(MedicalSpeciality speciality)
			throws ObjectNotFoundException, ObjectNotValidException {
		specialityRecord.update(speciality);
	}

	public void insert(DiseaseType diseaseType) throws InsertEntryException,
			ObjectAlreadyInsertedException, ObjectNotValidException {
		diseaseRecord.insert(diseaseType);
	}
}