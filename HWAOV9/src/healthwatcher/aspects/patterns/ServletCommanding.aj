package healthwatcher.aspects.patterns;

import healthwatcher.business.HealthWatcherFacade;
import healthwatcher.view.IFacade;
import healthwatcher.view.command.CommandServlet;
import healthwatcher.view.command.ConfigRMI;
import healthwatcher.view.command.GetDataForSearchByDiseaseType;
import healthwatcher.view.command.GetDataForSearchByHealthUnit;
import healthwatcher.view.command.GetDataForSearchBySpeciality;
import healthwatcher.view.command.InsertAnimalComplaint;
import healthwatcher.view.command.InsertDiseaseType;
import healthwatcher.view.command.InsertEmployee;
import healthwatcher.view.command.InsertFoodComplaint;
import healthwatcher.view.command.InsertHealthUnit;
import healthwatcher.view.command.InsertMedicalSpeciality;
import healthwatcher.view.command.InsertSpecialComplaint;
import healthwatcher.view.command.InsertSymptom;
import healthwatcher.view.command.Login;
import healthwatcher.view.command.LoginMenu;
import healthwatcher.view.command.SearchComplaintData;
import healthwatcher.view.command.SearchDiseaseData;
import healthwatcher.view.command.SearchHealthUnitsBySpecialty;
import healthwatcher.view.command.SearchSpecialtiesByHealthUnit;
import healthwatcher.view.command.UpdateComplaintData;
import healthwatcher.view.command.UpdateComplaintList;
import healthwatcher.view.command.UpdateComplaintSearch;
import healthwatcher.view.command.UpdateEmployeeData;
import healthwatcher.view.command.UpdateEmployeeSearch;
import healthwatcher.view.command.UpdateHealthUnitData;
import healthwatcher.view.command.UpdateHealthUnitList;
import healthwatcher.view.command.UpdateHealthUnitSearch;
import healthwatcher.view.command.UpdateMedicalSpecialityData;
import healthwatcher.view.command.UpdateMedicalSpecialityList;
import healthwatcher.view.command.UpdateMedicalSpecialitySearch;
import healthwatcher.view.command.UpdateSymptomData;
import healthwatcher.view.command.UpdateSymptomList;
import healthwatcher.view.command.UpdateSymptomSearch;
import healthwatcher.view.servlets.HWServlet;
import healthwatcher.view.servlets.ServletRequestAdapter;
import healthwatcher.view.servlets.ServletResponseAdapter;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lib.patterns.Command;
import lib.patterns.CommandInvoker;
import lib.patterns.CommandInvokerToken;
import lib.patterns.CommandProtocol;
import lib.patterns.CommandReceiver;

/**
 * Sets up the Command pattern.
 * 
 * @author Marcos Dósea
 * @author Sérgio Soares
 * @version 04/09/2006
 * 
 */

