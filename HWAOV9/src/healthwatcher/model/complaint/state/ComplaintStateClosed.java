package healthwatcher.model.complaint.state;

import healthwatcher.model.address.Address;
import healthwatcher.model.complaint.Situation;
import healthwatcher.model.employee.Employee;
import lib.util.Date;

public class ComplaintStateClosed extends ComplaintState  {
	
	public ComplaintStateClosed() {
	}

	public ComplaintStateClosed(int code, String complainer, String description, String observation, String email, Employee attendant, int status, Date medialOpinionDate, Date complaintDate, Address complainerAddress) {
		super(code, complainer, description, observation, email, attendant, status,
				medialOpinionDate, complaintDate, complainerAddress);
	}

	public void setAttendant(Employee attendant) {
	}

	public void setCode(int code) {
	}

	public void setComplainer(String complainer) {
	}

	public void setComplainerAddress(Address complainerAddress) {
	}

	public void setComplaintDate(Date complaintDate) {
	}

	public void setDescription(String description) {
	}

	public void setEmail(String email) {
	}

	public void setMedicalOpinionDate(Date medialOpinionDate) {
	}

	public void setObservation(String observation) {
	}

	public int getStatus() {
		return Situation.QUEIXA_FECHADA;
	}
}
