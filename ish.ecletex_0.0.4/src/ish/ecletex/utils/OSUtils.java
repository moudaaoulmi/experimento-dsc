/*
 * Created on 10-Aug-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ish.ecletex.utils;


/**
 * @author ish
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OSUtils {
	public static final OS WINDOWS = new OS("windows");
	public static final OS UNIX = new OS("unix");
	public static final OS MAC = new OS("mac");
	public static final OS UNDEFINED = new OS("undefined");
	public static final String OS_PROPERTY_NAME = "os.name";
	
	private static OS _currentOS;

	public static final OS[] DEFINED_OS_ARRAY = {WINDOWS, UNIX, MAC};
	
	public static OS currentOS(){
		String opsys = System.getProperty(OS_PROPERTY_NAME);
		for (int i = 0; i < DEFINED_OS_ARRAY.length; i++)
			if (DEFINED_OS_ARRAY[i].matches(opsys))
				_currentOS = DEFINED_OS_ARRAY[i];
		
			return _currentOS;
	}
	
	
	public static final class OS {
		private String _name;

		protected OS(String name) {
			_name = name;
		}

		public String toString() {
			return _name;
		}

		public boolean matches(String test) {
			return test.toLowerCase().indexOf(_name.toLowerCase()) >= 0;
		}
	}
}
