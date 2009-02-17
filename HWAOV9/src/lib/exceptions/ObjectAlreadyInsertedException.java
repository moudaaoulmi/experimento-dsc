package lib.exceptions;

public class ObjectAlreadyInsertedException extends Exception {

	public ObjectAlreadyInsertedException(String erro) {
		super("ExcecaoDados: " + erro);
	}
}
