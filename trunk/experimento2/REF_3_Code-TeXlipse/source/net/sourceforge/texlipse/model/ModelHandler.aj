package net.sourceforge.texlipse.model;

import java.io.IOException;
import java.util.ArrayList;

import net.sourceforge.texlipse.TexlipsePlugin;
import net.sourceforge.texlipse.bibparser.BibParser;
import net.sourceforge.texlipse.builder.KpsewhichRunner;
import net.sourceforge.texlipse.editor.TexDocumentParseException;

import org.aspectj.lang.SoftException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;

import br.upe.dsc.reusable.exception.EmptyBlockAbstractExceptionHandling;

public privileged aspect ModelHandler extends EmptyBlockAbstractExceptionHandling{

	pointcut runHandler(): execution(protected IStatus TexDocumentModel.ParseJob.run(IProgressMonitor));

	pointcut internalRunHandler(): execution(private ArrayList TexDocumentModel.ParseJob.internalRun(IProgressMonitor,ArrayList));

	pointcut internalRunHandler1(): execution(private void TexDocumentModel.ParseJob.internalRun());

	pointcut internalRunHandler2(): execution(private void TexDocumentModel.ParseJob.internalRun(IProgressMonitor));

	pointcut runInUIThreadHandler(): execution(public IStatus TexDocumentModel.PostParseJob.runInUIThread(IProgressMonitor));

	pointcut internalDocumentChangedHandler(): execution(private void TexDocumentModel.internalDocumentChanged());

	pointcut internalDoParseHandler(): execution(private void TexDocumentModel.internalDoParse());

	pointcut internalUpdateDocumentPositionsHandler(): execution(private void TexDocumentModel.internalUpdateDocumentPositions(IDocument));

	pointcut internalHandler(): execution(private void TexDocumentModel.internal(IResource, IProject, IPath,KpsewhichRunner, String));

	pointcut internalAddNodePositionHandler(): execution(private Position TexDocumentModel.internalAddNodePosition(OutlineNode,IDocument, int, int, Position));

	pointcut internalHandler1(): execution(private void TexDocumentModel.internal(String,IPath, String, BibParser));

	pointcut internalCreateProjectDatastructsHandler(): execution(private void TexDocumentModel.internalCreateProjectDatastructs(IResource[],IProject, IFile, int));

	pointcut internalCreateProjectDatastructs1Handler(): execution(private void TexDocumentModel.internalCreateProjectDatastructs1(IProject,IResource[], IFile, int));

	pointcut internalReadFileHandler(): execution(private void TexProjectParser.internalReadFile(IFile, StringBuilder));
	
	public pointcut emptyBlockException(): internalUpdateDocumentPositionsHandler();

	declare soft: CoreException : internalHandler()||internalReadFileHandler();
	declare soft: BadLocationException: internalAddNodePositionHandler();
	declare soft: Exception: runHandler()||runInUIThreadHandler();
	declare soft: TexDocumentParseException: internalRunHandler();
	declare soft: InterruptedException: internalRunHandler1();
	declare soft: IOException: internalDoParseHandler()||internalHandler1()||internalCreateProjectDatastructsHandler();
	declare soft: BadPositionCategoryException: internalUpdateDocumentPositionsHandler()||internalAddNodePositionHandler();

	void around() throws IOException : internalReadFileHandler(){
		try {
			proceed();
		} catch (CoreException e) {
			// This should be very rare...
			throw new IOException(e.getMessage());
		}
	}

	void around(): internalCreateProjectDatastructs1Handler() {
		try {
			proceed(); 
		} catch (SoftException ioe) {
			// continue;
		}
	}

	void around(IResource[] files, int i): internalCreateProjectDatastructsHandler() && args(files,..,i) {
		try {
			proceed(files, i);
		} catch (IOException ioe) {
			//XXX LOG  - nao dah p gerenalizar totalmente
			TexlipsePlugin.log("Unable to open file " + files[i].getFullPath()
					+ " for parsing", ioe);
		}
	}

	void around(): internalHandler() {
		try {
			proceed();
		} catch (SoftException e) {
			// continue...
		} catch (CoreException ce) {
			//XXX LOG  - nao dah p gerenalizar totalmente
			TexlipsePlugin.log("Can't run Kpathsea", ce);
		}
	}

	void around(String filepath): internalHandler1() && args(filepath,..) {
		try {
			proceed(filepath);
		} catch (IOException ioe) {
			//XXX LOG  - nao dah p gerenalizar totalmente
			TexlipsePlugin.log("Can't read BibTeX file " + filepath, ioe);
		}
	}

	Position around(): internalAddNodePositionHandler() {
		try {
			return proceed();
		} catch (BadLocationException bpe) {
			throw new OperationCanceledException();
		} catch (BadPositionCategoryException bpce) {
			throw new OperationCanceledException();
		}
	}

//	void around(): internalUpdateDocumentPositionsHandler() {
//		try {
//			proceed();
//		} catch (BadPositionCategoryException bpce) {
//			// do nothing, the category will be added again next, it does not
//			// exists the first time
//		}
//	}

	void around() throws TexDocumentParseException : internalDoParseHandler() {
		try {
			proceed();
		} catch (IOException e) {
			TexlipsePlugin.log("Can't read file.", e);
			throw new TexDocumentParseException(e);
		}
	}

	IStatus around(): runInUIThreadHandler() {
		try {
			return proceed();
		} catch (Exception e) {
			// npe when exiting eclipse and saving
			return Status.CANCEL_STATUS;
		}
	}

	IStatus around(): runHandler() {
		try {
			return proceed();
		} catch (Exception e) {
			return Status.CANCEL_STATUS;
		}
	}

	void around():internalRunHandler2()||internalDocumentChangedHandler(){
		try {
			proceed();
		} finally {
			TexDocumentModel.lock.release();
		}
	}

	//XXX rethrow
	void around(): internalRunHandler1() {
		try {
			proceed();
		} catch (InterruptedException e2) {
			// return Status.CANCEL_STATUS;
			throw new SoftException(e2);
		}
	}

	//XXX rethrow
	ArrayList around(): internalRunHandler() {
		try {
			return proceed();
		} catch (TexDocumentParseException e1) {
			// return Status.CANCEL_STATUS;
			throw new SoftException(e1);
		}
	}

}
