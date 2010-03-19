package org.tigris.aopmetrics.export;

/**
 * Factory for exporters, maps exporter type to its implementation. 
 * 
 * @author <a href="mailto:misto@e-informatyka.pl">Michal Stochmialek</a>
 * @version CVS $Id: ExporterFactory.java,v 1.1 2006/04/20 23:37:20 misto Exp $
 */
public class ExporterFactory {
	public static Exporter createExporter(String type) {
		if ("xml".equalsIgnoreCase(type) || type == null)
			return new XStreamExporter();
		else if ("xls".equalsIgnoreCase(type))
			return new POIExporter();
		else
			return null;
	}
}
