package org.tigris.aopmetrics;

import java.io.File;
import java.nio.charset.Charset;

import org.tigris.aopmetrics.ajdt.AjdtBuilder;
import org.tigris.aopmetrics.export.Exporter;
import org.tigris.aopmetrics.export.ExporterFactory;
import org.tigris.aopmetrics.results.ProjectMeasurements;
import org.tigris.aopmetrics.source.Project;

public class AopMetricsRunner {
	private AopMetricsOptions options;

	public void execute(AopMetricsOptions options)
			throws AopMetricsRunnerException {
		this.options = options;
		verifyOptions();

		Logger.msg("Building and measuring " + options.getProjectName()
				+ " project.");
		long start = System.currentTimeMillis();

		Project project = buildProject();
		ProjectMeasurements measures = measureProject(project);
		exportMeasurements(measures);

		long end = System.currentTimeMillis();
		Logger.info("Build and measurement time: " + (end - start) / 1000.0
				+ "s");
	}

	private Project buildProject() throws AopMetricsRunnerException {
		AjdtBuilder builder = new AjdtBuilder();

		builder.setClasspath(options.getClasspath());
		builder.setRootClasspath(options.getRootClasspath());
		builder.setProjectName(options.getProjectName());
		builder.setWorkdir(options.getWorkdir().getAbsolutePath());
		builder.setConfigfile(options.getConfigfile().getAbsolutePath());
		builder.setSourceLevel(options.getSourcelevel());
		builder.setCharset(options.getCharset());

		return internalBuildProject(builder);
	}

	private Project internalBuildProject(AjdtBuilder builder)
			throws AopMetricsRunnerException {
		return builder.build();
	}

	private ProjectMeasurements measureProject(Project project) {
		Engine engine = new Engine();
		return engine.calculateMetrics(project);
	}

	private void exportMeasurements(ProjectMeasurements measures)
			throws AopMetricsRunnerException {
		Exporter exporter = ExporterFactory.createExporter(options.getExport());
		if (options.getResultsfile() == null)
			System.out.println(exporter.export(measures));
		else {
			createResultDir();
			exporter.export(measures, options.getResultsfile()
					.getAbsolutePath());
		}
	}

	private void createResultDir() {
		if (options.getResultsfile() != null) {
			File resultsDir = options.getResultsfile().getParentFile();
			if (resultsDir != null && !resultsDir.exists())
				resultsDir.mkdirs();
		}
	}

	private void verifyOptions() throws AopMetricsRunnerException {
		if (options.getWorkdir() == null)
			throw new AopMetricsRunnerException("Workdir must be specified.");
		if (options.getWorkdir() != null && !options.getWorkdir().isDirectory()) {
			if (options.getWorkdir().exists())
				throw new AopMetricsRunnerException(
						"Workdir is not a regular directory.");
			else
				options.getWorkdir().mkdirs();
		}

		if (options.getVerbose() != null)
			Logger.setMode(options.getVerbose());

		if (options.getSourcelevel() != null
				&& !(options.getSourcelevel().equals("1.5")
						|| options.getSourcelevel().equals("1.4") || options
						.getSourcelevel().equals("1.3")))
			throw new AopMetricsRunnerException(
					"Sourcelevel can be one of 1.3, 1.4 or 1.5 values.");

		if (options.getCharset() != null
				&& !Charset.isSupported(this.options.getCharset()))
			throw new AopMetricsRunnerException("Charset '"
					+ this.options.getCharset() + "' isn't supported.");

		if (options.getClasspath() == null)
			options.setClasspath("");

		if (ExporterFactory.createExporter(this.options.getExport()) == null)
			throw new AopMetricsRunnerException("Unknown export type.");

		if (options.getConfigfile() == null) {
			throw new AopMetricsRunnerException("Configfile isn't specified.");
		}
		if (options.getConfigfile() != null
				&& !options.getConfigfile().isFile()) {
			throw new AopMetricsRunnerException(
					"Configfile is not a regular file.");
		}

	}

}
