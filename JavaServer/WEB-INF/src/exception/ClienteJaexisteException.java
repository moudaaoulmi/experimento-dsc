package exception;

public class ClienteJaexisteException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3962192379313583095L;

	public ClienteJaexisteException() {
		super("Cliente já existe no sistema");
		
	}


}
