package org.tigris.aopmetrics.metrics;

import org.tigris.aopmetrics.results.Measurement;
import org.tigris.aopmetrics.source.MetricsSource;
import org.tigris.aopmetrics.source.Project;


public interface MetricsCalculator {
	Measurement[] calculate(MetricsSource source, Project project) throws InvalidMetricsSourceException;  
}
