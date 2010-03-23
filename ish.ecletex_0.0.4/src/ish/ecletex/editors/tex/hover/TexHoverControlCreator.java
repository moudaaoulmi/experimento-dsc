/*
 * Created on 10-Jul-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ish.ecletex.editors.tex.hover;

import ish.ecletex.editors.tex.TeXEditor;

import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.swt.widgets.Shell;

/**
 * @author ish
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TexHoverControlCreator implements IInformationControlCreator {

	private TeXEditor editor;
	
	public TexHoverControlCreator(TeXEditor editor){
		this.editor = editor;
	}
	
	
	public IInformationControl createInformationControl(Shell parent) {
		return new TexInformationControl(editor,parent);
	}

}
