package healthwatcher.view.command;

import lib.patterns.Command;

/**
 * Sets up the Command pattern.
 * 
 * @author  Marcos Dósea
 * @author  Sérgio Soares
 * @version 04/09/2006
 * 
 */
public abstract class CommandServlet implements Command {

	protected CommandRequest request;
	protected CommandResponse response;
	
	public void setRequest(CommandRequest request)
	{
		this.request = request;
	}
	public void setReponse(CommandResponse response)
	{
		this.response = response;
	}
	
}
