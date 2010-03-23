/*
 * Created on 23-Sep-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex.editors.tex;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.texteditor.BasicTextEditorActionContributor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.RetargetTextEditorAction;


/**
 * @author ish
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TeXActionContributor extends BasicTextEditorActionContributor{
	private RetargetTextEditorAction contentAssist;
	private RetargetTextEditorAction commentAction;



	
	public TeXActionContributor(){
		super();
		contentAssist = new RetargetTextEditorAction(TeXEditorMessages.getResourceBundle(),"ContentAssistProposal.");
		contentAssist.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
		commentAction = new RetargetTextEditorAction(TeXEditorMessages.getResourceBundle(),"CommentAction.");
		
	}
	
	public void init(IActionBars bars) {
		super.init(bars);
		IMenuManager menuManager= bars.getMenuManager();
		IMenuManager editMenu= menuManager.findMenuUsingPath(IWorkbenchActionConstants.M_EDIT);
		if (editMenu != null) {
			editMenu.add(new Separator());
			editMenu.add(contentAssist);
			editMenu.add(commentAction);
		}	
	}
	
	private void doSetActiveEditor(IEditorPart part) {
		super.setActiveEditor(part);

		ITextEditor editor= null;
		if (part instanceof ITextEditor)
			editor= (ITextEditor) part;
		contentAssist.setAction(getAction(editor, "ContentAssistProposal"));
		commentAction.setAction((getAction(editor,"CommentAction")));
	}
	
	public void setActiveEditor(IEditorPart part) {
		super.setActiveEditor(part);
		doSetActiveEditor(part);
	}
	
	public void dispose() {
		doSetActiveEditor(null);
		super.dispose();
	}


}
