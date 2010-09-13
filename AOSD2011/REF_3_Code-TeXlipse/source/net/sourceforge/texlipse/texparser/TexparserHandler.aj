package net.sourceforge.texlipse.texparser;

import java.io.IOException;
import java.util.ArrayList;

import net.sourceforge.texlipse.model.ParseErrorMessage;
import net.sourceforge.texlipse.model.ReferenceContainer;
import net.sourceforge.texlipse.model.TexCommandEntry;
import net.sourceforge.texlipse.texparser.lexer.LexerException;
import net.sourceforge.texlipse.texparser.node.Token;

import org.eclipse.core.resources.IMarker;

public privileged aspect TexparserHandler {

	pointcut internalParseHandler(): execution(private boolean LatexParser.internalParse(boolean, Token,
			TexCommandEntry, Token));

	pointcut internalParseHandler1(): execution(private void LatexRefExtractingParser.internalParse(LatexLexer, boolean,
			boolean , Token,TexCommandEntry, int));

	pointcut internalParseHandler2(): execution(private boolean LatexRefExtractingParser.internalParse(boolean,
			TexCommandEntry, Token));

	pointcut countWordsHandler(): execution(public int LatexWordCounter.countWords() );

	pointcut internalParseDocumentHandler(): execution(private void TexParser.internalParseDocument(ReferenceContainer,ReferenceContainer, String ,boolean));

	declare soft: LexerException: internalParseHandler1()||countWordsHandler()||internalParseDocumentHandler();
	declare soft: IOException: countWordsHandler();

	void around(TexParser tex): internalParseDocumentHandler() && this(tex){
		try {
			proceed(tex);
		} catch (LexerException e) {
			// we must parse the lexer exception into a suitable format
			String msg = e.getMessage();
			int first = msg.indexOf('[');
			int last = msg.indexOf(']');
			String numseq = msg.substring(first + 1, last);
			String[] numbers = numseq.split(",");
			tex.errors = new ArrayList<ParseErrorMessage>(1);
			tex.errors.add(new ParseErrorMessage(Integer.parseInt(numbers[0]),
					Integer.parseInt(numbers[1]), 2, msg.substring(last + 2),
					IMarker.SEVERITY_ERROR));
			tex.fatalErrors = true;
		}
	}

	int around(): countWordsHandler() {
		try {
			return proceed();
		} catch (IOException e) {
			return -1;
		} catch (LexerException e) {
			return -1;
		}
	}

	boolean around(boolean expectArg2): internalParseHandler2() && args(expectArg2,..) {
		try {
			return proceed(expectArg2);
		} catch (NumberFormatException nfe) {
			expectArg2 = false;
		}
		return expectArg2;
	}

	void around(LatexRefExtractingParser lat): internalParseHandler1() && this(lat){
		try {
			proceed(lat);
		} catch (LexerException e) {
			lat.fatalErrors = true;
		}
	}

	boolean around(LatexParser lat, boolean expectArg2, Token prevToken, Token t): internalParseHandler() &&args(expectArg2,prevToken,*, t) &&this(lat) {
		try {
			return proceed(lat, expectArg2, prevToken, t);
		} catch (NumberFormatException nfe) {
			lat.errors
					.add(new ParseErrorMessage(
							prevToken.getLine(),
							t.getPos(),
							t.getText().length(),
							"The first optional argument of newcommand must only contain the number of arguments",
							IMarker.SEVERITY_ERROR));
			expectArg2 = false;
		}
		return expectArg2;
	}

}
