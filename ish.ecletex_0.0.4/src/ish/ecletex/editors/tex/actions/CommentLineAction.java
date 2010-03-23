/*
 * Created on 16-Mar-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex.editors.tex.actions;

import org.aspectj.lang.SoftException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Action to comment or decomment lines in the latex source.
 * 
 * @author ish, Thorsten Schäfer
 */
public class CommentLineAction extends Action {
	private ITextEditor textEditor;

	public CommentLineAction(ITextEditor editor) {
		this.textEditor = editor;
		this.setText("(Un)Comment Line@Ctrl+k");
	}

	public void run() {
		ITextSelection selection = (ITextSelection) textEditor
				.getSelectionProvider().getSelection();
		if (selection != null) {
			IDocument document = textEditor.getDocumentProvider().getDocument(
					textEditor.getEditorInput());
			int startingLine = selection.getStartLine();
			int endingLine = selection.getEndLine();
			if (startingLine != -1 && endingLine != -1) {
				for (int lineNumber = startingLine; lineNumber <= endingLine; lineNumber++) {
					internalRun(document, lineNumber);
				}
			}
		}
	}

	private void internalRun(IDocument document, int lineNumber) {
		int firstCharacterOffset = document.getLineOffset(lineNumber);
		if (document.getChar(firstCharacterOffset) == '%') {
			document.replace(firstCharacterOffset, 1, "");
		} else {
			document.replace(firstCharacterOffset, 0, "%");
		}
	}
}
