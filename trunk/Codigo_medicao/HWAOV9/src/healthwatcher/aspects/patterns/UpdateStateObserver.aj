package healthwatcher.aspects.patterns;

import healthwatcher.business.HealthWatcherFacade;
import healthwatcher.model.complaint.Complaint;
import healthwatcher.model.complaint.Symptom;
import healthwatcher.model.employee.Employee;
import healthwatcher.model.healthguide.HealthUnit;
import healthwatcher.model.healthguide.MedicalSpeciality;
import healthwatcher.view.IFacade;
import healthwatcher.view.command.CommandRequest;
import healthwatcher.view.command.CommandServlet;
import lib.patterns.ObserverProtocol;


/**
 * Implements the observer protocol to update automatically to the DB changes made in model
 * objects. This aspect also is responsible by adding the observers when the subject objects
 * are created.
 */
public aspect UpdateStateObserver extends ObserverProtocol {
	
	declare parents: IFacade implements Observer;

	declare parents: Complaint ||
	 				 Employee ||  
	 				 Symptom ||
	 				 MedicalSpeciality ||
	 				 HealthUnit implements Subject;
	
	after(Subject s) : call(* CommandRequest+.put(String, Object)) && this(CommandServlet) &&	args(.., s) {
		addObserver(s, HealthWatcherFacade.getInstance());
	}
	
	protected pointcut subjectChange(Subject subject): 
		this(CommandServlet+) && call(* Subject+.set*(..)) && target(subject);

	protected void updateObserver(Subject subject, Observer observer) {
		
		if(observer instanceof IFacade) {
			IFacade facade= (IFacade) observer;
			
			if(subject instanceof Employee){
				facade.updateEmployee((Employee) subject);
			} else if(subject instanceof Complaint){
				facade.updateComplaint((Complaint) subject);
			} else if(subject instanceof HealthUnit){
				facade.updateHealthUnit((HealthUnit) subject);
			} else if(subject instanceof Symptom){
				facade.updateSymptom((Symptom) subject);
			} else if(subject instanceof MedicalSpeciality){
				facade.updateMedicalSpeciality((MedicalSpeciality) subject);
			}
		}
	}

}
