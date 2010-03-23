/*
 * Created on 06-Oct-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ish.ecletex.editors.tex;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.widgets.Display;

/**
 * @author Ian Hartney
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class Flow2DocumentListener implements IDocumentListener {

	private int MAX_LINE = 80;

	private class CarotUpdater implements Runnable {
		private int pos;

		public CarotUpdater(int pos) {
			this.pos = pos;
		}

		public void run() {
			editor.setCaretPos(pos);

		}
	}

	private TeXEditor editor;

	public Flow2DocumentListener(TeXEditor editor) {
		this.editor = editor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.IDocumentListener#documentAboutToBeChanged(org
	 * .eclipse.jface.text.DocumentEvent)
	 */
	public void documentAboutToBeChanged(DocumentEvent event) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.IDocumentListener#documentChanged(org.eclipse.
	 * jface.text.DocumentEvent)
	 */
	public void documentChanged(DocumentEvent event) {
		int line = event.getDocument().getLineOfOffset(event.getOffset());
		int caretpos = editor.getCaretPos();
		int origionalOffset = event.getDocument().getLineOffset(line);
		int FixLineAction = FixLine(line, event.getDocument());
		if (caretpos > FixLineAction) {
			// System.out.println("Moved Caret to ["+event.getDocument().getChar(caretpos+3)+"]");
			// System.out.println("Moved Caret to ["+event.getDocument().getChar(caretpos+4)+"]");
			int newcarotpos = caretpos + 4;
			CarotUpdater update = new CarotUpdater(newcarotpos);
			Display.getDefault().asyncExec(update);
		} else if (caretpos == FixLineAction) {
			int lineoffset = event.getDocument().getLineOffset(line + 1);
			// System.out.println("Moved Caret to ["+event.getDocument().getChar(caretpos+4)+"]");
			int newcarotpos = lineoffset + 1;
			CarotUpdater update = new CarotUpdater(newcarotpos);
			Display.getDefault().asyncExec(update);
		}
	}

	public int FixLine(int line, IDocument document)
			throws BadLocationException {
		String delimiter = document.getLineDelimiter(line);
		int delimiterlength = 0;
		if (delimiter != null) {
			delimiterlength = delimiter.length();
		} else {
			delimiter = "";
		}
		int linelength = document.getLineLength(line) - delimiterlength;
		// int caretposition = editor.getCaretPos();
		if (linelength > MAX_LINE) {
			// System.out.println("LINE > 80 ["+linelength+"]");
			int lineoffset = document.getLineOffset(line);
			int breakpoint = lineoffset + MAX_LINE;

			if (document.getChar(breakpoint) == ' ') {
				breakpoint--;
			}

			for (int i = breakpoint - 1; i > lineoffset; i--) {
				if (document.getChar(i) == ' ') {
					breakpoint = i;
					break;
				}
			}

			if (breakpoint == (lineoffset + MAX_LINE))
				document.replace(breakpoint, 0, "\r\r\n");
			else
				document.replace(breakpoint + 1, 0, "\r\r\n");

			FixLine(line + 1, document);

			// System.out.println("CAROT ["+caretposition+"] BREAKPOINT ["+breakpoint+"]");
			return breakpoint;

		} else if (linelength < MAX_LINE && delimiter.equals("\r\r\n")) {
			int firstwordlength = getFirstWordLength(line + 1, document);
			if ((linelength + firstwordlength + 1) < MAX_LINE) {
				RemoveLineDelimiter(line, document);
			}
		}
		return Integer.MAX_VALUE;

	}

	private int getFirstWordLength(int line, IDocument document)
			throws BadLocationException {
		if (document.getLineLength(line) == 0)
			return -1;
		int lineLength = document.getLineLength(line);
		for (int i = 0; i < lineLength; i++) {
			int offset = document.getLineOffset(line);
			int char_offset = offset + i;
			char c_char = document.getChar(char_offset);
			if (c_char == ' ') {
				return i + 1;
			}
		}

		return document.getLineLength(line);
	}

	private void RemoveLineDelimiter(int line, IDocument document)
			throws BadLocationException {
		String doc = document.get();
		int c_offset = document.getLineOffset(line);
		int c_length = document.getLineLength(line);
		String line_delimiter = document.getLineDelimiter(line);
		if (line_delimiter == null)
			return;

		String remove = doc.substring(c_offset + c_length
				- line_delimiter.length(), c_offset + c_length);

		if (!isLineDelimiter(remove, document))
			return;

		if (doc.charAt((c_offset + c_length) - (line_delimiter.length() + 1)) != ' ') {
			document.replace((c_offset + c_length) - line_delimiter.length(),
					line_delimiter.length(), "");
			// Display.getDefault().asyncExec(new
			// TextUpdater((c_offset+c_length)-line_delimiter.length(),line_delimiter.length()," "));
		} else {
			document.replace((c_offset + c_length) - line_delimiter.length(),
					line_delimiter.length(), "");
			// Display.getDefault().asyncExec(new
			// TextUpdater((c_offset+c_length)-line_delimiter.length(),line_delimiter.length(),""));
		}
	}

	private boolean isLineDelimiter(String s, IDocument document) {
		boolean isDelimiter = false;
		for (int i = 0; i < document.getLegalLineDelimiters().length; i++) {
			if (s.equals(document.getLegalLineDelimiters()[i])) {
				isDelimiter = true;
				break;
			}
		}
		return isDelimiter;

	}

}
