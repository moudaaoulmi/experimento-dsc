package net.sourceforge.texlipse.exception;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import net.sourceforge.texlipse.TexlipsePlugin;
import net.sourceforge.texlipse.actions.OpenDeclarationAction;
import net.sourceforge.texlipse.actions.PartialBuildAction;
import net.sourceforge.texlipse.actions.PreviewAction;
import net.sourceforge.texlipse.actions.TexComment;
import net.sourceforge.texlipse.actions.TexCorrectIndentationAction;
import net.sourceforge.texlipse.actions.TexHardLineWrapAction;
import net.sourceforge.texlipse.actions.TexInsertMathSymbolAction;
import net.sourceforge.texlipse.actions.TexSelections;
import net.sourceforge.texlipse.actions.TexUncomment;
import net.sourceforge.texlipse.builder.ExternalProgram;
import net.sourceforge.texlipse.builder.KpsewhichRunner;
import net.sourceforge.texlipse.editor.HardLineWrap;
import net.sourceforge.texlipse.editor.TexAutoIndentStrategy;
import net.sourceforge.texlipse.editor.TexCompletionProposal;
import net.sourceforge.texlipse.editor.TexEditor;
import net.sourceforge.texlipse.editor.TexPairMatcher;
import net.sourceforge.texlipse.editor.hover.TexInformationControl;
import net.sourceforge.texlipse.model.DocumentReference;
import net.sourceforge.texlipse.model.MarkerHandler;
import net.sourceforge.texlipse.model.ParseErrorMessage;
import net.sourceforge.texlipse.model.TexProjectParser;
import net.sourceforge.texlipse.outline.TexOutlineDNDAdapter;
import net.sourceforge.texlipse.properties.TexlipseProperties;
import net.sourceforge.texlipse.spelling.SpellChecker;
import net.sourceforge.texlipse.spelling.SpellingCompletionProposal;
import net.sourceforge.texlipse.spelling.SpellingMarkerResolution;
import net.sourceforge.texlipse.templates.BibTexTemplateCompletion;
import net.sourceforge.texlipse.templates.ProjectTemplateManager;
import net.sourceforge.texlipse.templates.TexTemplateCompletion;
import net.sourceforge.texlipse.ui.KpathseaProvider;
import net.sourceforge.texlipse.viewer.TexLaunchConfigurationTab;
import net.sourceforge.texlipse.viewer.util.FileLocationServer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.ui.PartInitException;

import br.upe.dsc.reusable.exception.ILogObject;
import br.upe.dsc.reusable.exception.LogAbstractHandler;


