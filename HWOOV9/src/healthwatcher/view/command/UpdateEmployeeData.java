package healthwatcher.view.command;

import healthwatcher.model.employee.Employee;
import healthwatcher.view.IFacade;

import java.io.IOException;
import java.io.PrintWriter;

import lib.exceptions.InvalidSessionException;
import lib.util.HTMLCode;

public class UpdateEmployeeData extends Command {

	public UpdateEmployeeData(IFacade f) {
		super(f);
		// TODO Auto-generated constructor stub
	}

	public void execute() {
		PrintWriter out = null;

		String name = request.getInput("name");
		// String password = request.getParameter("password");
		String newPassword = request.getInput("newPassword");
		Employee employee = null;

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

			employee = (Employee) request.get(Login.EMPLOYEE);

			employee.setName(name);
			if (!newPassword.equals("")) {
				employee.setPassword(newPassword);
			}

			out.println(HTMLCode.htmlPageAdministrator("Operation executed",
					"Employee updated"));

		} catch (Exception e) {
			out.println(lib.util.HTMLCode.errorPage(e.getMessage()));
			e.printStackTrace(out);
		} finally {
			out.close();
		}
	}
}
