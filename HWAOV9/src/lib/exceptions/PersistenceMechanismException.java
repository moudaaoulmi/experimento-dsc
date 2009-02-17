package lib.exceptions;

public class PersistenceMechanismException extends Exception {

	public PersistenceMechanismException(String erro) {
		super("ExcecaoDados: " + erro);
	}
}
