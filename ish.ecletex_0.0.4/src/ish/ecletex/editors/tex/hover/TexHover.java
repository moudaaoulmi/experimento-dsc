/*
 * Created on 10-Jul-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ish.ecletex.editors.tex.hover;

import ish.ecletex.editors.tex.TeXEditor;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;

/**
 * @author ish
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class TexHover implements ITextHover, ITextHoverExtension {

	TeXEditor editor;
	TexHoverControlCreator creator;

	public TexHover(TeXEditor editor) {
		this.editor = editor;
	}

	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		return textViewer.getDocument().get(hoverRegion.getOffset(),
				hoverRegion.getLength());
		// return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.ITextHover#getHoverRegion(org.eclipse.jface.text
	 * .ITextViewer, int)
	 */
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		int Start = offset;
		int Finish = offset;
		while ((Start > 0)
				&& !isIgnoreChar(textViewer.getDocument().getChar(Start)))
			Start--;
		while ((Finish < textViewer.getDocument().getLength())
				&& !isIgnoreChar(textViewer.getDocument().getChar(Finish)))
			Finish++;
		Region r = new Region(Start + 1, (Finish - Start) - 1);
		return r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.ITextHoverExtension#getHoverControlCreator()
	 */
	public IInformationControlCreator getHoverControlCreator() {
		if (creator == null) {
			creator = new TexHoverControlCreator(editor);
		}
		return creator;
	}

	public boolean isIgnoreChar(char c) {
		if (c == '{' || c == '}' || c == '[' || c == ']' || c == ' '
				|| c == '\n' || c == '\r')
			return true;
		return false;
	}

}
