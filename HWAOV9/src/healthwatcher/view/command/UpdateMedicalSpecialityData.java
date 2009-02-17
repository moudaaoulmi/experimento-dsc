package healthwatcher.view.command;

import healthwatcher.model.healthguide.MedicalSpeciality;

import java.io.PrintWriter;

import lib.exceptions.InvalidSessionException;
import lib.patterns.CommandReceiver;
import lib.util.HTMLCode;

public class UpdateMedicalSpecialityData extends CommandServlet {

	public void executeCommand(CommandReceiver receiver) {

		PrintWriter out= null;
		try {
			out = response.getWriter();
        	MedicalSpeciality speciality;       
        
        	if (! request.isAuthorized()) {
                throw new InvalidSessionException();
            }        	
        	speciality = (MedicalSpeciality) request.get(UpdateMedicalSpecialitySearch.SPECIALITY);
            String descricao = request.getInput("descricao");            
            
            speciality.setDescricao(descricao);
        	
            out.println(HTMLCode.htmlPageAdministrator("Operation executed", "Medical Speciality updated"));

        }catch(Exception e){
            out.println(HTMLCode.errorPage("Communication error, please try again later."));
            e.printStackTrace(out);
        }finally {
            out.close();
        }
	}

}
