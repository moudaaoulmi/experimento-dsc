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
import net.sourceforge.texlipse.bibparser.lexer.Lexer;
import net.sourceforge.texlipse.bibparser.parser.Parser;
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

public privileged aspect GeneralExceptionHandler {

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
	pointcut getLineHandler(): execution(public String TexSelections.getLine(int));

	//9
	pointcut getCompleteLinesHandler(): execution(public String TexSelections.getCompleteLines());

	//10
	pointcut internalUncommentHandler(): execution(private void TexUncomment.internalUncomment(StringBuffer));

	//11
	pointcut lexerHandler(): staticinitialization(Lexer);

	//12
	pointcut parserHandler(): staticinitialization(Parser);
	
	//13
	pointcut ExternalProgram_internalRunHandler1(): execution(private void ExternalProgram.internalRun(Thread));

	//14
	pointcut ExternalProgram_internalRunHandler2(): execution(private void ExternalProgram.internalRun(Thread, Thread));

	//15
	pointcut ExternalProgram_internalRunHandler3(): execution(private void ExternalProgram.internalRun());

	//16
	pointcut matchHandler(): execution(public IRegion TexPairMatcher.match(IDocument, int) );
	
	//17
	pointcut doWrapBHandler(): execution(public void HardLineWrap.doWrapB(IDocument, DocumentCommand , int));
	
	//18
	pointcut smartIndentAfterBraceHandler(): execution(private void TexAutoIndentStrategy.smartIndentAfterBrace(IDocument,DocumentCommand) );
	
	//19
	pointcut smartIndentAfterNewLineHandler(): execution(private void TexAutoIndentStrategy.smartIndentAfterNewLine(IDocument,
            DocumentCommand) );

	//20
	pointcut internalCreateImageCompHandler(): execution(private void TexInformationControl.internalCreateImageComp(PaintEvent));
	
	
	//21
	pointcut createErrorMarkerHandler(): execution(public void MarkerHandler.createErrorMarker(IResource, String,int));

	//22
	pointcut internalClearTaskMarkersHandler(): execution(private void MarkerHandler.internalClearTaskMarkers(IResource));

	//23
	pointcut clearProblemMarkersHandler(): execution(public void MarkerHandler.clearProblemMarkers(IResource));

	//24
	pointcut internalClearErrorMarkersHandler(): execution(private void MarkerHandler.internalClearErrorMarkers(IResource));

	//25
	pointcut internalAddFatalErrorHandler(): execution(private void MarkerHandler.internalAddFatalError(String, IResource));

	//26
	pointcut internalCreateMarkersHandler(): execution(private void MarkerHandler.internalCreateMarkers(String,IResource, IDocument, ParseErrorMessage));

	//27
	pointcut internalCreateReferencingErrorMarkersHandler(): execution(private void MarkerHandler.internalCreateReferencingErrorMarkers(IResource,IDocument, DocumentReference));

	//28
	pointcut internalFindIFileHandler(): execution(private static void TexProjectParser.internalFindIFile(String,IProject, IFile, KpsewhichRunner));
	
	//29
	pointcut SpellingHandler_internalCheckDocumentSpellingHandler(): execution(private void SpellChecker.internalCheckDocumentSpelling(IDocument, IFile,
			IProgressMonitor));
	
	//30
	pointcut SpellingHandler_internalCreateMarkerHandler(): execution(private void SpellChecker.internalCreateMarker(IResource, String[],
			Map));
	
	//31 
	pointcut SpellingHandler_internalDeleteOldProposalsHandler(): execution(private void SpellChecker.internalDeleteOldProposals(IMarker));

	//32
	pointcut SpellingHandler_internalDeleteOldProposalsHandler2(): execution(private void SpellChecker.internalDeleteOldProposals(IResource));

	//33
	pointcut SpellingHandler_internalGetSpellingProposalHandler2(): execution(private static void SpellChecker.internalGetSpellingProposal(IMarker[], int,int[]));
	
	//34
	pointcut SpellingHandler_internalApplyHandler(): execution(private void SpellingCompletionProposal.internalApply(IDocument));

	//35
	pointcut SpellingHandler_internalApplyHandler2(): execution(private void SpellingCompletionProposal.internalApply());
	
	//36 
	pointcut SpellingHandler_internalRunHandler1(): execution(private void SpellingMarkerResolution.internalRun(int, int));

	//37 
	pointcut SpellingHandler_internalRunHandler2(): execution(private void SpellingMarkerResolution.internalRun(IMarker));

	//38 
	pointcut Templates_internalExtractPrefixHandler(): execution(private void BibTexTemplateCompletion.internalExtractPrefix(ITextViewer, int,
			StringBuffer, char ) );
	//39 
	pointcut Templates_internalSaveProjectTemplateHandler(): execution(private static void ProjectTemplateManager.internalSaveProjectTemplate(IFile, IPath));

	//40 
	pointcut Templates_internalExtractPrefixHandler1(): execution(private void TexTemplateCompletion.internalExtractPrefix(ITextViewer, int,
			StringBuffer, char));

	//41 
	pointcut internalGetChildrenHandler(): execution(private Object[] KpathseaProvider.FileType.internalGetChildren(KpsewhichRunner));

	//42
	pointcut FileLocationServer_internalRunHandler(): execution(private void FileLocationServer.internalRun() );
	
	//43
	pointcut TexLaunchConfigurationTab_initializeFromHandler(): execution(public void TexLaunchConfigurationTab.initializeFrom(ILaunchConfiguration));
	
	//44
	pointcut TexLaunchConfigurationTab_setDefaultsHandler(): execution(public void TexLaunchConfigurationTab.setDefaults(ILaunchConfigurationWorkingCopy));
	
	//45 
	pointcut internalDragFinishedHandler(): execution(private void TexOutlineDNDAdapter.internalDragFinished(int));
	
	//46 
	pointcut internalSaveProjectPropertiesHandler1(): execution(private static void TexlipseProperties.internalSaveProjectProperties(IFile,NullProgressMonitor, ByteArrayInputStream));
	
	declare soft: CoreException: internalGetChildrenHandler();
	
	declare soft: InterruptedException: ExternalProgram_internalRunHandler1()||ExternalProgram_internalRunHandler2()||ExternalProgram_internalRunHandler3();
	
	declare soft: Exception: internalRunHandler2() 
		|| internalCommentHandler() 
		|| internalSelectHandler() 
		|| getLineHandler() 
		|| getCompleteLinesHandler() 
		|| internalUncommentHandler()
		|| lexerHandler() 
		|| parserHandler()
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

	private String getMessageText(int pointcutIndex){
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
			textResult = "TexSelections.getLine: ";
			break;
		case 9:
			textResult = "TexSelections.getCompleteLines: ";
			break;
		case 10:
			textResult = "TexUncomment.uncomment(): ";
			break;
		case 11:
			textResult = "The file \"lexer.dat\" is either missing or corrupted.";
			break;
		case 12:
			textResult = "The file \"parser.dat\" is either missing or corrupted.";
			break;
		case 13:
		case 14:
			textResult = "Output scanner interrupted";
			break;
		case 15:
			textResult = "Process interrupted";
			break;
		case 16:
			textResult = "Bad location in TexPairMatcher.match()";
			break;
		case 17:
			textResult = "Problem with hard line wrap";
			break;
		case 18:
		case 19:
			textResult = "TexAutoIndentStrategy:SmartIndentAfterBracket";
			break;
		case 20:
			textResult = "TexInformationControl: ";
			break;
		case 21:
			textResult = "Creating marker";
			break;
		case 22:
			textResult = "Deleting task markers";
			break;
		case 23:
		case 24:
			textResult = "Deleting error markers";
			break;
		case 25:
		case 26:
		case 27:
			textResult = "Creating marker";
			break;
		case 28:
			textResult = "Can't run Kpathsea";
			break;
		case 29: 
			textResult = "Checking spelling on a line";
			break;
		case 30: 	
			textResult = "Adding spelling marker";
			break;
		case 31: 
			textResult = "Deleting marker";
			break;
		case 32: 
			textResult = "Deleting markers";
			break;
		case 33: 
			textResult = "Error while updating Marker";
			break;
		case 34: 
		case 36:
			textResult = "Replacing Spelling Marker";
			break;
		case 35: 
		case 37:
			textResult = "Removing Spelling Marker";
			break;
		case 38: 
			textResult = "TexTemplateCompletion, extractPrefix.";
			break;
		case 39: 
			textResult = "Saving template";
			break;
		case 40: 
			textResult = "BibTemplateCompletion, extractPrefix.";
			break;
		case 41: 
			textResult = "Can't run Kpathsea";
			break;
		case 42: 
			textResult = "Starting server";
			break;
		case 43:
			textResult = "Reading launch configuration";
			break;
		case 44:
			textResult = "Initializing launch configuration";
			break;
		case 45: 
			textResult = "Could not remove drag'n'drop source.";
			break;
		case 46:
			textResult = "Saving project property file";
			break;
		}
		return textResult;
	}
	
	//XXX rethrow
	void around(): lexerHandler()||parserHandler(){
		try {
			proceed();
		} catch (Exception e) {
			String logText = getMessageText(thisEnclosingJoinPointStaticPart.getId());
			throw new RuntimeException(logText);
		}
	}
	
	String around(): getLineHandler()||getCompleteLinesHandler() {
		try {
			return proceed();
		} catch (Exception e) {
			String logText = getMessageText(thisEnclosingJoinPointStaticPart.getId());
			TexlipsePlugin.log(logText, e);
		}
		return "";
	}

	//Handleres que tratam todos os tipos de exceções Exception
	void around(): internalCommentHandler() 
		|| internalSelectHandler()
		|| internalUncommentHandler() 
		|| smartIndentAfterBraceHandler() 
		|| smartIndentAfterNewLineHandler()
		|| internalCreateImageCompHandler()
		{
		try {
			proceed();
		} catch (Exception e) {
			String logText = getMessageText(thisEnclosingJoinPointStaticPart.getId());
			TexlipsePlugin.log(logText, e);
		}
	}
	
	/*
	 Handleres que tratam exceções subclasses de Exception mas:
	 - não as que herdam de RuntimeException.
	 - no método Java não tem throws para uma exceção checada. Ou seja, toda exceção checada é capturada e tratada ali mesmo.
	 - utiliza de objeto de retorno, padrão null, ou valor padrão para tipo primitivo ou void
	 - Os catch são unicos para cada try. ou catch duplicado com mensagens semelhantes
	 OBS: Atenção para um mesmo try com vários catchs com o log, mas mensagens diferentes
	*/
	Object around(): internalRunHandler3() 
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
			|| internalDragFinishedHandler()
	{
		Object result = null;
		try {
			result = proceed();
		} catch (RuntimeException re){
			throw re;
		} catch (Exception e) {
			String logText = getMessageText(thisEnclosingJoinPointStaticPart.getId());
			TexlipsePlugin.log(logText, e);
		}
		return result;
	}
}
