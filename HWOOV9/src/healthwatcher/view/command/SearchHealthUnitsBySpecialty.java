package healthwatcher.view.command;


import healthwatcher.model.healthguide.HealthUnit;
import healthwatcher.view.IFacade;

import java.io.IOException;
import java.io.PrintWriter;

import lib.exceptions.CommunicationException;
import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.RepositoryException;
import lib.exceptions.TransactionException;
import lib.util.HTMLCode;
import lib.util.IteratorDsk;



public class SearchHealthUnitsBySpecialty extends Command {

    public SearchHealthUnitsBySpecialty(IFacade f) {
		super(f);
		// TODO Auto-generated constructor stub
	}

	public void execute() {
        PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        int codigoEsp =  Integer.parseInt(request.getInput("codEspecialidade"));

        try {
        	IteratorDsk repUS = facade.searchHealthUnitsBySpeciality(codigoEsp);

        	out.println(HTMLCode.open("Queries - Health Unit"));
            out.println("<body><h1>Querie result<br>Health units</h1>");
            
            out.println("<P><h3>Medical specialty: " + codigoEsp + "</h3></P>");
            out.println("<h3>Health units:</h3>");

            while (repUS.hasNext()) {
                HealthUnit us = (HealthUnit) repUS.next();
                out.println("<dd><dd>" + us.getDescription());             
            }
            out.println(HTMLCode.closeQueries());

        }catch (ObjectNotFoundException e) {
            out.println("<P> " + e.getMessage() + " </P>");
        } catch (RepositoryException e) {
            out.println("<P> " + e.getMessage() + " </P>");
        } catch (TransactionException e) {
            out.println("<P> " + e.getMessage() + " </P>");
        } catch (CommunicationException e) {
            out.println("<P> " + e.getMessage() + " </P>");            
        } catch(Exception e){
            out.println(lib.util.HTMLCode.errorPage("Comunitation error, please try again later."));
            e.printStackTrace(out);
        }finally {
            out.close();
        }
	}
}