package healthwatcher.view.command;

import healthwatcher.model.complaint.Symptom;

import java.io.PrintWriter;

import lib.exceptions.InvalidSessionException;
import lib.patterns.CommandReceiver;
import lib.util.HTMLCode;


public class UpdateSymptomData extends CommandServlet {

	public void executeCommand(CommandReceiver receiver) {

		PrintWriter out= null;
		try {
			out = response.getWriter();
        	Symptom symptom;       
        
        	if (! request.isAuthorized()) {
                throw new InvalidSessionException();
            }        	
        	symptom = (Symptom) request.get(UpdateSymptomSearch.SYMPTOM);
            String descricao = request.getInput("descricao");            
            
            symptom.setDescription(descricao);
        	
            out.println(HTMLCode.htmlPageAdministrator("Operation executed", "Symptom updated"));

        }catch(Exception e){
            out.println(HTMLCode.errorPage("Comunitation error, please try again later."));
            e.printStackTrace(out);
        }finally {
            out.close();
        }
	}

}
