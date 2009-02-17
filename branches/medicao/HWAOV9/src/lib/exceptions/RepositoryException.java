package lib.exceptions;

public class RepositoryException extends Exception {

	public RepositoryException(String erro) {
		super("ExcecaoDados: " + erro);
	}
}