public privileged aspect GeneralLogExceptionHandling extends LogAbstractHandler {
	
	public pointcut checkedExceptionLog(): internalRunHandler3() 
			|| internalRunHandler4() 
			|| internalRunHandler5()
			|| internalRunHandler2()
			|| runHandler()
			|| internalRunHandler()
			|| ExternalProgram_internalRunHandler1()
			|| ExternalProgram_internalRunHandler2()
			|| ExternalProgram_internalRunHandler3()
			|| matchHandler()
			|| doWrapBHandler()
			|| createErrorMarkerHandler() 
			|| internalClearTaskMarkersHandler()
			|| clearProblemMarkersHandler()
			|| internalClearErrorMarkersHandler()
			|| internalAddFatalErrorHandler()
			|| internalCreateMarkersHandler() 
			|| internalCreateReferencingErrorMarkersHandler()
			|| internalFindIFileHandler()
			|| SpellingHandler_internalRunHandler2()
			|| SpellingHandler_internalRunHandler1()
			|| SpellingHandler_internalApplyHandler2()
			|| SpellingHandler_internalApplyHandler()
			|| SpellingHandler_internalGetSpellingProposalHandler2()
			|| SpellingHandler_internalDeleteOldProposalsHandler2()
			|| SpellingHandler_internalDeleteOldProposalsHandler()
			|| SpellingHandler_internalCreateMarkerHandler()
			|| SpellingHandler_internalCheckDocumentSpellingHandler()
			|| Templates_internalExtractPrefixHandler1()
			|| Templates_internalSaveProjectTemplateHandler()
			|| Templates_internalExtractPrefixHandler()
			|| internalGetChildrenHandler() 
			|| FileLocationServer_internalRunHandler() 
			|| TexLaunchConfigurationTab_initializeFromHandler()
			|| TexLaunchConfigurationTab_setDefaultsHandler()
			|| internalDragFinishedHandler();
	
	public pointcut exceptionLog():internalCommentHandler() 
			|| internalSelectHandler()
			|| internalUncommentHandler() 
			|| smartIndentAfterBraceHandler() 
			|| smartIndentAfterNewLineHandler()
			|| internalCreateImageCompHandler();

	//0
	pointcut internalRunHandler(): execution(private void OpenDeclarationAction.internalRun(TexEditor, IResource) );

	//1
	pointcut internalRunHandler2(): execution(private void PartialBuildAction.internalRun(IProject) );

	//2
	pointcut runHandler(): execution(public void PreviewAction.run(IAction) );

	//3
	pointcut internalCommentHandler(): execution(private void TexComment.internalComment(StringBuffer) );

	//4
	pointcut internalRunHandler3(): execution(private void TexCorrectIndentationAction.internalRun() );

	//5
	pointcut internalRunHandler4(): execution(private void TexHardLineWrapAction.internalRun(TexSelections) );

	//6
	pointcut internalRunHandler5(): execution(private void TexInsertMathSymbolAction.internalRun(ITextSelection, IDocument,TexCompletionProposal) );

	//7
	pointcut internalSelectHandler(): execution(private void TexSelections.internalSelect() );

	//8
	pointcut internalUncommentHandler(): execution(private void TexUncomment.internalUncomment(StringBuffer));

	//9
	pointcut ExternalProgram_internalRunHandler1(): execution(private void ExternalProgram.internalRun(Thread));

	//10
	pointcut ExternalProgram_internalRunHandler2(): execution(private void ExternalProgram.internalRun(Thread, Thread));

	//11
	pointcut ExternalProgram_internalRunHandler3(): execution(private void ExternalProgram.internalRun());

	//12
	pointcut matchHandler(): execution(public IRegion TexPairMatcher.match(IDocument, int) );
	
	//13
	pointcut doWrapBHandler(): execution(public void HardLineWrap.doWrapB(IDocument, DocumentCommand , int));
	
	//14
	pointcut smartIndentAfterBraceHandler(): execution(private void TexAutoIndentStrategy.smartIndentAfterBrace(IDocument,DocumentCommand) );
	
	//15
	pointcut smartIndentAfterNewLineHandler(): execution(private void TexAutoIndentStrategy.smartIndentAfterNewLine(IDocument,
            DocumentCommand) );

	//16
	pointcut internalCreateImageCompHandler(): execution(private void TexInformationControl.internalCreateImageComp(PaintEvent));
	
	
	//17
	pointcut createErrorMarkerHandler(): execution(public void MarkerHandler.createErrorMarker(IResource, String,int));

	//18
	pointcut internalClearTaskMarkersHandler(): execution(private void MarkerHandler.internalClearTaskMarkers(IResource));

	//19
	pointcut clearProblemMarkersHandler(): execution(public void MarkerHandler.clearProblemMarkers(IResource));

	//20
	pointcut internalClearErrorMarkersHandler(): execution(private void MarkerHandler.internalClearErrorMarkers(IResource));

	//21
	pointcut internalAddFatalErrorHandler(): execution(private void MarkerHandler.internalAddFatalError(String, IResource));

	//22
	pointcut internalCreateMarkersHandler(): execution(private void MarkerHandler.internalCreateMarkers(String,IResource, IDocument, ParseErrorMessage));

	//23
	pointcut internalCreateReferencingErrorMarkersHandler(): execution(private void MarkerHandler.internalCreateReferencingErrorMarkers(IResource,IDocument, DocumentReference));

	//24
	pointcut internalFindIFileHandler(): execution(private static void TexProjectParser.internalFindIFile(String,IProject, IFile, KpsewhichRunner));
	
	//25
	pointcut SpellingHandler_internalCheckDocumentSpellingHandler(): execution(private void SpellChecker.internalCheckDocumentSpelling(IDocument, IFile,
			IProgressMonitor));
	
	//26
	pointcut SpellingHandler_internalCreateMarkerHandler(): execution(private void SpellChecker.internalCreateMarker(IResource, String[],
			Map));
	
	//27
	pointcut SpellingHandler_internalDeleteOldProposalsHandler(): execution(private void SpellChecker.internalDeleteOldProposals(IMarker));

	//28
	pointcut SpellingHandler_internalDeleteOldProposalsHandler2(): execution(private void SpellChecker.internalDeleteOldProposals(IResource));

	//29
	pointcut SpellingHandler_internalGetSpellingProposalHandler2(): execution(private static void SpellChecker.internalGetSpellingProposal(IMarker[], int,int[]));
	
	//30
	pointcut SpellingHandler_internalApplyHandler(): execution(private void SpellingCompletionProposal.internalApply(IDocument));

	//31
	pointcut SpellingHandler_internalApplyHandler2(): execution(private void SpellingCompletionProposal.internalApply());
	
	//32 
	pointcut SpellingHandler_internalRunHandler1(): execution(private void SpellingMarkerResolution.internalRun(int, int));

	//33 
	pointcut SpellingHandler_internalRunHandler2(): execution(private void SpellingMarkerResolution.internalRun(IMarker));

	//34
	pointcut Templates_internalExtractPrefixHandler(): execution(private void BibTexTemplateCompletion.internalExtractPrefix(ITextViewer, int,
			StringBuffer, char ) );
	//35 
	pointcut Templates_internalSaveProjectTemplateHandler(): execution(private static void ProjectTemplateManager.internalSaveProjectTemplate(IFile, IPath));

	//36
	pointcut Templates_internalExtractPrefixHandler1(): execution(private void TexTemplateCompletion.internalExtractPrefix(ITextViewer, int,
			StringBuffer, char));

	//37 
	pointcut internalGetChildrenHandler(): execution(private Object[] KpathseaProvider.FileType.internalGetChildren(KpsewhichRunner));

	//38
	pointcut FileLocationServer_internalRunHandler(): execution(private void FileLocationServer.internalRun() );
	
	//39
	pointcut TexLaunchConfigurationTab_initializeFromHandler(): execution(public void TexLaunchConfigurationTab.initializeFrom(ILaunchConfiguration));
	
	//40
	pointcut TexLaunchConfigurationTab_setDefaultsHandler(): execution(public void TexLaunchConfigurationTab.setDefaults(ILaunchConfigurationWorkingCopy));
	
	//41 
	pointcut internalDragFinishedHandler(): execution(private void TexOutlineDNDAdapter.internalDragFinished(int));
	
	//42
	pointcut internalSaveProjectPropertiesHandler1(): execution(private static void TexlipseProperties.internalSaveProjectProperties(IFile,NullProgressMonitor, ByteArrayInputStream));
	
	declare soft: CoreException: internalGetChildrenHandler();
	
	declare soft: InterruptedException: ExternalProgram_internalRunHandler1()||ExternalProgram_internalRunHandler2()||ExternalProgram_internalRunHandler3();
	
	declare soft: Exception: internalRunHandler2() 
		|| internalCommentHandler() 
		|| internalSelectHandler() 
		|| internalUncommentHandler()
		|| smartIndentAfterBraceHandler() 
		|| smartIndentAfterNewLineHandler()
		|| internalCreateImageCompHandler();
	
	declare soft: PartInitException: internalRunHandler();

	declare soft: BadLocationException: internalRunHandler3() 
		|| internalRunHandler4() 
		|| internalRunHandler5()
		|| matchHandler() 
		|| doWrapBHandler()
		|| internalCreateMarkersHandler() 
		|| internalCreateReferencingErrorMarkersHandler()
		|| SpellingHandler_internalCheckDocumentSpellingHandler() 
		|| SpellingHandler_internalApplyHandler() 
		|| SpellingHandler_internalRunHandler1()
		|| Templates_internalExtractPrefixHandler() 
		|| Templates_internalExtractPrefixHandler1()
		|| internalDragFinishedHandler();
	
	declare soft: CoreException: runHandler()
		|| createErrorMarkerHandler()
		|| internalClearTaskMarkersHandler()
		|| clearProblemMarkersHandler()
		|| internalClearErrorMarkersHandler()
		|| internalAddFatalErrorHandler()
		|| internalCreateMarkersHandler()
		|| internalCreateReferencingErrorMarkersHandler()
		|| internalFindIFileHandler()
		|| SpellingHandler_internalCreateMarkerHandler() 
		|| SpellingHandler_internalDeleteOldProposalsHandler() 
		|| SpellingHandler_internalDeleteOldProposalsHandler2() 
		|| SpellingHandler_internalGetSpellingProposalHandler2() 
		|| SpellingHandler_internalApplyHandler2() 
		|| SpellingHandler_internalRunHandler2()
		|| internalGetChildrenHandler()
		|| TexLaunchConfigurationTab_initializeFromHandler()
		|| TexLaunchConfigurationTab_setDefaultsHandler()
		|| internalSaveProjectPropertiesHandler1(); 

	declare soft: IOException: Templates_internalSaveProjectTemplateHandler()
		|| FileLocationServer_internalRunHandler();

	public String getMessageText(int pointcutIndex){
		String textResult = "";
		switch(pointcutIndex){
		case 0:
			textResult = "Open declaration:";
			break;
		case 1:
			textResult = "Error while deleting temp files";
			break;
		case 2:
			textResult = "Launching viewer";
			break;
		case 3:
			textResult = "TexComment.comment(): ";
			break;
		case 4:
		case 5:
			textResult = "TexCorrectIndentationAction.run";
			break;
		case 6:
			textResult = "Error while trying to insert command";
			break;
		case 7:
			textResult = "TexSelections.select(): ";
			break;
		case 8:
			textResult = "TexUncomment.uncomment(): ";
			break;
		case 9:
		case 10:
			textResult = "Output scanner interrupted";
			break;
		case 11:
			textResult = "Process interrupted";
			break;
		case 12:
			textResult = "Bad location in TexPairMatcher.match()";
			break;
		case 13:
			textResult = "Problem with hard line wrap";
			break;
		case 14:
		case 15:
			textResult = "TexAutoIndentStrategy:SmartIndentAfterBracket";
			break;
		case 16:
			textResult = "TexInformationControl: ";
			break;
		case 17:
			textResult = "Creating marker";
			break;
		case 18:
			textResult = "Deleting task markers";
			break;
		case 19:
		case 20:
			textResult = "Deleting error markers";
			break;
		case 21:
		case 22:
		case 23:
			textResult = "Creating marker";
			break;
		case 24:
			textResult = "Can't run Kpathsea";
			break;
		case 25: 
			textResult = "Checking spelling on a line";
			break;
		case 26: 	
			textResult = "Adding spelling marker";
			break;
		case 27: 
			textResult = "Deleting marker";
			break;
		case 28: 
			textResult = "Deleting markers";
			break;
		case 29: 
			textResult = "Error while updating Marker";
			break;
		case 30: 
		case 32:
			textResult = "Replacing Spelling Marker";
			break;
		case 31: 
		case 33:
			textResult = "Removing Spelling Marker";
			break;
		case 34: 
			textResult = "TexTemplateCompletion, extractPrefix.";
			break;
		case 35: 
			textResult = "Saving template";
			break;
		case 36: 
			textResult = "BibTemplateCompletion, extractPrefix.";
			break;
		case 37: 
			textResult = "Can't run Kpathsea";
			break;
		case 38: 
			textResult = "Starting server";
			break;
		case 39:
			textResult = "Reading launch configuration";
			break;
		case 40:
			textResult = "Initializing launch configuration";
			break;
		case 41: 
			textResult = "Could not remove drag'n'drop source.";
			break;
		case 42:
			textResult = "Saving project property file";
			break;
		}
		return textResult;
	}
	
	public ILogObject getLogObject(){
		return TexlipsePlugin.getLogInstance();
	}
	
}
