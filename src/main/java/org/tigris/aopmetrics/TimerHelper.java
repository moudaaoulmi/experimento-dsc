package org.tigris.aopmetrics;

/**
 * @author <a href="mailto:misto@e-informatyka.pl">Michal Stochmialek</a>
 * @version CVS $Id: TimerHelper.java,v 1.1 2006/04/20 23:37:22 misto Exp $
 */
public class TimerHelper {
	static long start;
	static long last;
	
	public static void checkpoint(String name) {
		if (start == 0) {
			start = System.currentTimeMillis();
			last = System.currentTimeMillis();
		}
		
		long current = System.currentTimeMillis();
		Logger.debug("TIME [" + name + "]: " + (current - start) + " ms, "
					+ " +" + (current - last) + " ms");

		last = System.currentTimeMillis();
	}
}
