package net.sourceforge.texlipse.bibparser;

import java.util.List;

import net.sourceforge.texlipse.bibparser.lexer.LexerException;
import net.sourceforge.texlipse.bibparser.parser.ParserException;
import net.sourceforge.texlipse.model.ParseErrorMessage;

import org.eclipse.core.resources.IMarker;

public privileged aspect BibparserHandler {

	pointcut getEntriesHandler(): execution(public List BibParser.getEntries());

	declare soft: LexerException: getEntriesHandler();
	declare soft: ParserException: getEntriesHandler();

	List around(BibParser bib): getEntriesHandler()&& this(bib) {
		try {
			return proceed(bib);
		} catch (LexerException le) {
			String msg = le.getMessage();
			int first = msg.indexOf('[');
			int last = msg.indexOf(']');
			String numseq = msg.substring(first + 1, last);
			String[] numbers = numseq.split(",");
			bib.errors.add(new ParseErrorMessage(Integer.parseInt(numbers[0]),
					Integer.parseInt(numbers[1]) - 1, 2, msg
							.substring(last + 2), IMarker.SEVERITY_ERROR));
		} catch (ParserException pe) {
			String msg = pe.getMessage();
			int last = msg.indexOf(']');
			bib.errors.add(new ParseErrorMessage(pe.getToken().getLine(), pe
					.getToken().getPos(), pe.getToken().getText().length(), msg
					.substring(last + 2), IMarker.SEVERITY_ERROR));
		}
		return null;
	}

}
