package lib.util;

public class Functions {

	/**
	 * Comentário do construtor Funcoes.
	 */
	public Functions() {
	}

	/**
	 * Insira a descrição do método aqui.
	 * Data de criação: (02/06/2000 10:45:43)
	 * @param campo int
	 */
	public void campoPreenchido(int campo) {

		if (campo == 0) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Insira a descrição do método aqui.
	 * Data de criação: (02/06/2000 10:45:43)
	 * @param Object
	 */
	public void campoPreenchido(Object o) {

		if (o == null) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Insira a descrição do método aqui.
	 * Data de criação: (02/06/2000 10:45:43)
	 * @param campo java.lang.String
	 */
	public void campoPreenchido(String campo) {

		if (campo.length() == 0) {
			throw new IllegalArgumentException();
		}

		if (campo == " ") {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Insira a descrição do método aqui.
	 * Data de criação: (02/06/2000 10:45:43)
	 * @param short
	 */
	public void campoPreenchido(short campo) {

		if (campo == 0) {
			throw new IllegalArgumentException();
		}
	}
}