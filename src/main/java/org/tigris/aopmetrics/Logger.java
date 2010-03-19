package org.tigris.aopmetrics;

/**
 * Logger class for logging metrics output.
 * 
 * @author <a href="mailto:misto@e-informatyka.pl">Michal Stochmialek</a>
 * @version CVS $Id: Logger.java,v 1.1 2006/04/20 23:37:22 misto Exp $
 */
public class Logger {
	enum Mode {
		NORMAL, INFO, DEBUG;

		static Mode createByName(String name) {
			return Mode.valueOf(name.toUpperCase());
		}
	};

	static Mode mode = Mode.NORMAL;

	public static void setMode(String name) {
		mode = Mode.createByName(name);
	}

	public static boolean isDebugMode() {
		return mode == Mode.DEBUG;
	}

	public static boolean isInfoMode() {
		return mode == Mode.INFO;
	}

	public static void debug(String msg) {
		log(Mode.DEBUG, msg);
	}

	public static void info(String msg) {
		log(Mode.INFO, msg);
	}

	public static void msg(String msg) {
		log(Mode.NORMAL, msg);
	}

	public static void log(Mode askedMode, String msg) {
		if (mode.compareTo(askedMode) >= 0)
			System.out.println(msg);
	}
}
