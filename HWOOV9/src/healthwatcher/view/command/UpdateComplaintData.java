package healthwatcher.view.command;

import healthwatcher.model.complaint.Complaint;
import healthwatcher.model.complaint.Situation;
import healthwatcher.model.employee.Employee;
import healthwatcher.view.IFacade;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import lib.exceptions.InvalidSessionException;
import lib.util.HTMLCode;

public class UpdateComplaintData extends Command {

	public UpdateComplaintData(IFacade f) {
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

		String obsQueixa = request.getInput("obsQueixa");
		Complaint q = null;

		try {
			if (! request.isAuthorized()) {
				throw new InvalidSessionException();
			}

			q = (Complaint) request
					.get(UpdateComplaintSearch.QUEIXA);
			
			q.setObservacao(obsQueixa);
			Calendar agora = Calendar.getInstance();
			q.setDataParecer(new lib.util.Date(
					agora.get(Calendar.DAY_OF_MONTH),
					agora.get(Calendar.MONTH), agora.get(Calendar.YEAR)));
			Employee employee = (Employee) request
					.get(Login.EMPLOYEE);
			q.setAtendente(employee);
			q.setSituacao(Situation.QUEIXA_FECHADA);

			out.println(HTMLCode.htmlPageAdministrator("Operation executed",
					"Complaint updated" + "<P>" + obsQueixa + "</P>"));
		} catch (Exception e) {
			out.println(lib.util.HTMLCode
					.errorPageAdministrator(e.getMessage()));
			e.printStackTrace(out);
		} finally {
			out.close();
		}
	}
}
