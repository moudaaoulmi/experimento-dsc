package ish.ecletex.editors.tex.spelling.event;

import java.io.File;
import java.io.IOException;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public privileged aspect EventHandler {

	pointcut internalSetupHandler(): execution(private void AbstractWordFinder.internalSetup());

	pointcut internalDocumentWordTokenizerHandler(): execution(private void DocumentWordTokenizer.internalDocumentWordTokenizer(Document));

	pointcut internalNextWordHandler(): execution(private String DocumentWordTokenizer.internalNextWord(String));

	pointcut internalReplaceWordHandler(): execution(private void DocumentWordTokenizer.internalReplaceWord(String));

	pointcut internalStringValueHandler(): execution(private static void FileWordTokenizer.internalStringValue(File , StringBuffer));

	pointcut spellCheckerHandler(): execution(SpellChecker.new());

	declare soft: BadLocationException: internalDocumentWordTokenizerHandler() || internalNextWordHandler()|| internalReplaceWordHandler();

	declare soft: IOException: internalStringValueHandler() || spellCheckerHandler();

	void around(): spellCheckerHandler() {
		try {
			proceed();
		} catch (IOException e) {
			throw new RuntimeException(
					"this exception should never happen because we are using null phonetic file");
		}
	}

	void around(File inFile): internalStringValueHandler() && args(inFile, *) {
		try {
			proceed(inFile);
		} catch (IOException e) {
			System.err.println("File input error trying to open "
					+ inFile.toString() + " : " + e);
		}
	}

	void around(): internalReplaceWordHandler() {
		try {
			proceed();
		} catch (BadLocationException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	String around(DocumentWordTokenizer doc, String word): internalNextWordHandler() && args(word)&& this(doc) {
		try {
			return proceed(doc, word);
		} catch (BadLocationException ex) {
			doc.moreTokens = false;
		}
		return word;
	}

	void around(DocumentWordTokenizer doc): internalDocumentWordTokenizerHandler() && this(doc) {
		try {
			proceed(doc);
		} catch (BadLocationException ex) {
			doc.moreTokens = false;
		}
	}

	void around(AbstractWordFinder abt): internalSetupHandler() && this(abt){
		try {
			proceed(abt);
		} catch (WordNotFoundException e) {
			abt.currentWord = null;
			abt.nextWord = null;
		}
	}

}
