package healthwatcher.model.complaint.state;

import healthwatcher.model.address.Address;
import healthwatcher.model.complaint.Complaint;
import healthwatcher.model.complaint.Situation;
import healthwatcher.model.employee.Employee;
import lib.util.Date;


public class ComplaintStateClosed extends ComplaintState  {
	
	public ComplaintStateClosed(){
	}

	public ComplaintStateClosed(int codigo,String solicitante, String descricao,
            String observacao, String email, Employee atendente,
            Date dataParecer, Date dataQueixa,
            Address enderecoSolicitante, long timestamp) {
		super(codigo,solicitante, descricao,
                observacao, email, atendente,
                dataParecer, dataQueixa,
                enderecoSolicitante, timestamp);
	}
	public void setAttendant(Employee atend, Complaint complaint) {
    }
    public void setCode(int cod) {
    }
    public void setMedicalOpinionDate(Date data) {
    }
    public void setComplaintDate(Date data) {
    }
    public void setDescription(String desc) {
    }
    public void setEmail(String _email) {
    }
    public void setComplainerAddress(Address end) {
    }
    public void setObservation(String obs, Complaint complaint) {
    }
    public void setSituacao(int sit) {
    }
    public void setComplainer(String _solicitante) {
    }
    public void setTimestamp(long newTimestamp) {
    }
	public int getStatus() {
		return Situation.QUEIXA_FECHADA;
	}

}
