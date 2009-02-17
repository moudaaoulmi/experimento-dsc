package healthwatcher.view.command;

import healthwatcher.view.IFacade;

public abstract class Command {
	
	protected IFacade facade;
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

	public Command(IFacade f){
		facade= f;
	}
	
	public abstract void execute();

}
