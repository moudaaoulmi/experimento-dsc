package healthwatcher.view.command;

import healthwatcher.Constants;
import healthwatcher.model.complaint.Symptom;
import healthwatcher.view.IFacade;

import java.io.IOException;
import java.io.PrintWriter;

import lib.exceptions.InvalidSessionException;
import lib.exceptions.ObjectNotFoundException;
import lib.patterns.CommandReceiver;
import lib.util.HTMLCode;


public class UpdateSymptomSearch extends CommandServlet {
	
	public static final String SYMPTOM = "symptom";

	public void executeCommand(CommandReceiver receiver) {

		PrintWriter out=null;        

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
        
            out.println("Update Symptom");
            out.println("<script language=\"javascript\">");
            out.println("function submeterDados(modulo)");
            out.println("{");
    
            String a1 = "\"";
            String a2 = "\"";
    
            out.println("   var f = document.formAlterarUnidade2;");
            out.println("   if(f.descricao.value ==" + a1 + a2 + ")");
            out.println("   {");
            out.println("           alert(\"Digite a nova descrição do sintoma!\");");
            out.println("           f.descricao.select();");
            out.println("           return;");
            out.println("   }");
            out.println("   f.operation.value = \"UpdateSymptomData\";");
            out.println("   f.submit();");
            out.println("}");
            out.println("//--></script>");
            out.println("<body><h1>Update Symptom:</h1>");
    
            int symptomCode = (new Integer(request.getInput("symptomCode"))).intValue();
            
            Symptom symptom = ((IFacade) receiver).searchSymptom(symptomCode);
            
            request.put(SYMPTOM, symptom);
            
            out.println("<form method=\"POST\" name=\"formAlterarUnidade2\" action=\"http://"+Constants.SERVLET_SERVER_PATH+"HWServlet\">");
            out.println("<input type=\"hidden\" name=\"operation\" value=\"UpdateSymptomData\">" );
            out.println("<div align=\"center\"><center><h4>Symptom: " + symptomCode + "</h4></center></div>");                      
            out.println("<div align=\"center\"><center><p><strong>Name:</strong><br><input type=\"text\" name=\"descricao\" value=\"" + symptom.getDescription() + "\" size=\"60\"></p></center></div>");
            out.println("<div align=\"center\"><center><h4><input type=\"button\" value=\"Update\" name=\"bt1\" onClick=\"javascript:submeterDados();\"><input type=\"reset\" value=\"Clear\" name=\"bt2\"></h4></center></div></form>");
            
            out.println(HTMLCode.closeAdministrator());                                
        } catch (ObjectNotFoundException e) {
            out.println("Symptom does not exist!");
        } catch (InvalidSessionException e) {
        	out.println(e.getMessage());
		} finally {
            out.close();
        }
	}

}
