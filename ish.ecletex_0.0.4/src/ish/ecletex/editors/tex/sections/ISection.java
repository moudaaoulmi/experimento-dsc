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
public interface ISection {
	public String getTitle();
	public void setTitle(String title);
	public ISection[] getChildern();
	public Object getParent();
	public void addChild(ISection child);
	public void setParent(Object parent);
	public Position getPosition();
	public void setPosition(Position p);
	public String getFile();
	public String getTreeString();
	public void addBeginEnd(ISection begin_end);
}
