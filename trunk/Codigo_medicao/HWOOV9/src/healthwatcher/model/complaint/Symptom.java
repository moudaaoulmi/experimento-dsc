package healthwatcher.model.complaint;

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

public class Symptom implements java.io.Serializable, Subject {

	private int code;

	private String description;

	private List subscribers = new ArrayList();
	
	public Symptom(String descricao) {
		this.description = descricao;
	}

	public int getCode() {
		return this.code;
	}

	public String getDescription() {
		return this.description;
	}

	public void setCode(int codigo) {
		this.code = codigo;
	}
	
	public void setDescription(String description) {
		this.description = description;
		notifyObservers();
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