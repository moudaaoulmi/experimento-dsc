package healthwatcher.view.command;

import healthwatcher.Constants;
import healthwatcher.model.healthguide.HealthUnit;
import healthwatcher.view.IFacade;

import java.io.IOException;
import java.io.PrintWriter;

import lib.exceptions.InvalidSessionException;
import lib.exceptions.ObjectNotFoundException;
import lib.patterns.CommandReceiver;
import lib.util.HTMLCode;
import lib.util.IteratorDsk;

public class UpdateHealthUnitList extends CommandServlet {

	public void executeCommand(CommandReceiver receiver) {
			PrintWriter out=null;
			
	        try {
	        out = response.getWriter();

	        
	            if (! request.isAuthorized()) {
	                throw new InvalidSessionException("Invalid Session! <br><a href=\""+Constants.SYSTEM_LOGIN+"\">Try again</a>");
	            }
	        
		        out.println("Queries - Complaint information");
		        out.println("<body><h1>Queries:<br>Querie about complaint</h1>");
		        out.println("<p>Choose a complaint: </p>");
		        out.println("<form method=\"POST\" action=\""+Constants.SYSTEM_ACTION+"?operation=UpdateHealthUnitSearch\">");

	            out.println("<div align=\"center\"><center><p><select name=\"numUS\" size=\"1\">");

	           
	            IteratorDsk repTP = ((IFacade) receiver).getHealthUnitList();

	            if (repTP==null||!repTP.hasNext()) {
	                out.println("</select></p></center></div>");
	                out.println("<p><font color=\"red\"><b> There isn't any health units.</b></font></p>");
	            } else {
	                HealthUnit tp;
	                do {
	                    tp = (HealthUnit) repTP.next();
	                    out.println("<option value=\""      +
	                                tp.getCode() + "\"> " +
	                                tp.getDescription()           +
	                                " </OPTION>");
	                } while (repTP.hasNext());
	                repTP.close();
	                
	                out.println("</select></p></center></div>");
	                out.println("  <div align=\"center\"><center><p><input type=\"submit\" value=\"Search\" name=\"B1\"></p></center></div></form>");
	            }
	            out.println(HTMLCode.closeAdministrator());
	        } catch (ObjectNotFoundException e) {
	            out.println("There isn't any health units");
	        } catch (InvalidSessionException e) {
	            out.println(e.getMessage());			
			} catch (IOException e) {
				out.println(e.getMessage());
			}finally {
	            out.close();
	        }
		}

	}
