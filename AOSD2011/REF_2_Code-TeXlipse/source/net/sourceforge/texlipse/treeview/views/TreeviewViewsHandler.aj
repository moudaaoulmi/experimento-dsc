package net.sourceforge.texlipse.treeview.views;

import net.sourceforge.texlipse.TexlipsePlugin;
import net.sourceforge.texlipse.model.OutlineNode;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;

public privileged aspect TreeviewViewsHandler {

	pointcut internalSelectionChangedHandler(): execution(private void TexOutlineTreeView.internalSelectionChanged(OutlineNode,
			FileEditorInput) );

	declare soft: PartInitException: internalSelectionChangedHandler();
	declare soft: BadLocationException: internalSelectionChangedHandler();

	void around(TexOutlineTreeView tex): internalSelectionChangedHandler() && this(tex){
		try {
			proceed(tex);
		} catch (PartInitException e) {
			TexlipsePlugin.log("Can't open editor.", e);
		} catch (BadLocationException e) {
			tex.editor.resetHighlightRange();
		}
	}

}
