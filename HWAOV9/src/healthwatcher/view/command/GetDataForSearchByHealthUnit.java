package healthwatcher.view.command;

import healthwatcher.Constants;
import healthwatcher.model.healthguide.HealthUnit;
import healthwatcher.view.IFacade;

import java.io.IOException;
import java.io.PrintWriter;

import lib.exceptions.ObjectNotFoundException;
import lib.patterns.CommandReceiver;
import lib.util.HTMLCode;
import lib.util.IteratorDsk;

public class GetDataForSearchByHealthUnit extends CommandServlet {

	public void executeCommand(CommandReceiver receiver) {
		PrintWriter out = null;

		try {
			out = response.getWriter();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		out.println(HTMLCode.open("Queries - Specialties"));
		out
				.println("<body><h1>Queries:<br>Search Specialties of a Health unit</h1>");
		out.println("<p>Choose a health unit: </p>");
		out.println("<form method=\"POST\"action=\""+Constants.SYSTEM_ACTION+"?operation=SearchSpecialtiesByHealthUnit\">");

		try {
			out
					.println("<div align=\"center\"><center><p><select name=\"codUnidadeSaude\" size=\"1\">");

			IteratorDsk repUS = ((IFacade) receiver).getPartialHealthUnitList();

			if (!repUS.hasNext()) {
				out.println("</select></p></center></div>");
				out.println("<P> There isn't registered health units.</P>");
			} else {
				HealthUnit us;
				do {
					us = (HealthUnit) repUS.next();
					out.println("<option value=\"" + us.getCode() + "\"> "
							+ us.getDescription() + " </OPTION>");
				} while (repUS.hasNext());

				repUS.close();
				out.println("</select></p></center></div>");
				out
						.println("<div align=\"center\"><center><p><input type=\"submit\" value=\"Consultar\" name=\"B1\"></p></center></div></form>");
			}
			out.println(HTMLCode.closeQueries());
		} catch (ObjectNotFoundException e) {
			out.println("</select></p></center></div>");
			out.println("<P> " + e.getMessage() + " </P>");
			out.println("<P> Nenhuma unidade de saude foi cadastrada</P>");
		} catch (Exception e) {
			out.println(lib.util.HTMLCode
					.errorPage("Comunitation error, please try again later."));
			e.printStackTrace(out);
		} finally {
			out.close();
		}
	}
}