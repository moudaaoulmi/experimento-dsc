package healthwatcher.model.employee;

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

public class Employee implements Serializable, Subject {

	private String name;

	private String login;

	private String password;
	
	private List subscribers = new ArrayList();

	public Employee(String login, String password, String name) {
		this.name = name;
		this.login = login;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		notifyObservers();
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
		notifyObservers();
	}

	public void setPassword(String password) {
		this.password = password;
		notifyObservers();
	}

	public String getPassword() {
		return password;
	}

	public boolean validatePassword(String password) {
		return this.password.equals(password);
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