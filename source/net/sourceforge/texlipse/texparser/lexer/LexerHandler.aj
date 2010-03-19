package net.sourceforge.texlipse.texparser.lexer;

public privileged aspect LexerHandler {

	pointcut staticLexerHandler(): staticinitialization(Lexer);

	declare soft: Exception: staticLexerHandler();

	//XXX rethrow
	void around(): staticLexerHandler(){
		try {
			proceed();
		} catch (Exception e) {
			throw new RuntimeException(
					"The file \"lexer.dat\" is either missing or corrupted.");
		}
	}

}
