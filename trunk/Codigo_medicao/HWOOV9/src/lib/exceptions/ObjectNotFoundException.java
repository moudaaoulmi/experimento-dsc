package lib.exceptions;

public class ObjectNotFoundException extends Exception {

	public ObjectNotFoundException(String erro) {
		super("ExcecaoDados: " + erro);
	}
}
