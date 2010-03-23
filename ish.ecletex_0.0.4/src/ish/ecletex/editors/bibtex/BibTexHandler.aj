package ish.ecletex.editors.bibtex;

import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.PartInitException;

public privileged aspect BibTexHandler {

	pointcut createSourceViewerHandler(): execution(void BibTeXEditor.createSourceViewer());

	declare soft: PartInitException: createSourceViewerHandler();	

	void around(BibTeXEditor bibTeX): createSourceViewerHandler() && this(bibTeX) {
		try {
			proceed(bibTeX);
		} catch (PartInitException e) {
			ErrorDialog.openError(bibTeX.getSite().getShell(),
					"Error creating nested TeXEditor", null, e.getStatus());
		}
	}	
	

}
