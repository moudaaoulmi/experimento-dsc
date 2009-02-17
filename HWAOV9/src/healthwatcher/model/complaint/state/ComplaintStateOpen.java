package healthwatcher.model.complaint.state;

import healthwatcher.model.address.Address;
import healthwatcher.model.complaint.Situation;
import healthwatcher.model.employee.Employee;
import lib.util.Date;

public class ComplaintStateOpen extends ComplaintState {

	public ComplaintStateOpen() {
	}

	public ComplaintStateOpen(int code, String complainer, String description,
			String observation, String email, Employee attendant, int status,
			Date medicalOpinionDate, Date complaintDate,
			Address complainerAddress) {
		super(code, complainer, description, observation, email, attendant,
				status, medicalOpinionDate, complaintDate, complainerAddress);
	}

	
	public int getStatus() {
		return Situation.QUEIXA_ABERTA;
	}

	
	public void setAttendant(Employee attendant) {
		this.attendant = attendant;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setComplainer(String complainer) {
		this.complainer = complainer;
	}

	public void setComplainerAddress(Address complainerAddress) {
		this.complainerAddress = complainerAddress; 
	}	

	public void setComplaintDate(Date complaintDate) {
		this.complaintDate = complaintDate; 
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setMedicalOpinionDate(Date medicalOpinionDate) {
		this.medicalOpinionDate = medicalOpinionDate;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}
}
