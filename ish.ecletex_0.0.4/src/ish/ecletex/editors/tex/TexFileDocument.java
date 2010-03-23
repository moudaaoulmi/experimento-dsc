/*
 * Created on 05-Oct-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ish.ecletex.editors.tex;

import org.eclipse.jface.text.Document;

/**
 * @author Ian Hartney
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TexFileDocument extends Document {
	public TexFileDocument(){
		super();
		setLineTracker(new FlowLineTracker());
	}
	
	public TexFileDocument(String contents){
		super(contents);
		setLineTracker(new FlowLineTracker());
	}
}
