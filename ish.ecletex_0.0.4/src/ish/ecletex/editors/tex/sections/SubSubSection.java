/*
 * Created on 22-Sep-2003
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
public class SubSubSection extends AbstractSection {

	public SubSubSection(String name, Position position) {
		setTitle(name);
		setPosition(position);
	}
	
	public SubSubSection(String name, String file) {
		setTitle(name);
		setFile(file);
	}
}
