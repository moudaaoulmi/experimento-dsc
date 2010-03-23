/*
 * Created on 05-Oct-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ish.ecletex.editors.tex;

import org.eclipse.jface.text.AbstractLineTracker;

/**
 * @author Ian Hartney
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FlowLineTracker extends AbstractLineTracker {
	/** The predefined delimiters of this tracker */
	public final static String[] DELIMITERS= { "\r", "\n", "\r\n","\r\r\n" }; //$NON-NLS-3$ //$NON-NLS-1$ //$NON-NLS-2$
	/** A predefined delimiter information which is always reused as return value */
	//private DelimiterInfo fDelimiterInfo= new DelimiterInfo();
	
	protected class TexDelimiterInfo extends DelimiterInfo{
				
	}
	
	public TexDelimiterInfo fDelimiterInfo = new TexDelimiterInfo();
	
	/**
	 * Creates a standard line tracker.
	 */
	public FlowLineTracker() {
	}
	
	/*
	 * @see org.eclipse.jface.text.ILineTracker#getLegalLineDelimiters()
	 */
	public String[] getLegalLineDelimiters() {
		return DELIMITERS;
	}

	/*
	 * @see org.eclipse.jface.text.AbstractLineTracker#nextDelimiterInfo(java.lang.String, int)
	 */
	protected DelimiterInfo nextDelimiterInfo(String text, int offset) {
		char ch;
		int length= text.length();
		for (int i= offset; i < length; i++) {
			
			ch= text.charAt(i);
			if (ch == '\r') {
				
				if (i + 1 < length) {
					if (text.charAt(i + 1) == '\n') {
						fDelimiterInfo.delimiter= DELIMITERS[2];
						fDelimiterInfo.delimiterIndex= i;
						fDelimiterInfo.delimiterLength= 2;
						return fDelimiterInfo;
					}else if (text.charAt(i + 1) == '\r') {
						if(i + 2 < length){
							if (text.charAt(i + 2) == '\n') {
								fDelimiterInfo.delimiter= DELIMITERS[3];
								fDelimiterInfo.delimiterIndex= i;
								fDelimiterInfo.delimiterLength= 3;
								return fDelimiterInfo;
							}
							
						}
						
					}
				}
				
				fDelimiterInfo.delimiter= DELIMITERS[0];
				fDelimiterInfo.delimiterIndex= i;
				fDelimiterInfo.delimiterLength= 1;
				return fDelimiterInfo;
				
			} else if (ch == '\n') {
				
				fDelimiterInfo.delimiter= DELIMITERS[1];
				fDelimiterInfo.delimiterIndex= i;
				fDelimiterInfo.delimiterLength= 1;
				return fDelimiterInfo;
			}
		}
		
		return null;
	}

}
