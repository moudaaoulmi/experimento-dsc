package org.tigris.aopmetrics.export;

import org.tigris.aopmetrics.results.ProjectMeasurements;

/**
 * @author misto
 */
public interface Exporter {
	public abstract void export(ProjectMeasurements project, String file) throws ExporterException;
	public abstract String export(ProjectMeasurements project) throws ExporterException;
}