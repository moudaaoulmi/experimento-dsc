package healthwatcher.view.command;

import healthwatcher.model.employee.Employee;
import healthwatcher.view.IFacade;

import java.io.IOException;
import java.io.PrintWriter;

import lib.exceptions.InvalidSessionException;
import lib.exceptions.ObjectAlreadyInsertedException;
import lib.exceptions.ObjectNotValidException;
import lib.patterns.CommandReceiver;
import lib.util.HTMLCode;

public class InsertEmployee extends CommandServlet {

	public void executeCommand(CommandReceiver receiver) {
		PrintWriter out = null;
		Employee employee;

		try {
			out = response.getWriter();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			if (! request.isAuthorized()) {
				throw new InvalidSessionException();
			}

			// Complaint Normal
			String name = request.getInput("name");
			String login = request.getInput("login");
			String password = request.getInput("password");

			employee = new Employee(login, password, name);

			((IFacade) receiver).insert(employee);

			out.println(HTMLCode.htmlPageAdministrator("Operation executed",
					"Employee inserted"));
		} catch (ObjectAlreadyInsertedException e) {
			out.println(lib.util.HTMLCode
					.errorPageAdministrator(e.getMessage()));
			e.printStackTrace(out);
		} catch (ObjectNotValidException e) {
			out.println(lib.util.HTMLCode
					.errorPageAdministrator(e.getMessage()));
			e.printStackTrace(out);
		} catch (InvalidSessionException e) {
			out.println(lib.util.HTMLCode
					.errorPageAdministrator(e.getMessage()));
			e.printStackTrace(out);
		} finally {
			out.close();
		}
	}
}