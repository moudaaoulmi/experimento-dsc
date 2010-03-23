package ish.ecletex.editors.tex;

import org.eclipse.jface.text.*;

public class TeXDoubleClickStrategy implements ITextDoubleClickStrategy {
	protected ITextViewer fText;

	public void doubleClicked(ITextViewer part) {
		int pos = part.getSelectedRange().x;

		if (pos < 0)
			return;

		fText = part;
		selectWord(pos);
	}

	protected boolean selectComment(int caretPos) {
		IDocument doc = fText.getDocument();
		int startPos = 0, endPos = 0;
		return internalSelectComment(caretPos, doc, startPos, endPos);
	}

	private boolean internalSelectComment(int caretPos, IDocument doc,
			int startPos, int endPos) {
		int pos = caretPos;
		char c = ' ';

		while (pos >= 0) {
			c = doc.getChar(pos);
			if (c == '\\') {
				pos -= 2;
				continue;
			}
			if (c == Character.LINE_SEPARATOR || c == '\"')
				break;
			--pos;
		}

		if (c != '\"')
			return false;

		startPos = pos;

		pos = caretPos;
		int length = doc.getLength();
		c = ' ';

		while (pos < length) {
			c = doc.getChar(pos);
			if (c == Character.LINE_SEPARATOR || c == '\"')
				break;
			++pos;
		}
		if (c != '\"')
			return false;

		endPos = pos;

		int offset = startPos + 1;
		int len = endPos - offset;
		fText.setSelectedRange(offset, len);
		return true;
	}

	protected boolean selectWord(int caretPos) {

		IDocument doc = fText.getDocument();
		int startPos = 0, endPos = 0;

		return internalSelectWord(caretPos, doc, startPos, endPos);
	}

	private boolean internalSelectWord(int caretPos, IDocument doc,
			int startPos, int endPos) {
		int pos = caretPos;
		char c;

		while (pos >= 0) {
			c = doc.getChar(pos);
			if (!Character.isJavaIdentifierPart(c))
				break;
			--pos;
		}

		startPos = pos;

		pos = caretPos;
		int length = doc.getLength();

		while (pos < length) {
			c = doc.getChar(pos);
			if (!Character.isJavaIdentifierPart(c))
				break;
			++pos;
		}

		endPos = pos;
		selectRange(startPos, endPos);
		return true;
	}

	private void selectRange(int startPos, int stopPos) {
		int offset = startPos + 1;
		int length = stopPos - offset;
		fText.setSelectedRange(offset, length);
	}
}