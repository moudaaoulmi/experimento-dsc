package net.sourceforge.texlipse.actions;

import net.sourceforge.texlipse.TexlipsePlugin;
import net.sourceforge.texlipse.editor.TexEditor;
import net.sourceforge.texlipse.model.AbstractEntry;

import org.aspectj.lang.SoftException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.PartInitException;

public privileged aspect ActionsHandler {

	pointcut changeHandler(): execution(private void AbstractTexSelectionChange.change() );

	pointcut unchangeHandler(): execution(private void AbstractTexSelectionChange.unchange() );

	pointcut internalRun2Handler(): execution(private void OpenDeclarationAction.internalRun2(TexEditor, String,
			AbstractEntry , IFile ) );

	pointcut internalIdentHandler(): execution(private String[] TexCorrectIndentationAction.internalIdent(IDocument, String,
			String[]));

	pointcut internalIdentHandlerCont(): execution(private void TexCorrectIndentationAction.indent());

	pointcut internalSelectParagraphHandler(): execution(private void TexSelections.internalSelectParagraph());

	declare soft: Exception: changeHandler()|| unchangeHandler()||internalIdentHandler();

	declare soft: BadLocationException: internalSelectParagraphHandler() || internalRun2Handler();

	declare soft: PartInitException: internalRun2Handler();
		
	declare soft: CoreException: internalRun2Handler();
	
	void around(AbstractTexSelectionChange ab): (changeHandler()|| unchangeHandler())&& this(ab) {
		try {
			proceed(ab);
		} catch (Exception e) {
			TexlipsePlugin.log("Wrapping selection inside " + ab.startTag, e);
		}
	}	

	//XXX rethrow
	String[] around(): internalIdentHandler() {
		try {
			return proceed();
		} catch (Exception e) {// for example new empty file
			throw new SoftException(e);
		}
	}

	void around(): internalIdentHandlerCont() {
		try {
			proceed();
		} catch (SoftException e) {
			return;
		}
	}


	void around(): internalSelectParagraphHandler(){
		try {
			proceed();
		} catch (BadLocationException ble) {
		}
	}	
	
	void around(): internalRun2Handler() {
		try {
			proceed();
		} catch (PartInitException e) {
			//XXX LOG - Não Generalizado totalmente
			TexlipsePlugin.log("Jump2Label PartInitException", e);
		} catch (BadLocationException e) {
			//XXX LOG - Não Generalizado totalmente
			TexlipsePlugin.log("Jump2Label BadLocationException", e);
		} catch (CoreException ce) {
			//XXX LOG - Não Generalizado totalmente
			TexlipsePlugin.log("Can't run Kpathsea", ce);
		}
	}
}
