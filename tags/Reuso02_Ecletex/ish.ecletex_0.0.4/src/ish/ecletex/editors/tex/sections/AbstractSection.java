
package ish.ecletex.editors.tex.sections;

import java.util.ArrayList;

import org.eclipse.jface.text.Position;

/**
 * Abstract base class for sections.
 * 
 * @author Thorsten Schäfer & Ian Hartney
 */
public class AbstractSection implements ISection {
	
	private ArrayList children = new ArrayList();
	private ArrayList begin_ends = new ArrayList();
	private Object parent;
	private String title;
	private Position position;
	private String file;	
	public static String ID_SEPERATOR= "-[=]-";

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void SetTitle(String title){
		this.title = title;
	}

	public ISection[] getChildern() {
		return (ISection[]) children.toArray(new ISection[children.size()]);
	}

	public Object getParent() {
		return parent;
	}

	public void setParent(Object parent) {
		this.parent = parent;
	}

	public void addChild(ISection child) {
		children.add(child);
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	protected void setFile(String file) {
		this.file = file;
	}
	
	public String getFile() {
		return file;
	}
	
	public String toString(){
		return getTitle();
	}	
	
	public String getTreeString(){
		if(parent==null || !(parent instanceof AbstractSection))
			return "document-"+getTitle();
		else
			return ((AbstractSection) parent).getTreeString()+"-"+getTitle();
	}
	
	public void addBeginEnd(ISection begin_end){
		int offset = 0;
		if(begin_end instanceof Begin){
			for(int i=0;i<begin_ends.size();i++){
				ISection s = (ISection)begin_ends.get(i);
				if(s instanceof Begin && s.getTitle().startsWith(begin_end.getTitle())){
					offset++;
				}
			}
		}else{
			for(int i=0;i<begin_ends.size();i++){
				ISection s = (ISection)begin_ends.get(i);
				if(s instanceof End && s.getTitle().startsWith(begin_end.getTitle())){
					offset++;
				}
			}
		}
		if(offset!=0)
			begin_end.setTitle(begin_end.getTitle()+ID_SEPERATOR+offset);
		begin_ends.add(begin_end);		
	}
}
