package healthwatcher.view.command;

import healthwatcher.Constants;
import healthwatcher.model.healthguide.MedicalSpeciality;
import healthwatcher.view.IFacade;

import java.io.IOException;
import java.io.PrintWriter;

import lib.exceptions.CommunicationException;
import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.RepositoryException;
import lib.exceptions.TransactionException;
import lib.util.HTMLCode;
import lib.util.IteratorDsk;

public class GetDataForSearchBySpeciality extends Command {

	public GetDataForSearchBySpeciality(IFacade f) {
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

		out.println(HTMLCode.open("Queries - Health Units"));
		out
				.println("<body><h1>Queries:<br>Search Health units by Medical specialty</h1>");
		out.println("<p>Choose a specialty: </p>");
		out.println("<form method=\"POST\"action=\""+Constants.SYSTEM_ACTION+"?operation=SearchHealthUnitsBySpecialty\">");

		try {
			out
					.println("<div align=\"center\"><center><p><select name=\"codEspecialidade\" size=\"1\">");

			IteratorDsk repEsp = facade.getSpecialityList();

			if (!repEsp.hasNext()) {
				out.println("</select></p></center></div>");
				out.println("<P>There isn't registered specialties.</P>");
			} else {
				MedicalSpeciality esp;

				do {
					esp = (MedicalSpeciality) repEsp.next();
					out.println("<option value=\"" + esp.getCodigo() + "\"> "
							+ esp.getDescricao() + " </OPTION>");
				} while (repEsp.hasNext());

				repEsp.close();
				out.println("</select></p></center></div>");
				out
						.println(" <div align=\"center\"><center><p><input type=\"submit\" value=\"Consultar\" name=\"B1\"></p></center></div></form>");
			}
			out.println(HTMLCode.closeQueries());
		
		} catch (ObjectNotFoundException e) {
			out.println("</select></p></center></div>");
			out.println("<P> " + e.getMessage() + " </P>");
			out.println("<P> Nenhuma especialidade foi cadastrada</P>");
		} catch (RepositoryException e) {
			out.println("</select></p></center></div>");
			out.println("<P> " + e.getMessage() + " </P>");
		} catch (TransactionException e) {
			out.println("</select></p></center></div>");
			out.println("<P> " + e.getMessage() + " </P>");
		} catch (CommunicationException e) {
			out.println("<P> " + e.getMessage() + " </P>");
		} catch (Exception e) {
			out.println("Error!");
			out.println(lib.util.HTMLCode
					.errorPage("Comunitation error, please try again later."));
			e.printStackTrace(out);
		} finally {
			out.close();
		}
	}
}
