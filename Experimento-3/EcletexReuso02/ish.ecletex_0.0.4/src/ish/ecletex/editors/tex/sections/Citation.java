/*
 * Created on 09-Oct-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex.editors.tex.sections;

import org.eclipse.jface.text.Position;

/**
 * @author ish
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Citation extends AbstractSection {
	
	public Citation(String name, Position position) {
		setTitle(name);
		setPosition(position);
	}
	
	public Citation(String name, String file) {
		setTitle(name);
		setFile(file);
		
	}
}
