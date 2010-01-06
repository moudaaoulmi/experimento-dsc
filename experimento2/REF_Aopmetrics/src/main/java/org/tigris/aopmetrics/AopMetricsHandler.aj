package org.tigris.aopmetrics;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.aspectj.lang.SoftException;
import org.apache.commons.cli.ParseException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileSet;
import org.tigris.aopmetrics.Logger.Mode;
import org.tigris.aopmetrics.ajdt.AjdtBuilder;
import org.tigris.aopmetrics.ajdt.AjdtBuilderException;
import org.tigris.aopmetrics.export.ExporterException;
import org.tigris.aopmetrics.results.ProjectMeasurements;
import org.tigris.aopmetrics.source.Project;

import exception.ExceptionHandler;

@ExceptionHandler
public privileged aspect AopMetricsHandler {

	pointcut internalStartHandler(): execution(private void AopMetricsCLI.internalStart(String[]));

	pointcut internalStartHandlerSoft(): execution(public void AopMetricsCLI.start(String[]));

	pointcut internalStart2Handler(): execution(private void AopMetricsCLI.internalStart2(AopMetricsRunner));

	pointcut internalBuildProjectHandler(): execution( private Project AopMetricsRunner.internalBuildProject(AjdtBuilder));

	pointcut exportMeasurementsHandler(): execution(private void AopMetricsRunner.exportMeasurements(ProjectMeasurements));

	pointcut internalExecuteHandler(): execution(private void AopMetricsTask.internalExecute(AopMetricsRunner));

	pointcut createConfigHandler(): execution(private void AopMetricsTask.createConfig(File, List<FileSet>));

	pointcut createByNameHandler(): execution(static Mode Logger.Mode.createByName(String));

	declare soft: ParseException: internalStartHandler();
	declare soft: AopMetricsRunnerException: internalStart2Handler() || internalExecuteHandler();
	declare soft: AjdtBuilderException: internalBuildProjectHandler();
	declare soft: ExporterException: exportMeasurementsHandler();
	declare soft: IOException: createConfigHandler();

	Mode around(String name): createByNameHandler()&& args(name) {
		try {
			return proceed(name);
		} catch (IllegalArgumentException e) {
			Logger.msg("Unknown logger mode: " + name);
			return Mode.NORMAL;
		}
	}

	void around(): createConfigHandler() {
		try {
			proceed();
		} catch (IOException e) {
			throw new BuildException("Can't create temporary config file: "
					+ e.getMessage());
		}
	}

	void around(): internalExecuteHandler() {
		try {
			proceed();
		} catch (AopMetricsRunnerException e) {
			throw new BuildException(e.getMessage());
		}
	}

	void around() throws AopMetricsRunnerException: exportMeasurementsHandler()	{
		try {
			proceed();
		} catch (ExporterException e) {
			throw new AopMetricsRunnerException(e.getMessage());
		}
	}

	Project around() throws AopMetricsRunnerException: internalBuildProjectHandler(){
		try {
			return proceed();
		} catch (AjdtBuilderException e) {
			throw new AopMetricsRunnerException(e.getMessage());
		}
	}

	void around(): internalStart2Handler(){
		try {
			proceed();
		} catch (AopMetricsRunnerException e) {
			Logger.msg("ERROR: " + e.getMessage());
		}
	}

	void around(): internalStartHandler(){
		try {
			proceed();
		} catch (MissingArgumentException e) {
			Logger.msg("ERROR: Missing argument " + e.getMessage());
			Logger.msg("Please, use '-help' option.");
			// return;
			throw new SoftException(e);
		} catch (MissingOptionException e) {
			Logger.msg("ERROR: Missing option " + e.getMessage());
			Logger.msg("Please, use '-help' option.");
			// return;
			throw new SoftException(e);
		} catch (ParseException e) {
			Logger.msg("ERROR: " + e.getMessage());
			Logger.msg("Please, use '-help' option.");
			// return;
			throw new SoftException(e);
		}

	}

	void around(): internalStartHandlerSoft(){
		try {
			proceed();
		} catch (SoftException e) {
			// nothing
		}
	}

}
