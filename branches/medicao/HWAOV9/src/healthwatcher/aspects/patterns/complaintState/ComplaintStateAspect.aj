package healthwatcher.aspects.patterns.complaintState;

/**
 * Implements the state transitions for this state design pattern example. 
 * State transitions are realizied as <code>after</code> advice. The 
 * joinpoints are the calls from the context to its state object.<p>
 *
 * Exisiting states are reused without a employing a flyweight mechanism or 
 * (inflexibly) modularizing the transitions in the context.
 *
 * @author  Marcos Dósea
 * @author  Sérgio Soares
 * 
 */

import healthwatcher.model.address.Address;
import healthwatcher.model.complaint.Complaint;
import healthwatcher.model.complaint.Situation;
import healthwatcher.model.complaint.state.ComplaintState;
import healthwatcher.model.complaint.state.ComplaintStateClosed;
import healthwatcher.model.complaint.state.ComplaintStateOpen;
import healthwatcher.model.employee.Employee;
import lib.util.Date;



public aspect ComplaintStateAspect {
    
    after(Complaint complaint): initialization(Complaint+.new()) && target(complaint) {
		complaint.setComplaintState( new ComplaintStateOpen() );
	}
	
	after(Complaint complaint, String complainer, String description, String observation,
		  String email, Employee attendant, int status, Date medicalOpinionDate, 
		  Date complaintDate, Address complainerAddress) :     	
			  initialization(Complaint+.new(..)) && target(complaint) && 
			  args(complainer, description, observation, email, attendant, status,
				   medicalOpinionDate, complaintDate, complainerAddress) {
		
		 if(status==Situation.QUEIXA_ABERTA) {
		        complaint.setComplaintState( new ComplaintStateOpen(0, complainer, description, observation, email, attendant,
		        		status, medicalOpinionDate, complaintDate, complainerAddress));
		 } else if(status==Situation.QUEIXA_FECHADA) {
			 complaint.setComplaintState( new ComplaintStateClosed(0, complainer, description, observation, email, attendant,
					 status, medicalOpinionDate, complaintDate, complainerAddress));
		 }
	}
	
	after(int status, Complaint complaint, ComplaintState state) : 
			execution(void ComplaintState+.setStatus(int, Complaint)) &&
			args(status, complaint) && target(state) {
		
		if (status != state.getStatus()) {
			if (status == Situation.QUEIXA_ABERTA) {
				complaint.setComplaintState(new ComplaintStateOpen(state.getCode(),
						state.getComplainer(), state.getDescription(), state.getObservation(), 
						state.getEmail(), state.getAttendant(),	state.getStatus(), state.getMedicalOpinionDate(), 
						state.getComplaintDate(), state.getComplainerAddress()));
			} else if (status == Situation.QUEIXA_FECHADA) {
				complaint.setComplaintState(new ComplaintStateClosed(state.getCode(),
						state.getComplainer(), state.getDescription(), state.getObservation(), 
						state.getEmail(), state.getAttendant(),	state.getStatus(), state.getMedicalOpinionDate(), 
						state.getComplaintDate(), state.getComplainerAddress()));
			} else if (status == Situation.QUEIXA_SUSPENSA) {

			}
		}
	}

}