package healthwatcher.view;

import healthwatcher.model.complaint.Complaint;
import healthwatcher.model.complaint.DiseaseType;
import healthwatcher.model.complaint.Symptom;
import healthwatcher.model.employee.Employee;
import healthwatcher.model.healthguide.HealthUnit;
import healthwatcher.model.healthguide.MedicalSpeciality;
import lib.exceptions.InsertEntryException;
import lib.exceptions.ObjectAlreadyInsertedException;
import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.ObjectNotValidException;
import lib.exceptions.UpdateEntryException;
import lib.util.IteratorDsk;

public interface IFacade {

	public void updateComplaint(Complaint q) throws ObjectNotFoundException,
			ObjectNotValidException;

	public IteratorDsk searchSpecialitiesByHealthUnit(int code) throws ObjectNotFoundException;

	public Complaint searchComplaint(int code) throws ObjectNotFoundException;

	public DiseaseType searchDiseaseType(int code) throws ObjectNotFoundException;

	public IteratorDsk searchHealthUnitsBySpeciality(int code) throws ObjectNotFoundException;

	public IteratorDsk getSpecialityList() throws ObjectNotFoundException;

	public IteratorDsk getDiseaseTypeList() throws ObjectNotFoundException;

	public IteratorDsk getHealthUnitList() throws ObjectNotFoundException;

	public IteratorDsk getPartialHealthUnitList() throws ObjectNotFoundException;

	public int insertComplaint(Complaint complaint) throws ObjectAlreadyInsertedException,
			ObjectNotValidException;

	public void updateHealthUnit(HealthUnit unit) throws ObjectNotFoundException,
			ObjectNotValidException;

	public IteratorDsk getComplaintList() throws ObjectNotFoundException;

	public void insert(Employee e) throws ObjectAlreadyInsertedException, ObjectNotValidException;

	public void updateEmployee(Employee e) throws ObjectNotFoundException, ObjectNotValidException,
			UpdateEntryException;

	public Employee searchEmployee(String login) throws ObjectNotFoundException,
			ObjectNotValidException, UpdateEntryException;

	public HealthUnit searchHealthUnit(int healthUnitCode) throws ObjectNotFoundException;
	
	public void insert(HealthUnit us) throws InsertEntryException, ObjectAlreadyInsertedException,
			ObjectNotValidException;

	public void insert(MedicalSpeciality speciality) throws ObjectAlreadyInsertedException,
			ObjectNotValidException, InsertEntryException;

	public void insert(Symptom symptom) throws ObjectAlreadyInsertedException,
			InsertEntryException, ObjectNotValidException;

	public Symptom searchSymptom(int numSymptom) throws ObjectNotFoundException;

	public IteratorDsk getSymptomList() throws ObjectNotFoundException;

	public void updateSymptom(Symptom symptom) throws ObjectNotFoundException,
			ObjectNotValidException;

	public MedicalSpeciality searchSpecialitiesByCode(int numSpeciality)
			throws ObjectNotFoundException;

	public void updateMedicalSpeciality(MedicalSpeciality speciality)
			throws ObjectNotFoundException, ObjectNotValidException;

	public void insert(DiseaseType diseaseType) throws InsertEntryException,
			ObjectAlreadyInsertedException, ObjectNotValidException;
}
