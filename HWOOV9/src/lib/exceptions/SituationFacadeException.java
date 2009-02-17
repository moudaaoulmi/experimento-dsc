package lib.exceptions;

public class SituationFacadeException extends Exception {

	public SituationFacadeException(String erro) {
		super("Excecao: " + erro);
	}
}