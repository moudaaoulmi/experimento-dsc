/*
 * 25.03.2004 
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex.editors.tex;

import org.eclipse.jface.text.rules.IWordDetector;
import java.util.HashMap;

/**
 * @author Stephan Dlugosz
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TeXElse implements IWordDetector {
	HashMap ignoreChars;

	public TeXElse() {
		super();
		ignoreChars = new HashMap();
		makeIgnoreChars();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordStart(char)
	 */
	public boolean isWordStart(char c) {
		if (ignoreChars.containsKey(new Character(c))) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart(char)
	 */
	public boolean isWordPart(char c) {
		if (ignoreChars.containsKey(new Character(c))) {
			return false;
		}
		return true;
	}

	private void makeIgnoreChars() {
		ignoreChars = new HashMap();
		ignoreChars.put(new Character('\n'), new Object());
		ignoreChars.put(new Character('\t'), new Object());
		ignoreChars.put(new Character('\r'), new Object());
		//ignoreChars.put(new Character('?'), new Object()); // Stephan 27.5.2004
		ignoreChars.put(new Character('"'), new Object());
		ignoreChars.put(new Character('%'), new Object());
		//ignoreChars.put(new Character(':'), new Object()); // Stephan 27.5.2004
		//ignoreChars.put(new Character(';'), new Object()); // Stephan 27.5.2004
		ignoreChars.put(new Character('@'), new Object());
		ignoreChars.put(new Character('\''), new Object());
		ignoreChars.put(new Character('~'), new Object());
		ignoreChars.put(new Character('\\'), new Object());
		ignoreChars.put(new Character(((char)65535)), new Object());
	
	}

}