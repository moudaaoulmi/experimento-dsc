package exception;

public class UsuarioNaoExistenteException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4091860180206343697L;

	public UsuarioNaoExistenteException() {
		// TODO Auto-generated constructor stub
		super("Usuario nao existe no sistema");
	}

}
