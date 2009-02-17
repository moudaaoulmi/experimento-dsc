
package healthwatcher.view.command;

import healthwatcher.model.healthguide.MedicalSpeciality;
import healthwatcher.view.IFacade;

import java.io.IOException;
import java.io.PrintWriter;

import lib.exceptions.ObjectNotFoundException;
import lib.patterns.CommandReceiver;
import lib.util.HTMLCode;
import lib.util.IteratorDsk;


public class SearchSpecialtiesByHealthUnit extends CommandServlet {

	public void executeCommand(CommandReceiver receiver) {
        PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        int codigoUS = Integer.parseInt(request.getInput("codUnidadeSaude"));

        try {

            IteratorDsk repEsp = ((IFacade) receiver).searchSpecialitiesByHealthUnit(codigoUS);
            
            out.println(HTMLCode.open("Queries - Especialties"));
            out.println("<body><h1>Querie result</h1>");
            
            out.println("<P><h3>Health unit: " + codigoUS+ " </h3></P>");
            out.println("<h3>Especialties :</h3>");

            while (repEsp.hasNext()) {
                MedicalSpeciality esp = (MedicalSpeciality) repEsp.next();
                out.println("<dd><dd>" + esp.getDescricao());
            
            }
            out.println(HTMLCode.closeQueries());            
        
        } catch (ObjectNotFoundException e) {
        	out.println(HTMLCode.errorPageQueries("This health unit does not have registered specialties."));
        }catch(Exception e){
            out.println(lib.util.HTMLCode.errorPage("Comunitation error, please try again later."));
            e.printStackTrace(out);
        } finally {
            out.close();
        }
	}
}