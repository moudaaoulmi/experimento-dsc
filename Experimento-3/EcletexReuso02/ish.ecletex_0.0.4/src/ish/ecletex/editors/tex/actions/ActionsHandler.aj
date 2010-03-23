package ish.ecletex.editors.tex.actions;

import org.aspectj.lang.SoftException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;


public privileged aspect ActionsHandler {
	
	pointcut internalRunHandler(): execution(private void CommentLineAction.internalRun(IDocument, int));
	
	pointcut internalRunHandlerSoft(): execution(public void CommentLineAction.run());
	
	//Declare soft -------------------------------------------------------------------
	
	declare soft: BadLocationException: internalRunHandler();
	
	// Advices -----------------------------------------------------------------------
	
	void around(): internalRunHandler() {
		try {
			proceed();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// return;
			throw new SoftException(e);
		}
	}
	
	void around(): internalRunHandlerSoft(){
		try{
			proceed();
		}catch(SoftException e){
			// return;
		}
	}
	
	
	

}
