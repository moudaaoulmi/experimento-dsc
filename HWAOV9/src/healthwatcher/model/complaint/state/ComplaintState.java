package healthwatcher.model.complaint.state;

import healthwatcher.model.address.Address;
import healthwatcher.model.complaint.Complaint;
import healthwatcher.model.employee.Employee;
import lib.util.Date;

public abstract class ComplaintState {
	
	protected int code;
	protected String complainer;
	protected String description;
	protected String observation;
	protected String email;
	protected Employee attendant;
	protected int status;
	protected Date medicalOpinionDate;
	protected Date complaintDate;
	protected Address complainerAddress;  


	public ComplaintState() {
	}

	public ComplaintState(int code, String complainer, String description,
			String observation, String email, Employee attendant, int status,
			Date medicalOpinionDate, Date complaintDate,
			Address complainerAddress) {
		this.code = code;
		this.complainer = complainer;
		this.description = description;
		this.observation = observation;
		this.email = email;
		this.attendant = attendant;
		this.status = status;
		this.medicalOpinionDate = medicalOpinionDate;
		this.complaintDate = complaintDate;
		this.complainerAddress = complainerAddress;
	}
	
	public abstract void setAttendant(Employee attendant);
	public abstract void setCode(int code);
	public abstract void setComplainer(String complainer);
	public abstract void setComplainerAddress(Address complainerAddress);
	public abstract void setComplaintDate(Date complaintDate);
	public abstract void setDescription(String description);
	public abstract void setEmail(String email);
	public abstract void setMedicalOpinionDate(Date medialOpinionDate);
	public abstract void setObservation(String observation);
	public abstract int getStatus();
	
	public Employee getAttendant() {
		return attendant;
	}

	public int getCode() {
		return code;
	}

	public String getComplainer() {
		return complainer;
	}

	public Address getComplainerAddress() {
		return complainerAddress;
	}

	public Date getComplaintDate() {
		return complaintDate;
	}

	public String getDescription() {
		return description;
	}

	public String getEmail() {
		return email;
	}

	public Date getMedicalOpinionDate() {
		return medicalOpinionDate;
	}

	public String getObservation() {
		return observation;
	}

	public void setStatus(int sit, Complaint complaint) {
	}
}
