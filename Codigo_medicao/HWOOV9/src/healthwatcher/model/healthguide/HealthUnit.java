package healthwatcher.model.healthguide;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.ObjectNotValidException;
import lib.exceptions.RepositoryException;
import lib.exceptions.TransactionException;
import lib.patterns.observer.Observer;
import lib.patterns.observer.Subject;

public class HealthUnit implements Serializable, Subject {

	private int code;

	private String description;

	private List specialities;

	private List subscribers = new ArrayList();
	
	public HealthUnit() {
	}

	public HealthUnit(String description, List specialities) {
		this.description = description;
		this.specialities = specialities;
	}

	public boolean hasSpeciality(int code) {
		for(Iterator i = specialities.iterator(); i.hasNext();) {
			MedicalSpeciality m = (MedicalSpeciality) i.next();
			if (m.getCodigo() == code) {
				return true;
			}
		}
		return false;
	}
	
	public int getCode() {
		return this.code;
	}

	public String getDescription() {
		return this.description;
	}

	public List getSpecialities() {
		return this.specialities;
	}

	public void setCode(int cod) {
		this.code = cod;
	}

	public void setDescription(String descricao) {
		this.description = descricao;
		notifyObservers();
	}

	public String toString() {
		return description;
	}
	
	public void addObserver(Observer observer) {
		subscribers.add(observer);
	}

	public void removeObserver(Observer observer) {
		subscribers.remove(observer);
	}

	public void notifyObservers() {
		for (Iterator it = subscribers.iterator(); it.hasNext();) {
			Observer observer = (Observer) it.next();
			try {
				observer.notify(this);
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (ObjectNotValidException e) {
				e.printStackTrace();
			} catch (ObjectNotFoundException e) {
				e.printStackTrace();
			} catch (TransactionException e) {
				e.printStackTrace();
			} catch (RepositoryException e) {
				e.printStackTrace();
			}
		}
	}  
}