/*
 * Created on 22-Sep-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex.editors.tex.sections;


/**
 * @author ish
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Chapter extends AbstractSection {

	public Chapter(String name) {
		setTitle(name);
	}
	
	public Chapter(String name,String file){
		this(name);
		setFile(file);
	}
}
