package ish.ecletex.builders.latexlogparser;

import java.io.FileNotFoundException;
import java.util.EmptyStackException;

public privileged aspect LatexlogparserHandler {

	pointcut parserHandler(): execution(public void Parser.parse());

	pointcut internalFileHandler(): execution(private void Parser.internalFile());

	declare soft: FileNotFoundException : parserHandler();
	declare soft: Error: parserHandler();
	declare soft: Exception: parserHandler();

	void around(Parser parser): parserHandler() && this(parser)
	{
		try {
			proceed(parser);
		} catch (ArrayIndexOutOfBoundsException arrayindexoutofboundsexception) {
			parser.model.add(new Entry(101,
					"Log file is 'corrupt'.. did TeX/LaTeX compile properly?",
					null, -1));
		} catch (NumberFormatException numberformatexception) {
			parser.model.add(new Entry(101,
					"Log file is 'corrupt'.. did TeX/LaTeX compile properly?",
					null, -1));
		} catch (NullPointerException nullpointerexception) {
			parser.model.add(new Entry(101,
					"You need to choose a TeX/LaTeX log file...", null, -1));
		} catch (FileNotFoundException filenotfoundexception) {
			parser.model.add(new Entry(101, "The chosen file \""
					+ parser.filename + "\" could not be opened", null, -1));
		} catch (Error error) {
			parser.model.add(new Entry(101, "Unexpected error "
					+ error.getMessage() + " please contact the author...",
					null, -1));
		} catch (Exception exception) {
			parser.model.add(new Entry(101, "Unexpected exception "
					+ exception.getMessage() + " please contact the author...",
					null, -1));
		}
	}

	// Runtime eh EmptyStackException
	void around(): internalFileHandler() {
		try {
			proceed();
		} catch (EmptyStackException emptystackexception) {
		}
	}

}
