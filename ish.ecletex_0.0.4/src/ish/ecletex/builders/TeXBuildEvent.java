/*
 * Created on 18-Nov-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex.builders;

/**
 * @author ish
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TeXBuildEvent {
	public static String ERROR = "error";
	public static String WARNING = "warning";
	
	public String EventClass;
	
	public int LineNumber;
	
	public String Message;
	
	public String Filename;
	
	public TeXBuildEvent(String EventClass,int LineNumber,String Message,String Filename){
		this.EventClass = EventClass;
		this.LineNumber = LineNumber;
		this.Message = Message;
		this.Filename = Filename;
	}
}
