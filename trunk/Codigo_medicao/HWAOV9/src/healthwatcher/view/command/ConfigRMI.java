package healthwatcher.view.command;


import java.io.IOException;
import java.io.PrintWriter;

import lib.patterns.CommandReceiver;
import lib.util.HTMLCode;


public class ConfigRMI extends CommandServlet {

	public void executeCommand(CommandReceiver receiver) {
		PrintWriter out = null;

		try {
			out = response.getWriter();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {

			out.println(HTMLCode.htmlPage("Health-Watcher 2003", "Server name stored"));
		} catch (Exception e) {
			out.println(HTMLCode.errorPageAdministrator(e.getMessage()));
		} finally {
			out.close();
		}
	}
}