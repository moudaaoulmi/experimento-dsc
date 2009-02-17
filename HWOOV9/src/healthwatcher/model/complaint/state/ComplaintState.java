package healthwatcher.model.complaint.state;

import healthwatcher.model.address.Address;
import healthwatcher.model.complaint.Complaint;
import healthwatcher.model.complaint.Situation;
import healthwatcher.model.employee.Employee;

import java.io.Serializable;

import lib.util.Date;


public abstract class ComplaintState implements Serializable {
	
	protected int             code;
	protected String          complainer;
	protected String          description;
	protected String          observation;
	protected String          email;
	protected Employee		  attendant;
	protected Date            medicalOpinionDate;
	protected Date            complaintDate;
	protected Address         complainerAddress;
	protected long timestamp;
	
	public ComplaintState(){
	}
	
	public ComplaintState(int codigo,String solicitante, String descricao,
                  String observacao, String email, Employee atendente,
                  Date dataParecer, Date dataQueixa,
                  Address enderecoSolicitante, long timestamp){
		this.code              = codigo;
        this.complainer         = solicitante;
        this.description           = descricao;
        this.observation          = observacao;
        this.email               = email;
        this.attendant           = atendente;
        this.medicalOpinionDate  = dataParecer;
        this.complaintDate          = dataQueixa;
        this.complainerAddress = enderecoSolicitante;
        this.timestamp = timestamp;
	}
    
    public abstract void setAttendant(Employee atendente, Complaint complaint);
    public abstract void setCode(int cod);
    public abstract void setMedicalOpinionDate(Date data);
    public abstract void setComplaintDate(Date data);
    public abstract void setDescription(String desc);
    public abstract void setEmail(String email);
    public abstract void setComplainerAddress(Address end);
    public abstract void setObservation(String obs, Complaint complaint);
    public abstract void setComplainer(String solicitante);
    public abstract void setTimestamp(long newTimestamp);
    public abstract int getStatus();
    
    public Employee getAttendant() {
        return this.attendant;
    }
    public int getCode() {
        return this.code;
    }
    public Date getMedicalOpinionDate() {
        return this.medicalOpinionDate;
    }
    public Date getComplaintDate() {
        return this.complaintDate;
    }
    public String getDescription() {
        return this.description;
    }
    public String getEmail() {
        return this.email;
    }
    public Address getComplainerAddress() {
        return this.complainerAddress;
    }
    public String getObservation() {
        return this.observation;
    }
    public String getComplainer() {
        return this.complainer;
    }
    public long getTimestamp() {
    	return timestamp;
    }
    public void incTimestamp() {
        this.timestamp = timestamp + 1;
    }
    
    public void setStatus(int sit, Complaint complaint) {
    	if(sit!=getStatus()){
    		if(sit==Situation.QUEIXA_ABERTA){
    			complaint.setComplaintState(new ComplaintStateOpen(code,complainer, description,
    	                observation, email, attendant,
    	                medicalOpinionDate, complaintDate,
    	                complainerAddress, timestamp));
    		}else if(sit==Situation.QUEIXA_FECHADA){
    			 complaint.setComplaintState(new ComplaintStateClosed(code,complainer, description,
    	                observation, email, attendant,
    	                medicalOpinionDate, complaintDate,
    	                complainerAddress, timestamp));
    		}else if(sit==Situation.QUEIXA_SUSPENSA){
    			
    		}
    	}
    }
}
