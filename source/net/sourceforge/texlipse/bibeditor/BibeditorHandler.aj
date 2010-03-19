package net.sourceforge.texlipse.bibeditor;

import java.io.IOException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import net.sourceforge.texlipse.TexlipsePlugin;
import net.sourceforge.texlipse.editor.TexDocumentParseException;

public privileged aspect BibeditorHandler {

	pointcut doParseHandler(): execution(private void BibDocumentModel.doParse());

	pointcut internalUpdateDocumentPositionsHandler(): execution(private void BibDocumentModel.internalUpdateDocumentPositions(IDocument));

	pointcut internalUpdateDocumentPositions2Handler(): execution(private void BibDocumentModel.internalUpdateDocumentPositions2(IDocument));

	pointcut updateHandler(): execution(public void BibDocumentModel.update());

	pointcut internalSelectionChangedHandler(): execution(private void BibOutlinePage.internalSelectionChanged(ISelection));

	pointcut internalDocumentChangedHandler(): execution(private void BibStringCompleter.internalDocumentChanged(ITextSelection));

	pointcut internalDocumentChanged2Handler(): execution(private void BibStringCompleter.internalDocumentChanged2(ITextSelection));

	declare soft: IOException: doParseHandler();
	declare soft: BadPositionCategoryException: internalUpdateDocumentPositionsHandler()||internalUpdateDocumentPositions2Handler();
	declare soft: BadLocationException: internalUpdateDocumentPositions2Handler()||internalDocumentChangedHandler()||internalDocumentChanged2Handler();
	declare soft: TexDocumentParseException: updateHandler();

	void around(): internalDocumentChangedHandler()||internalDocumentChanged2Handler() {
		try {
			proceed();
		} catch (BadLocationException e) {
		}
	}

	void around(BibOutlinePage bib): internalSelectionChangedHandler()&& this(bib){
		try {
			proceed(bib);
		} catch (IllegalArgumentException x) {
			bib.editor.resetHighlightRange();
		} catch (ClassCastException y) {
			bib.editor.resetHighlightRange();
		} catch (NullPointerException z) {
			bib.editor.resetHighlightRange();
		}
	}

	void around(): updateHandler() {
		try {
			proceed();
		} catch (TexDocumentParseException e) {
			// We do nothing, since the error is already added
			// TexlipsePlugin.log("There were parse errors in the document", e);
		}
	}

	void around(): internalUpdateDocumentPositions2Handler() {
		//(2)
		try {
			proceed();
		} catch (BadPositionCategoryException bpce) {
			//XXX LOG  - Não Generalizado totalmente
			TexlipsePlugin
					.log(
							"BibDocumentModel.updateDocumentPositions: bad position category ",
							bpce);
		} catch (BadLocationException ble) {
			//XXX LOG  - Não Generalizado totalmente
			TexlipsePlugin.log(
					"BibDocumentModel.updateDocumentPositions: bad position ",
					ble);
		}
	}

	void around(): internalUpdateDocumentPositionsHandler() {
		try {
			proceed();
		} catch (BadPositionCategoryException bpce) {
			// do nothing
		}
	}

	void around() throws TexDocumentParseException: doParseHandler() {
		try {
			proceed();
		} catch (IOException e) {
			TexlipsePlugin.log("Can't read file.", e);
			throw new TexDocumentParseException(e);
		}
	}

}
