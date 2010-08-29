package exception;

public class UsuarioJaExistenteException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3929887631603103505L;

	public UsuarioJaExistenteException() {
		// TODO Auto-generated constructor stub
		super("Usuario já existe no sistema");
	}

}
