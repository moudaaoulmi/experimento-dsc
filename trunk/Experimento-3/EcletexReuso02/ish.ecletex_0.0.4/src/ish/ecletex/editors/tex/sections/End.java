/*
 * Created on 03-Oct-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ish.ecletex.editors.tex.sections;

import org.eclipse.jface.text.Position;

/**
 * @author Ian Hartney
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class End extends AbstractSection {
	public End(String name, Position positon){
		setTitle(name);
		setPosition(positon);
	}
	
	public End(String name, String file){
		setTitle(name);
		setFile(file);
	}
}
