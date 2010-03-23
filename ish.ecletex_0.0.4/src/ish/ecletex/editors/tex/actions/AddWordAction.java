/*
 * Created on 16-Mar-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex.editors.tex.actions;

import ish.ecletex.editors.tex.TeXEditor;
import ish.ecletex.editors.tex.TextWords;
import ish.ecletex.editors.tex.spelling.GlobalDictionary;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * @author ish
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class AddWordAction extends Action {
	private ITextEditor textEditor;

	public AddWordAction(ITextEditor editor) {
		this.textEditor = editor;
	}

	public void run() {

		String word = getCurrentWord(textEditor);
		if (word != null)
			GlobalDictionary.addWord(word, (TeXEditor) textEditor);

		// TODO : doc needed
		// IDocument doc =
		// textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
	}

	public static String getCurrentWord(ITextEditor editor) {
		ITextSelection selection = (ITextSelection) editor
				.getSelectionProvider().getSelection();
		if (selection != null) {
		}
		IDocument doc = editor.getDocumentProvider().getDocument(
				editor.getEditorInput());
		TextWords words = new TextWords();
		int currentPos = selection.getOffset();
		return internalGetCurrentWord(doc, words, currentPos);

	}

	private static String internalGetCurrentWord(IDocument doc,
			TextWords words, int currentPos) {
		char currentChar = doc.getChar(currentPos);
		if (words.isWordPart(currentChar)) {
			int selectpos = currentPos;
			while (words.isWordPart(currentChar = doc.getChar(currentPos))) {
				currentPos--;
			}

			int Begin = currentPos;

			currentPos = selectpos;

			while (words.isWordPart(currentChar = doc.getChar(currentPos))) {
				currentPos++;
			}
			int End = currentPos;

			String word = doc.get(Begin, End - Begin);
			word = word.trim();
			return word;

		} else {
			return null;
		}
	}

}