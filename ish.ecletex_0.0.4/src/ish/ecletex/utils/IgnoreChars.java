/*
 * Created on 06-Oct-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex.utils;

import java.util.HashMap;

/**
 * @author ish
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class IgnoreChars {
	private static HashMap ignoreChars;
	
	public static boolean isIgnoreChar(char c){
		checkInit();
		if(ignoreChars.containsKey(new Character(c))){
			return true;
		}else{
			return false;
		}
	}
	
	private static void checkInit(){
		if(ignoreChars==null){
					ignoreChars = new HashMap();
					ignoreChars.put(new Character(' '), new Object());
					ignoreChars.put(new Character('\n'), new Object());
					ignoreChars.put(new Character('\t'), new Object());
					ignoreChars.put(new Character('\r'), new Object());
					ignoreChars.put(new Character('{'), new Object());
					ignoreChars.put(new Character('}'), new Object());
					ignoreChars.put(new Character('['), new Object());
					ignoreChars.put(new Character(']'), new Object());
					ignoreChars.put(new Character('!'), new Object());
					ignoreChars.put(new Character('.'), new Object());
					ignoreChars.put(new Character(','), new Object());
					ignoreChars.put(new Character('?'), new Object());
					ignoreChars.put(new Character('"'), new Object());
					ignoreChars.put(new Character('£'), new Object());
					ignoreChars.put(new Character('$'), new Object());
					ignoreChars.put(new Character('%'), new Object());
					ignoreChars.put(new Character('^'), new Object());
					ignoreChars.put(new Character('&'), new Object());
					ignoreChars.put(new Character('*'), new Object());
					ignoreChars.put(new Character(':'), new Object());
					ignoreChars.put(new Character(';'), new Object());
					ignoreChars.put(new Character('@'), new Object());
					ignoreChars.put(new Character('\''), new Object());
					ignoreChars.put(new Character('#'), new Object());
					ignoreChars.put(new Character('~'), new Object());
					ignoreChars.put(new Character('/'), new Object());
					//ignoreChars.put(new Character('-'), new Object());
					ignoreChars.put(new Character('+'), new Object());
					ignoreChars.put(new Character('|'), new Object());
					ignoreChars.put(new Character('\\'), new Object());
					ignoreChars.put(new Character('<'), new Object());
					ignoreChars.put(new Character('>'), new Object());
					ignoreChars.put(new Character('='), new Object());
					ignoreChars.put(new Character('+'), new Object());
					ignoreChars.put(new Character(((char)65535)), new Object());
		}
	}
}
