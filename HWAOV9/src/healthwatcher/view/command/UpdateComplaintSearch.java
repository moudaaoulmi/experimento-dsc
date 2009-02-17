package healthwatcher.view.command;



import healthwatcher.Constants;
import healthwatcher.model.complaint.AnimalComplaint;
import healthwatcher.model.complaint.Complaint;
import healthwatcher.model.complaint.FoodComplaint;
import healthwatcher.model.complaint.Situation;
import healthwatcher.model.complaint.SpecialComplaint;
import healthwatcher.view.IFacade;

import java.io.IOException;
import java.io.PrintWriter;

import lib.exceptions.InvalidSessionException;
import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.SituationFacadeException;
import lib.patterns.CommandReceiver;
import lib.util.HTMLCode;

public class UpdateComplaintSearch extends CommandServlet {

	public static final String QUEIXA = "queixa";

	public void executeCommand(CommandReceiver receiver) {
		PrintWriter out= null;
		try {
		out = response.getWriter();

        	if (! request.isAuthorized()) {
                throw new InvalidSessionException();
            }  
        	
            int numQueixa = (new Integer(request.getInput("numQueixa"))).intValue();
            Complaint q = ((IFacade) receiver).searchComplaint(numQueixa);
            request.put(UpdateComplaintSearch.QUEIXA, q);
            
            if (q.getSituacao() != Situation.QUEIXA_ABERTA) {
                throw new SituationFacadeException("Complaint not open");
            }

            out.println(HTMLCode.open("Update complaint"));
            out.println("<script language=\"javascript\">");
            out.println("function submeterDados(modulo)");
            out.println("{");
    
            String a1 = "\"";
            String a2 = "\"";
    
            out.println("   var f = document.formAlterarQueixa2;");
            out.println("   if(f.obsQueixa.value ==" + a1 + a2 + ")");
            out.println("   {");
            out.println("           alert(\"Digite o parecer da queixa!\");");
            out.println("           f.obsQueixa.select();");
            out.println("           return;");
            out.println("   }");
            out.println("   f.submit();");
            out.println("}");
            out.println("//--></script>");
            out.println("<body><h1>Update Complaint:</h1>");
    
            out.println("<form method=\"POST\" name=\"formAlterarQueixa2\" action=\""+Constants.SYSTEM_ACTION+"?operation=UpdateComplaintData\">");
            out.println("<div align=\"center\"><center><h4>Complaint : " + numQueixa+ "</h4></center></div>");

            String t = null;
			if (q instanceof SpecialComplaint) {
				t = "Special complaint";
            } else if (q instanceof FoodComplaint) {
            	t = "Food complaint";
            } else if (q instanceof AnimalComplaint) {
            	t = "Animal complaint";
            }

			out.println("<div align=\"center\"><center><p><strong>Complaint kind: "+t+"</strong></p></center></div>");
            out.println("<div align=\"center\"><center><p><strong>Description: "+ q.getDescricao() + "</strong></p></center></div>");
            out.println("<div align=\"center\"><center><p><strong>Observation (complaint's solution):</strong><br><textarea rows=\"5\" name=\"obsQueixa\" cols=\"22\"></textarea></p></center></div>");
            out.println("<div align=\"center\"><center><h4><input type=\"button\" value=\"Update\" name=\"bt1\" onClick=\"javascript:submeterDados();\"><input type=\"reset\" value=\"Clear\" name=\"bt2\"></h4></center></div></form>");
            out.println(HTMLCode.closeAdministrator());
        } catch (InvalidSessionException e) {
        	out.println(HTMLCode.errorPageAdministrator("<p>Ivalid Session! <br>You must <a href=\""+Constants.SYSTEM_LOGIN+"\">login</a> again!"));
		} catch (ObjectNotFoundException e) {
			out.println(HTMLCode.errorPageAdministrator("Complaint does not exist!"));
		} catch (SituationFacadeException e) {
			out.println(HTMLCode.errorPageAdministrator("This complaint's status is closed!"));
		} catch(IOException e){
			out.println(HTMLCode.errorPage(e.getMessage()));
		}finally {
            out.close();
        }
	}

}