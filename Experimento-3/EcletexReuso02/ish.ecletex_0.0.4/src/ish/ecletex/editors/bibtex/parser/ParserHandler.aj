package ish.ecletex.editors.bibtex.parser;

import java.io.IOException;

public privileged aspect ParserHandler {

	pointcut internalParseHandler(): execution(private void BibtexParser.internalParse());

	declare soft: ParseException : internalParseHandler();

	void around(BibtexParser bib) throws IOException: internalParseHandler() && this(bib) {
		try {
			proceed(bib);
		} catch (ParseException parseException) {
			bib.exceptions.add(parseException);
		}
	}

}
