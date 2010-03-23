/*
 * Created on 28-Nov-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex.editors.tex;

import java.util.HashMap;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * @author Stephan Dlugosz,ish
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */


public class NumberWord implements IWordDetector {


//	 this class should detect numbers in arguments like "-2.1ex" etc.

	HashMap numberChars;

	public NumberWord() {
		super();
		numberChars = new HashMap();
		makeNumberChars();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordStart(char)
	 */
	public boolean isWordStart(char c) {
		
		return (c >= '0' && c <= '9')||(c=='-');  //Christian Ullenboom, Stephan Dlugosz

		//if (numberChars.containsKey(new Character(c))) {
		//	return true;
		//}
		//return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart(char)
	 */
	public boolean isWordPart(char c) {
		if (numberChars.containsKey(new Character(c))) {
			return true;
		}
		return false;
	}

	private void makeNumberChars() {
		numberChars = new HashMap();
		numberChars.put(new Character('0'), new Object());
		numberChars.put(new Character('1'), new Object());
		numberChars.put(new Character('2'), new Object());
		numberChars.put(new Character('3'), new Object());
		numberChars.put(new Character('4'), new Object());
		numberChars.put(new Character('5'), new Object());
		numberChars.put(new Character('6'), new Object());
		numberChars.put(new Character('7'), new Object());
		numberChars.put(new Character('8'), new Object());
		numberChars.put(new Character('9'), new Object());
		numberChars.put(new Character('.'), new Object());
		numberChars.put(new Character('b'), new Object()); // bp
		numberChars.put(new Character('c'), new Object()); // cm, pc, cc
		numberChars.put(new Character('d'), new Object()); // dd
		numberChars.put(new Character('e'), new Object()); // ex, em
		numberChars.put(new Character('i'), new Object()); // in
		numberChars.put(new Character('m'), new Object()); // cm , mm, em
		numberChars.put(new Character('n'), new Object()); // in
		numberChars.put(new Character('p'), new Object()); // pt, pc, bp, sp
		numberChars.put(new Character('s'), new Object()); // sp
		numberChars.put(new Character('t'), new Object()); // pt
		numberChars.put(new Character('x'), new Object()); // ex
		
	}

}
