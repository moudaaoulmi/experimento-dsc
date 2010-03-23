/*
 * Created on 17-Sep-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex.editors.tex;

/**
 * @author ish
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Command{
	public String word;
	public String description;
	public Argument[] arguments;
	public Argument[] optionals;
	
	public Command(String word,String description,Argument[] arguments,Argument[] optionals){
		this.word = word;
		this.description = description;
		this.arguments = arguments;
		this.optionals = optionals;
	}
}
