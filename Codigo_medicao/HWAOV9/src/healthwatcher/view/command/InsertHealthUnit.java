package healthwatcher.view.command;

import healthwatcher.model.healthguide.HealthUnit;
import healthwatcher.view.IFacade;

import java.io.IOException;
import java.io.PrintWriter;

import lib.exceptions.InsertEntryException;
import lib.exceptions.InvalidSessionException;
import lib.exceptions.ObjectAlreadyInsertedException;
import lib.exceptions.ObjectNotValidException;
import lib.patterns.CommandReceiver;
import lib.util.HTMLCode;

public class InsertHealthUnit  extends CommandServlet {

	public void executeCommand(CommandReceiver receiver) {
		PrintWriter out=null;
		HealthUnit hu = null;
       
        try {
        	try {
    			out = response.getWriter();
    		} catch (IOException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
        
            if (! request.isAuthorized()) {
                throw new InvalidSessionException();
            }            

            //Complaint Normal
			String code = request.getInput("code");
			String description = request.getInput("description");			
			
			hu = new HealthUnit(description, null);
			hu.setCode(Integer.parseInt(code));
			
			((IFacade) receiver).insert(hu);
        
            out.println(HTMLCode.htmlPageAdministrator("Operation executed", "HealthUnit inserted"));
        } catch (ObjectAlreadyInsertedException e) {
        	out.println(HTMLCode.errorPageAdministrator(e.getMessage()));
            e.printStackTrace(out);
        } catch (ObjectNotValidException e) {
        	out.println(HTMLCode.errorPageAdministrator(e.getMessage()));
            e.printStackTrace(out);
        } catch(InsertEntryException e){ 
        	out.println(HTMLCode.errorPageAdministrator(e.getMessage()));
            e.printStackTrace(out);
        } catch (InvalidSessionException e) {
        	out.println(HTMLCode.errorPageAdministrator(e.getMessage()));
            e.printStackTrace(out);
        } catch (Exception e) {
        	e.printStackTrace();
		}finally {
            out.close();
        }
	}

}
