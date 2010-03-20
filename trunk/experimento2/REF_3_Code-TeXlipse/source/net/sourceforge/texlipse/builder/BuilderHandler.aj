package net.sourceforge.texlipse.builder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import net.sourceforge.texlipse.TexlipsePlugin;

import org.aspectj.lang.SoftException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import br.upe.dsc.reusable.exception.EmptyBlockAbstractExceptionHandling;

public privileged aspect BuilderHandler extends EmptyBlockAbstractExceptionHandling{

	pointcut internalRunHandler(): execution(private void AbstractBuilder.internalRun());

	pointcut intenalBuildHandler(): execution(private void AbstractBuilder.intenalBuild(IResource, Thread));

	pointcut internalRunHandler2(): execution(private String AbstractProgramRunner.internalRun(String));

	pointcut internalRenameOutputFileHandler(): execution(private void AbstractProgramRunner.internalRenameOutputFile(IContainer) );

	pointcut internalCreateLayoutMarkerHandler(): execution(private static void AbstractProgramRunner.internalCreateLayoutMarker(IResource,
			Integer , String , String ));

	pointcut internalCreateMarkerHandler(): execution(private static void AbstractProgramRunner.internalCreateMarker(IResource,
			Integer, String, int, String));

	pointcut findMarkerHandler(): execution(public static IMarker AbstractProgramRunner.findMarker(IResource, String,
			String));

	pointcut internalParseErrorsHandler(): execution(private Integer BibtexRunner.internalParseErrors(Integer, String,
			int));

	pointcut internalParseErrorLineHandler(): execution(private Integer BibtexRunner.internalParseErrorLine(Integer,String));

	pointcut internalReadOutputHandler(): execution(private void ExternalProgram.internalReadOutput(InputStream, StringWriter));

	pointcut internalRunHandler6(): execution(private String KpsewhichRunner.internalRun(String));

	pointcut scanOutputHandler(): execution(public boolean OutputScanner.scanOutput());

	pointcut internalAskUserInputHandler(): execution(private void OutputScanner.internalAskUserInput());

	pointcut internalAskUserInputHandlerSoft(): execution(private boolean OutputScanner.askUserInput());

	pointcut internalBuildResourceHandler(): execution(private void TexBuilder.internalBuildResource(IResource));

	pointcut internalBuildPartialFileHandler(): execution(private void TexlipseBuilder.internalBuildPartialFile(IFile, Builder));

	pointcut internalReadFileHandler(): execution(private void TexlipseBuilder.internalReadFile( IProgressMonitor,
			BufferedReader, String, StringBuffer) );

	pointcut internalReadFile2Handler(): execution(private void TexlipseBuilder.internalReadFile2(BufferedReader) );

	pointcut internalCheckBuilderSettingsHandler(): execution(private int TexlipseBuilder.internalCheckBuilderSettings(String, int));

	pointcut internalFullBuildHandler(): execution(private IResource TexlipseBuilder.internalFullBuild(IProgressMonitor,
			IProject, IResource) );

	pointcut internalFullBuildHandler2(): execution(private Builder TexlipseBuilder.internalFullBuild(IProject, Builder));

	pointcut internalFullBuildHandler2Soft(): execution(protected void TexlipseBuilder.fullBuild(IProgressMonitor));

	pointcut internalFullBuildHandler3(): execution(private void TexlipseBuilder.internalFullBuild(IResource, Builder));

	pointcut internalFullBuildHandler4(): execution(private void TexlipseBuilder.internalFullBuild(IProgressMonitor,
			IProject, IContainer));

	pointcut internalMoveOutputHandler(): execution(private void TexlipseBuilder.internalMoveOutput(IProgressMonitor,
			IResource, IResource, File, File));

	pointcut internalGetOutputFileDateHandler(): execution(private static IResource TexlipseBuilder.internalGetOutputFileDate(IProject,
			IResource));
	
	public pointcut emptyBlockException(): (internalFullBuildHandler3()) || 
										   (internalReadFileHandler()||internalReadFile2Handler()) ||
										   (internalBuildResourceHandler() ||internalBuildPartialFileHandler()) ||
										   (internalReadOutputHandler()) ||
										   (internalRenameOutputFileHandler()) ||
										   (internalRunHandler());

	declare soft: InterruptedException : internalRunHandler();
	declare soft: Exception : internalRunHandler2()||internalRunHandler6();
	declare soft: CoreException : internalRenameOutputFileHandler()||internalCreateLayoutMarkerHandler()||internalCreateMarkerHandler()||findMarkerHandler()||internalFullBuildHandler()||internalFullBuildHandler2()||internalFullBuildHandler4()||internalGetOutputFileDateHandler();
	declare soft: IOException: internalReadOutputHandler()||scanOutputHandler()||internalAskUserInputHandler()||internalReadFileHandler()||internalReadFile2Handler()||internalMoveOutputHandler();
	declare soft: BuilderCoreException: internalBuildResourceHandler()||internalBuildPartialFileHandler()||internalFullBuildHandler3();

	IResource around(IResource of): internalGetOutputFileDateHandler() && args(*,of) {
		try {
			return proceed(of);
		} catch (CoreException e) {
		}
		return of;
	}

	void around(IProgressMonitor monitor, IResource outputFile, IResource dest,
			File outFile, File destFile) throws CoreException: internalMoveOutputHandler()&&args(monitor,
			outputFile, dest, outFile, destFile){
		try {
			proceed(monitor, outputFile, dest, outFile, destFile);
		} catch (IOException e) {
			// try to delete and move the file
			dest.delete(true, monitor);
			outputFile.move(dest.getFullPath(), true, monitor);
		}
	}

	void around() throws BuilderCoreException : internalFullBuildHandler4() {
		try { // possibly move output & temp files away from the source dir
			proceed();
		} catch (CoreException e) {
			throw new BuilderCoreException(
					TexlipsePlugin
							.stat("Could not write to output file. Please close the output document in your viewer and rebuild."));
		}
	}

//	void around(): internalFullBuildHandler3(){
//		try {
//			proceed();
//		} catch (BuilderCoreException e) {
//		}
//	}

	Builder around(): internalFullBuildHandler2() {
		try {
			return proceed();
		} catch (CoreException e) {
			// can't get builder, so can't build. error reported to the console
			throw new SoftException(e);
		}
	}

	IResource around(IResource resource): internalFullBuildHandler() && args(..,resource) {
		try {
			proceed(resource);
		} catch (CoreException e) {
		}
		return resource;
	}

	int around(int number):internalCheckBuilderSettingsHandler()&&args(*,number) {
		try {
			return proceed(number);
		} catch (NumberFormatException e) {
		}
		return number;
	}

//	void around(): internalReadFileHandler()||internalReadFile2Handler() {
//		try {
//			proceed();
//		} catch (IOException e) {
//		}
//	}

//	void around(): internalBuildResourceHandler() ||internalBuildPartialFileHandler(){
//		try {
//			proceed();
//		} catch (BuilderCoreException ex) {
//		}
//	}

	void around(): internalAskUserInputHandler() {
		try {
			proceed();
		} catch (IOException e) {
			throw new SoftException(e);
		}
	}

	Object around(): internalAskUserInputHandlerSoft()||internalFullBuildHandler2Soft() {
		try {
			return proceed();
		} catch (SoftException e) {
			return false;
		}
	}

	boolean around(): scanOutputHandler(){
		try {
			return proceed();
		} catch (IOException e) {
		}
		return true;
	}

	String around(KpsewhichRunner kpse) throws CoreException : internalRunHandler6() && this(kpse) {
		try {
			return proceed(kpse);
		} catch (Exception e) {
			throw new CoreException(new Status(IStatus.ERROR, TexlipsePlugin
					.getPluginId(), IStatus.ERROR, "Building the project: ", e));
		} finally {
			kpse.extrun.stop();
		}
	}

//	void around(): internalReadOutputHandler(){
//		try {
//			proceed();
//		} catch (IOException e) {
//		}
//	}

	Integer around(Integer lineNumber): (internalParseErrorsHandler()||internalParseErrorLineHandler()) && args(lineNumber,..){
		try {
			return proceed(lineNumber);
		} catch (NumberFormatException e) {
		}
		return lineNumber;
	}

	//XXX rethrow
	Object around(): internalCreateLayoutMarkerHandler()||internalCreateMarkerHandler()||findMarkerHandler() {
		try {
			return proceed();
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}
//
//	void around(): internalRenameOutputFileHandler() {
//		try {
//			proceed();
//		} catch (CoreException e) {
//		}
//	}

	String around(AbstractProgramRunner abs) throws CoreException : internalRunHandler2()&& this(abs) {
		try {
			return proceed(abs);
		} catch (Exception e) {
			throw new CoreException(new Status(IStatus.ERROR, TexlipsePlugin
					.getPluginId(), IStatus.ERROR, "Building the project: ", e));
		} finally {
			abs.extrun.stop();
		}
	}

	void around(AbstractBuilder abs, IResource resource, Thread buildThread): intenalBuildHandler()&&args(resource,buildThread)&& this(abs){
		try {
			proceed(abs, resource, buildThread);
		} finally {
			abs.buildRunning = false;
			try {
				buildThread.join();
			} catch (InterruptedException e) {
				Thread.interrupted();
				abs.monitor.setCanceled(true);
				abs.stopBuild();
			}
		}
	}

//	void around(): internalRunHandler() {
//		try {
//			proceed();
//		} catch (InterruptedException e) {
//		}
//	}

}
