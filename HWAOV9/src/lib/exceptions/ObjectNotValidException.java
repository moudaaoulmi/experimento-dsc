package lib.exceptions;

public class ObjectNotValidException extends Exception {

	public ObjectNotValidException(String erro) {
		super("ExcecaoDados: " + erro);
	}
}
