package healthwatcher.aspects.distribution;

import healthwatcher.model.complaint.Complaint;
import healthwatcher.model.complaint.DiseaseType;
import healthwatcher.model.complaint.Symptom;
import healthwatcher.model.employee.Employee;
import healthwatcher.model.healthguide.HealthUnit;
import healthwatcher.model.healthguide.MedicalSpeciality;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import lib.exceptions.InsertEntryException;
import lib.exceptions.ObjectAlreadyInsertedException;
import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.ObjectNotValidException;
import lib.exceptions.RepositoryException;
import lib.exceptions.UpdateEntryException;
import lib.util.IteratorDsk;

/**
 * A remote facade for use with rmi
 * This is just a copy of IFacade but throwing remote exception in every method
 */
public interface IRemoteFacade extends Remote, Serializable {

	public void updateComplaint(Complaint q) throws RepositoryException, ObjectNotFoundException,
			ObjectNotValidException, RemoteException;

	public IteratorDsk searchSpecialitiesByHealthUnit(int code) throws ObjectNotFoundException,
			RepositoryException, RemoteException;

	public Complaint searchComplaint(int code) throws RepositoryException, ObjectNotFoundException, RemoteException;

	public DiseaseType searchDiseaseType(int code) throws RepositoryException,
			ObjectNotFoundException, RemoteException;

	public IteratorDsk searchHealthUnitsBySpeciality(int code) throws ObjectNotFoundException,
			RepositoryException, RemoteException;

	public IteratorDsk getSpecialityList() throws RepositoryException, ObjectNotFoundException, RemoteException;

	public IteratorDsk getDiseaseTypeList() throws RepositoryException, ObjectNotFoundException, RemoteException;

	public IteratorDsk getHealthUnitList() throws RepositoryException, ObjectNotFoundException, RemoteException;

	public IteratorDsk getPartialHealthUnitList() throws RepositoryException,
			ObjectNotFoundException, RemoteException;

	public int insertComplaint(Complaint complaint) throws RepositoryException,
			ObjectAlreadyInsertedException, ObjectNotValidException, RemoteException;

	public void updateHealthUnit(HealthUnit unit) throws RepositoryException,
			ObjectNotFoundException, ObjectNotValidException, RemoteException;

	public IteratorDsk getComplaintList() throws RepositoryException, ObjectNotFoundException, RemoteException;

	public void insert(Employee e) throws ObjectAlreadyInsertedException, ObjectNotValidException,
			RepositoryException, RemoteException;

	public void updateEmployee(Employee e) throws RepositoryException, ObjectNotFoundException,
			ObjectNotValidException, UpdateEntryException, RemoteException;

	public Employee searchEmployee(String login) throws RepositoryException,
			ObjectNotFoundException, ObjectNotValidException, UpdateEntryException, RemoteException;

	public HealthUnit searchHealthUnit(int healthUnitCode) throws ObjectNotFoundException,
			RepositoryException, RemoteException;
	
	public void insert(HealthUnit us) throws InsertEntryException, ObjectAlreadyInsertedException,
			ObjectNotValidException, RemoteException;

	public void insert(MedicalSpeciality speciality) throws ObjectAlreadyInsertedException,
			ObjectNotValidException, InsertEntryException, RemoteException;

	public void insert(Symptom symptom) throws ObjectAlreadyInsertedException,
			InsertEntryException, ObjectNotValidException, RemoteException;

	public Symptom searchSymptom(int numSymptom) throws ObjectNotFoundException, RemoteException;

	public IteratorDsk getSymptomList() throws ObjectNotFoundException, RemoteException;

	public void updateSymptom(Symptom symptom) throws ObjectNotFoundException,
			ObjectNotValidException, RemoteException;

	public MedicalSpeciality searchSpecialitiesByCode(int numSpeciality)
			throws ObjectNotFoundException, RemoteException;

	public void updateMedicalSpeciality(MedicalSpeciality speciality)
			throws ObjectNotFoundException, ObjectNotValidException, RemoteException;

	public void insert(DiseaseType diseaseType) throws InsertEntryException,
			ObjectAlreadyInsertedException, ObjectNotValidException, RemoteException;
}