public aspect ServletCommanding extends CommandProtocol {

	private Hashtable commandTable;
	
	declare parents: IFacade implements CommandReceiver;
	
	private static final String CommandConfigRMI		             = "ConfigRMI";
	private static final String CommandGetDataForSearchByDiseaseType = "SearchByDiseaseType";
	private static final String CommandGetDataForSearchByHealthUnit  = "SearchByHealthUnit";
	private static final String CommandGetDataForSearchBySpeciality  = "SearchBySpecialty";
	private static final String CommandInsertAnimalComplaint         = "InsertAnimalComplaint";
	private static final String CommandInsertEmployee                = "InsertEmployee";
	private static final String CommandInsertFoodComplaint           = "InsertFoodComplaint";
	private static final String CommandInsertSpecialComplaint        = "InsertSpecialComplaint";
	private static final String CommandLogin                         = "Login";
	private static final String CommandLoginMenu                     = "LoginMenu";
	private static final String CommandSearchComplaintData           = "SearchComplaintData";
	private static final String CommandSearchDiseaseData             = "SearchDiseaseData";
	private static final String CommandSearchHealthUnitsBySpecialty  = "SearchHealthUnitsBySpecialty";
	private static final String CommandSearchSpecialtiesByHealthUnit = "SearchSpecialtiesByHealthUnit";
	private static final String CommandUpdateComplaintData           = "UpdateComplaintData";
	private static final String CommandUpdateComplaintList           = "UpdateComplaintList";
	private static final String CommandUpdateComplaintSearch         = "UpdateComplaintSearch";
	private static final String CommandUpdateEmployeeData            = "UpdateEmployeeData";
	private static final String CommandUpdateEmployeeSearch          = "UpdateEmployeeSearch";
	private static final String CommandUpdateHealthUnitData          = "UpdateHealthUnitData";
	private static final String CommandUpdateHealthUnitSearch        = "UpdateHealthUnitSearch";
	private static final String CommandUpdateHealthUnitList          = "UpdateHealthUnitList";
	
	private static final String CommandInsertSpeciality              = "InsertMedicalSpeciality";
	private static final String CommandInsertSymptom                 = "InsertSymptom";
	private static final String CommandInsertHealthUnit              = "InsertHealthUnit";
	private static final String CommandInsertDiseaseType             = "InsertDiseaseType";
	private static final String CommandUpdateSymptomData          	 = "UpdateSymptomData";
	private static final String CommandUpdateSymptomSearch        	 = "UpdateSymptomSearch";
	private static final String CommandUpdateSymptomList          	 = "UpdateSymptomList";
	private static final String CommandUpdateMedicalSpecialityData   = "UpdateMedicalSpecialityData";
	private static final String CommandUpdateMedicalSpecialitySearch = "UpdateMedicalSpecialitySearch";
	private static final String CommandUpdateMedicalSpecialityList   = "UpdateMedicalSpecialityList";

	after() : execution(void HWServlet.init(..)) {
		commandTable = new Hashtable();
    	registerCommand(CommandConfigRMI, new ConfigRMI());
    	registerCommand(CommandGetDataForSearchByDiseaseType, new GetDataForSearchByDiseaseType());
    	registerCommand(CommandGetDataForSearchByHealthUnit, new GetDataForSearchByHealthUnit());
   	 	registerCommand(CommandGetDataForSearchBySpeciality, new GetDataForSearchBySpeciality());
    	registerCommand(CommandInsertAnimalComplaint, new InsertAnimalComplaint());
    	registerCommand(CommandInsertEmployee, new InsertEmployee());
    	registerCommand(CommandInsertFoodComplaint, new InsertFoodComplaint());
    	registerCommand(CommandInsertSpecialComplaint, new InsertSpecialComplaint());
    	registerCommand(CommandLogin, new Login());
    	registerCommand(CommandLoginMenu, new LoginMenu());
    	registerCommand(CommandSearchComplaintData, new SearchComplaintData());
    	registerCommand(CommandSearchDiseaseData, new SearchDiseaseData());
    	registerCommand(CommandSearchHealthUnitsBySpecialty, new SearchHealthUnitsBySpecialty());
    	registerCommand(CommandSearchSpecialtiesByHealthUnit, new SearchSpecialtiesByHealthUnit());
    	registerCommand(CommandUpdateComplaintData, new UpdateComplaintData());
    	registerCommand(CommandUpdateComplaintList, new UpdateComplaintList());
    	registerCommand(CommandUpdateComplaintSearch, new UpdateComplaintSearch());
    	registerCommand(CommandUpdateEmployeeData, new UpdateEmployeeData());
    	registerCommand(CommandUpdateEmployeeSearch, new UpdateEmployeeSearch());
    	registerCommand(CommandUpdateHealthUnitData, new UpdateHealthUnitData());
    	registerCommand(CommandUpdateHealthUnitSearch, new UpdateHealthUnitSearch());
    	registerCommand(CommandUpdateHealthUnitList, new UpdateHealthUnitList());
    	
    	registerCommand(CommandInsertSpeciality, new InsertMedicalSpeciality());
    	registerCommand(CommandInsertSymptom, new InsertSymptom());
    	registerCommand(CommandInsertHealthUnit, new InsertHealthUnit());
    	registerCommand(CommandInsertDiseaseType, new InsertDiseaseType());
    	registerCommand(CommandUpdateSymptomData, new UpdateSymptomData());
    	registerCommand(CommandUpdateSymptomSearch, new UpdateSymptomSearch());
    	registerCommand(CommandUpdateSymptomList, new UpdateSymptomList());
    	registerCommand(CommandUpdateMedicalSpecialityData, new UpdateMedicalSpecialityData());
    	registerCommand(CommandUpdateMedicalSpecialitySearch, new UpdateMedicalSpecialitySearch());
    	registerCommand(CommandUpdateMedicalSpecialityList, new UpdateMedicalSpecialityList());
    }
	
	/**
	 * Register this command with this key in the command table and uses it
	 * as a key to the health watcher facade in the protocol's table
	 */
	protected void registerCommand(String key, Command command) {
		commandTable.put(key, command);
    	setReceiver(command, HealthWatcherFacade.getInstance());
	}

	/**
	 * The join points after which to execute the command. This replaces the
	 * normally scattered myCommand.execute() calls. In this example, a call to
	 * <code>Button.clicked()</code> triggers the execution of the command.
	 * 
	 * @param invoker
	 *            the object invoking the command
	 */
	protected pointcut commandTrigger(CommandInvoker invoker) : 
		call (* CommandProtocol.setCommand(CommandInvoker, Command)) &&
		within (ServletCommanding) && 
		args(invoker, ..);

	/**
	 * Before the execution of the servlet, create an invoker object for the operation and 
	 * register the mappings in the protocol. This is needed, because HK implementation only
	 * support a 1-1 mapping between invoker and command. Since we have only the servlet as
	 * an invoker, we need to create this objects to fake invokers.
	 * It is not needed to unregister, because the mapping uses a weak hash map which releases
	 * mappings when references are gone. 
	 */
	before(HttpServletRequest request, HttpServletResponse response) : 
		(execution(void HWServlet.doPost(HttpServletRequest, HttpServletResponse)) ||
		 execution(void HWServlet.doGet(HttpServletRequest, HttpServletResponse))) && 
		 args(request, response) && 
		 within(HWServlet) {
		
		String operation = (String) request.getParameter("operation");
		CommandServlet command = (CommandServlet) commandTable.get(operation);
		command.setRequest(new ServletRequestAdapter(request));
    	command.setReponse(new ServletResponseAdapter(response));
		setCommand(new CommandInvokerToken(), command);
	}
}
