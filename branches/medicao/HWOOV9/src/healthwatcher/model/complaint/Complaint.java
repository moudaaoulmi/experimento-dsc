package healthwatcher.model.complaint;





import healthwatcher.model.address.Address;
import healthwatcher.model.complaint.state.ComplaintState;
import healthwatcher.model.complaint.state.ComplaintStateClosed;
import healthwatcher.model.complaint.state.ComplaintStateOpen;
import healthwatcher.model.employee.Employee;

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
import lib.util.Date;

//classe queixa eh uma classe basica que tem subclasses
public abstract class Complaint implements Serializable, Subject {

	public static final int QUEIXA_ALIMENTAR = 1;

	public static final int QUEIXA_ANIMAL = 2;

	public static final int QUEIXA_DIVERSA = 3;
	
	private ComplaintState state;

	private List subscribers = new ArrayList();
	
    public Complaint() {
    	state= new ComplaintStateOpen();
    }

	public Complaint(String solicitante, String descricao, String observacao, String email,
			Employee atendente, int situacao, Date dataParecer, Date dataQueixa,
			Address enderecoSolicitante, long timestamp) {

	     if(situacao==Situation.QUEIXA_ABERTA)
	         state= new ComplaintStateOpen(0,solicitante, descricao,
	                 observacao, email, atendente,
	                 dataParecer, dataQueixa,
	                 enderecoSolicitante, timestamp);
	         else if(situacao==Situation.QUEIXA_FECHADA)
	         	state= new ComplaintStateClosed(0,solicitante, descricao,
	                     observacao, email, atendente,
	                     dataParecer, dataQueixa,
	                     enderecoSolicitante, timestamp);
	}
	public void setComplaintState(ComplaintState _state){
	    state= _state;
	    notifyObservers();
	}

	public Employee getAtendente() {
		return state.getAttendant();
	}

	public void setAtendente(Employee atendente) {
		state.setAttendant(atendente, this);
	}

	public int getCodigo() {
		return state.getCode();
	}

	public void setCodigo(int codigo) {
		state.setCode(codigo);
	}

	public Date getDataParecer() {
		return state.getMedicalOpinionDate();
	}

	public void setDataParecer(Date dataParecer) {
		state.setMedicalOpinionDate(dataParecer);
	}

	public Date getDataQueixa() {
		return state.getComplaintDate();
	}

	public void setDataQueixa(Date dataQueixa) {
		state.setComplaintDate(dataQueixa);
	}

	public String getDescricao() {
		return state.getDescription();
	}

	public void setDescricao(String descricao) {
		state.setDescription(descricao);
	}

	public String getEmail() {
		return state.getEmail();
	}

	public void setEmail(String email) {
		state.setEmail(email);
	}

	public Address getEnderecoSolicitante() {
		return state.getComplainerAddress();
	}

	public void setEnderecoSolicitante(Address enderecoSolicitante) {
		state.setComplainerAddress(enderecoSolicitante);
	}

	public String getObservacao() {
		return state.getObservation();
	}

	public void setObservacao(String observacao) {
		state.setObservation(observacao, this);
	}

	public int getSituacao() {
		return state.getStatus();
	}

	public void setSituacao(int situacao) {
		state.setStatus(situacao, this);
	}

	public String getSolicitante() {
		return state.getComplainer();
	}

	public void setSolicitante(String solicitante) {
		state.setComplainer(solicitante);
	}

	public long getTimestamp() {
		return state.getTimestamp();
	}

	public void setTimestamp(long timestamp) {
		state.setTimestamp(timestamp);
	}

	public void incTimestamp() {
		state.incTimestamp();
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
		// We need to increment the client side object, because notifying observers
		// will cause the server side to be incremented and the next update will fail
		// Note that this decision requires knowing that the only observer to this
		// class in going to update the complaint, that this observer is in another
		// machine and that there is timestamping installed. Several concerns are needed
		// to determine this single line, which is actually error prone, since having an
		// observer that does not update the complaint will probably cause errors.
		if (! subscribers.isEmpty()) {
			this.incTimestamp();
		}
	}  
}