package healthwatcher.view.command;

import healthwatcher.model.complaint.DiseaseType;
import healthwatcher.view.IFacade;

import java.io.IOException;
import java.io.PrintWriter;

import lib.exceptions.CommunicationException;
import lib.exceptions.InsertEntryException;
import lib.exceptions.InvalidSessionException;
import lib.exceptions.ObjectAlreadyInsertedException;
import lib.exceptions.ObjectNotValidException;
import lib.exceptions.RepositoryException;
import lib.exceptions.TransactionException;
import lib.util.HTMLCode;


public class InsertDiseaseType extends Command {

	public InsertDiseaseType(IFacade f) { 
		super(f);
	}

	public void execute() {

		PrintWriter out=null;
		DiseaseType diseaseType = null;
       
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

			String code = request.getInput("code");
			String name = request.getInput("name");
			String description = request.getInput("description");
			String manifestacao = request.getInput("manifestacao");
			String duration = request.getInput("duration");
			
			// TODO: FALTA INCLUIR OS SINTOMAS JUNTOS
			diseaseType = new DiseaseType(name, description, manifestacao, duration, null);
			diseaseType.setCode(Integer.parseInt(code));
			facade.insert(diseaseType);
        
            out.println(HTMLCode.htmlPageAdministrator("Operation executed", "DiseaseType inserted"));
        } catch (ObjectAlreadyInsertedException e) {
        	out.println(HTMLCode.errorPageAdministrator(e.getMessage()));
            e.printStackTrace(out);
        } catch (ObjectNotValidException e) {
        	out.println(HTMLCode.errorPageAdministrator(e.getMessage()));
            e.printStackTrace(out);
        } catch(InsertEntryException e){ 
        	out.println(HTMLCode.errorPageAdministrator(e.getMessage()));
            e.printStackTrace(out);
        }catch (InvalidSessionException e) {
        	out.println(HTMLCode.errorPageAdministrator(e.getMessage()));
            e.printStackTrace(out);
        }catch(TransactionException e){
        	out.println(HTMLCode.errorPageAdministrator(e.getMessage()));
            e.printStackTrace(out);            
        } catch(CommunicationException e){
			out.println(HTMLCode.errorPageAdministrator(e.getMessage()));
            e.printStackTrace(out);
        } catch(RepositoryException e){
			out.println(HTMLCode.errorPageAdministrator(e.getMessage()));
            e.printStackTrace(out); 
		} finally {
            out.close();
        }
	}

}
