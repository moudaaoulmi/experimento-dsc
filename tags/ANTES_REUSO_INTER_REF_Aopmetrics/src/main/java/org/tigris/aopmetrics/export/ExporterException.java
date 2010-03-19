package org.tigris.aopmetrics.export;

/**
 * @author misto
 */
public class ExporterException extends Exception {
	public ExporterException(String msg) {
		super(msg);
	}
	public ExporterException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
