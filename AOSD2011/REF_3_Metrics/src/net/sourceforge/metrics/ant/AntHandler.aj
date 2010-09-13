package net.sourceforge.metrics.ant;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import net.sourceforge.metrics.internal.xml.MetricsFirstExporter;
import net.sourceforge.metrics.internal.xml.XMLPrintStream;
import org.apache.tools.ant.BuildException;
import org.aspectj.lang.SoftException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;

import exception.ExceptionHandler;
@ExceptionHandler
public privileged aspect AntHandler {

	pointcut executeHandler(): execution(protected void ProjectBuildWorkspaceModifyOperation.execute(IProgressMonitor));

	pointcut internalExecuteHandler(): execution(private void EnableMetricsTask.internalExecute(IProject));

	pointcut internalExecuteHandler2(): execution(private void ExportMetricsTask.internalExecute(MetricsFirstExporter, IJavaProject,
			AntConsoleProgressMonitor));

	pointcut internalExecuteHandler3(): execution(private void ProjectBuild.internalExecute());

	pointcut getJavacErrorCountHandler(): execution(private int ProjectBuild.getJavacErrorCount(IProject,
			AntConsoleProgressMonitor));

	pointcut internalGetErrorOutputStreamHandler(): execution(private XMLPrintStream ProjectBuild.internalGetErrorOutputStream(String));

	pointcut internalValidateAttributesHandler(): execution(private void ProjectBuild.internalValidateAttributes());

	pointcut internalValidateAttributesHandlerSoft(): execution(protected void ProjectBuild.validateAttributes());

	declare soft: CoreException: executeHandler()||getJavacErrorCountHandler()||internalExecuteHandler();
	declare soft: InvocationTargetException: internalExecuteHandler2();
	declare soft: Exception: internalExecuteHandler3();
	declare soft: FileNotFoundException: internalGetErrorOutputStreamHandler();
	declare soft: IOException: internalValidateAttributesHandler();

	void around(): internalValidateAttributesHandlerSoft() {
		try {
			proceed();
		} catch (SoftException e) {
			// nada
		}
	}

	void around(ProjectBuild pb): internalValidateAttributesHandler() && this(pb) {
		try {
			proceed(pb);
		} catch (IOException e) {
			pb.displayError("Could not initialize compile error outputfile");
			// return;
			throw new SoftException(e);
		}
	}

	XMLPrintStream around(ProjectBuild pb): internalGetErrorOutputStreamHandler()&& this(pb) {
		try {
			return proceed(pb);
		} catch (FileNotFoundException e) {
			pb.displayError("Could not open error outputfile "
					+ pb.compileErrorsOut.getAbsolutePath());
			return null;
		}
	}

	int around(ProjectBuild pb): getJavacErrorCountHandler()&& this(pb) {
		try {
			return proceed(pb);
		} catch (CoreException e) {
			pb.displayError("CoreException: " + e.getMessage());
		}
		return pb.UNKNOWN_ERRORS;
	}

	void around(ProjectBuild pb): internalExecuteHandler3() && this(pb) {
		try {
			proceed(pb);
		} catch (BuildException x) {
			throw x;
		} catch (Exception e) {
			pb.displayError(pb.TASKNAME + pb.projectName + " Exception="
					+ e.getMessage());
		}
	}

	void around(ExportMetricsTask exp): internalExecuteHandler2() && this(exp) {
		try {
			proceed(exp);
		} catch (InvocationTargetException e) {
			exp.displayError("Error exporting metrics" + e.getMessage());
		}
	}

	void around(EnableMetricsTask ena): internalExecuteHandler() && this(ena) {
		try {
			proceed(ena);
		} catch (CoreException e) {
			ena.displayError(ena.TASKNAME
					+ " Could not enable the metrics for the given project.");
		}
	}

	void around(IProgressMonitor monitor) throws CoreException : executeHandler() && args(monitor) {
		try {
			proceed(monitor);
		} catch (CoreException e) {
			throw e;
		} finally {
			monitor.done();
		}
	}

}
