package healthwatcher.view.command;

import healthwatcher.Constants;
import healthwatcher.view.IFacade;

import java.io.PrintWriter;

import lib.exceptions.InvalidSessionException;
import lib.util.HTMLCode;
import lib.util.Library;

public class LoginMenu extends Command {

	public LoginMenu(IFacade f) {
		super(f);
		// TODO Auto-generated constructor stub
	}

	private String[] keywords = { "##SYSTEM_ROOT##", "##SERVLET_SERVER_PATH##",
			"##CLOSE##", "##SYSTEM_ACTION##" };
             
	private String[] newWords = { Constants.SYSTEM_ROOT,
			Constants.SERVLET_SERVER_PATH, HTMLCode.closeAdministrator(), Constants.SYSTEM_ACTION };

	public void execute() {
		PrintWriter out = null;

        try {
        	out = response.getWriter();
        } catch (Exception e) {
        	e.printStackTrace();
        }

        try {
            if (! request.isAuthorized()) {
                throw new InvalidSessionException("Invalid Session! <br><a href=\""+Constants.SYSTEM_LOGIN+"\">Try again</a>");
            }            
                                           
            out.println(Library.getFileListReplace(keywords, newWords, Constants.FORM_PATH+"MenuEmployee.html"));
        } catch (Exception e) {
            out.println(HTMLCode.errorPageAdministrator(e.getMessage()));
        } finally {           
            out.close();
        }
	}
}