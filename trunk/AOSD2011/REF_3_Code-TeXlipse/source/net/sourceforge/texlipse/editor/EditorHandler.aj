package net.sourceforge.texlipse.editor;

import net.sourceforge.texlipse.TexlipsePlugin;

import org.aspectj.lang.SoftException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPartitioningException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.link.LinkedModeUI.ExitFlags;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.texteditor.spelling.SpellingProblem;

import br.upe.dsc.reusable.exception.EmptyBlockAbstractExceptionHandling;

public privileged aspect EditorHandler extends EmptyBlockAbstractExceptionHandling{

	pointcut internalDoExitHandler(): execution(private ExitFlags BracketInserter.ExitPolicy.internalDoExit(int, IDocument,ExitFlags));

	pointcut isMaskedHandler(): execution(private boolean BracketInserter.ExitPolicy.isMasked(int));

	pointcut internalUpdateHandler(): execution(private void BracketInserter.ExclusivePositionUpdater.internalUpdate(DocumentEvent, int,
			int, int, int));

	pointcut intenalVerifyKeyHandler(): execution(private void BracketInserter.intenalVerifyKey(VerifyEvent, IDocument,
		 int ,  int,char));

	pointcut intenalVerifyKey2Handler(): execution(private int BracketInserter.internalVerifyKey(IDocument, int));

	pointcut intenalVerifyKey3Handler(): execution(private boolean BracketInserter.internalVerifyKey(IDocument,int,
			boolean ));

	pointcut intenalleftHandler(): execution(private void BracketInserter.intenalleft( net.sourceforge.texlipse.editor.BracketInserter.BracketLevel,IDocument));

	pointcut intenalleft2Handler(): execution(private void BracketInserter.internalLeft(IDocument));

//	pointcut doWrapBHandler(): execution(public void HardLineWrap.doWrapB(IDocument, DocumentCommand , int));

	pointcut internalGetHoverInfoHandler(): execution(private String TexAnnotationHover.internalGetHoverInfo(int, IFile));

//	pointcut smartIndentAfterNewLineHandler(): execution(private void TexAutoIndentStrategy.smartIndentAfterNewLine(IDocument,
//            DocumentCommand) );

//	pointcut smartIndentAfterBraceHandler(): execution(private void TexAutoIndentStrategy.smartIndentAfterBrace(IDocument,DocumentCommand) );

	pointcut dropItemHandler(): execution(private void TexAutoIndentStrategy.dropItem(IDocument, DocumentCommand) );

	pointcut internaIitemInsertedHandler(): execution(private boolean TexAutoIndentStrategy.internaIitemInserted(IDocument, DocumentCommand,
			int, StringBuffer, int, String) );

	pointcut internalComputeCompletionProposalsHandler(): execution(private ICompletionProposal[] TexCompletionProcessor.internalComputeCompletionProposals(IDocument, Point));

	pointcut internalComputeCompletionProposals2Handler(): execution(private ICompletionProposal[] TexCompletionProcessor.internalComputeCompletionProposals(ITextViewer, int, IDocument)  );

	pointcut internalDoExitHandler1(): execution(private ExitFlags TexCompletionProposal.ExitPolicy.internalDoExit(int));

	pointcut internalDoExitHandler2(): execution(private void TexCompletionProposal.ExitPolicy.internalDoExit(VerifyEvent, int) );

	pointcut internalDoExitHandler3(): execution(private void TexCompletionProposal.ExitPolicy.internalDoExit2(VerifyEvent, int) );

	pointcut internalGetDocumentHandler(): execution(private IDocument TexDocumentProvider.internalGetDocument(IDocument, IProject) );

	pointcut internalGetIndentationHandler(): execution(private String TexEditorTools.internalGetIndentation(IDocument, int,
			String , int , String));

	pointcut getIndentationHandler(): execution(public String TexEditorTools.getIndentation(IDocument, DocumentCommand));

	pointcut internalGetLineDelimiterHandler(): execution(private String TexEditorTools.internalGetLineDelimiter(IDocument, String));

	pointcut internalGetIndexLineHandler(): execution(private int TexEditorTools.internalGetIndexLine(IDocument,DocumentCommand, boolean, int) );

	pointcut internalGetLineLengthHandler(): execution(private int TexEditorTools.internalGetLineLength(IDocument ,	DocumentCommand, boolean, int, int,	int) );

	pointcut internalGetLineLengthHandlerSoft(): execution(public int TexEditorTools.getLineLength(IDocument, DocumentCommand,	boolean, int) );

	pointcut internalGetStringAtHandler(): execution(private String  TexEditorTools.internalGetStringAt(IDocument,DocumentCommand , boolean, int, int, int,String) );

	pointcut internalUpdateHandler2(): execution(private void TexlipseAnnotationUpdater.internalUpdate(IDocument, IAnnotationModel, ISelection)  );

	pointcut internalUpdateHandler2Soft(): execution(private void TexlipseAnnotationUpdater.update(ISourceViewer) );

	pointcut internalComputeCompletionProposalsHandler2(): execution(private String TexMathCompletionProcessor.internalComputeCompletionProposals(int,String, IDocument) );

	pointcut internalComputeCompletionProposalsSoft(): execution(public ICompletionProposal[] TexMathCompletionProcessor.computeCompletionProposals(ITextViewer,int));

//	pointcut matchHandler(): execution(public IRegion TexPairMatcher.match(IDocument, int) );

	pointcut internalDocumentChangedHandler(): execution(private void TexQuoteListener.internalDocumentChanged(ITextSelection));

	pointcut internalDocumentChangedHandlerSoft(): execution(public void TexQuoteListener.documentChanged(DocumentEvent));

	pointcut internalAcceptHandler(): execution(private void TeXSpellingReconcileStrategy.TeXSpellingProblemCollector.internalAccept(int) );

	pointcut internalAcceptHandlerSoft(): execution(public void TeXSpellingReconcileStrategy.TeXSpellingProblemCollector.accept(SpellingProblem));

	pointcut applyHandler(): execution(public void TexCompletionProposal.apply(IDocument));

	pointcut internalHandler(): execution(private char BracketInserter.internal(IDocument,int, char));

	pointcut internalHandler1(): execution(private char BracketInserter.internal1(IDocument,int,char));
	
	public pointcut emptyBlockException(): (applyHandler()) || 
										   (internalAcceptHandler()) || 
										   (internalDocumentChangedHandler()) || 
										   (internalUpdateHandler2()) || 
										   (internalDoExitHandler3()) || 
										   (internalDoExitHandler2()) || 
										   (internalComputeCompletionProposalsHandler()) || 
										   (internalGetHoverInfoHandler()) || 
										   (intenalleft2Handler()) || 
										   (intenalleftHandler()) || 
										   (intenalVerifyKeyHandler()) || 
										   (internalUpdateHandler());

	declare soft: BadLocationException: internalDoExitHandler()||isMaskedHandler()||internalUpdateHandler()||intenalVerifyKeyHandler()||intenalVerifyKey3Handler()||intenalleftHandler()||internalComputeCompletionProposalsHandler()||internalComputeCompletionProposals2Handler()||internalDoExitHandler1()||internalDoExitHandler2()||internalDoExitHandler3()||internalGetLineDelimiterHandler()||internalGetIndexLineHandler()||internalGetLineLengthHandler()||internalGetStringAtHandler()||internalUpdateHandler2()||internalComputeCompletionProposalsHandler2()|| internalDocumentChangedHandler()||internalAcceptHandler()||applyHandler()||internalHandler()||internalHandler1();
	
	declare soft: BadPositionCategoryException: internalUpdateHandler()||intenalVerifyKeyHandler()||intenalleft2Handler();
	declare soft: BadPartitioningException: intenalVerifyKey2Handler();
	declare soft: CoreException: internalGetHoverInfoHandler()||internalGetDocumentHandler();
	declare soft: Exception: dropItemHandler()||internaIitemInsertedHandler()||internalGetIndentationHandler()||getIndentationHandler();

	char around(char next): internalHandler1() && args(..,next){
		try {
			return proceed(next);
		} catch (BadLocationException e) {
			// Could happen if this is the beginning or end of a document
		}
		return next;
	}

	char around(char last): internalHandler() && args(..,last) {
		try {
			return proceed(last);
		} catch (BadLocationException e) {
			// Could happen if this is the beginning or end of a document
		}
		return last;
	}

//	void around(): applyHandler() {
//		try {
//			proceed();
//		} catch (BadLocationException x) {
//		}
//	}

	void around(): internalAcceptHandlerSoft() {
		try {
			proceed();
		} catch (SoftException e) {
			return;
		}
	}

//	void around(): internalAcceptHandler() {
//		try {
//			proceed();
//		} catch (BadLocationException e) {
//			// Should not happen
//		}
//	}

	void around(): internalDocumentChangedHandlerSoft() {
		try {
			proceed();
		} catch (SoftException e) {
			return;
		}
	}

//	void around(): internalDocumentChangedHandler() {
//		try {
//			proceed();
//		} catch (BadLocationException e) {
//		}
//	}

	ICompletionProposal[] around(): internalComputeCompletionProposalsSoft() {
		try {
			return proceed();
		} catch (BadLocationException e) {
			return new ICompletionProposal[0];
		}
	}

	String around(): internalComputeCompletionProposalsHandler2(){
		try {
			return proceed();
		} catch (BadLocationException e) {
			TexlipsePlugin.log("TexCompletionProcessor: ", e);
			throw new SoftException(null);
		}
	}

	void around(): internalUpdateHandler2Soft() {
		try {
			proceed();
		} catch (SoftException e) {
			return;
		}
	}

//	void around(): internalUpdateHandler2() {
//		try {
//			proceed();
//		} catch (BadLocationException ex) {
//			// Do not inform the user cause this is only a decorator
//		}
//	}
	
	String around(String line):internalGetStringAtHandler()&& args(..,line) {
		try {
			return proceed(line);
		} catch (BadLocationException e) {
			//XXX LOG  - nao dah p gerenalizar totalmente
			TexlipsePlugin.log("TexEditorTools.getStringAt", e);
		}
		return line;
	}
	
	int around(int length): internalGetLineLengthHandler()&& args(..,length) {
		try {
			return proceed(length);
		} catch (BadLocationException e) {
			//XXX LOG  - nao dah p gerenalizar totalmente
			TexlipsePlugin.log("TexEditorTools.getLineLength:", e);
		}
		return length;
	}
	
	int around():internalGetLineLengthHandlerSoft(){
		try {
			return proceed();
		} catch (SoftException e) {
			return 0;
		}
	}

	int around(int index):internalGetIndexLineHandler()&& args(..,index) {
		try {
			return proceed(index);
		} catch (BadLocationException e) {
			//XXX LOG - nao dah p gerenalizar totalmente
			TexlipsePlugin.log("TexEditorTools.getIndexAtLine: ", e);
		}
		return index;
	}

	String around(String delimiter): internalGetLineDelimiterHandler() && args(*,delimiter){
		try {
			return proceed(delimiter);
		} catch (BadLocationException e) {
			//XXX LOG - nao dah p gerenalizar totalmente
			TexlipsePlugin.log("TexEditorTools.getLineDelimiter: ", e);
		}
		return delimiter;
	}

	String around(): getIndentationHandler() {
		try {
			return proceed();
		} catch (Exception e) {
			//XXX LOG  - nao dah p gerenalizar totalmente
			TexlipsePlugin.log("TexAutoIndentStrategy:getIndentation", e);
		}
		return "";
	}

	String around(String indentation):internalGetIndentationHandler() && args(..,indentation){
		try {
			proceed(indentation);
		} catch (Exception e) {
			//XXX LOG - nao dah p gerenalizar totalmente
			TexlipsePlugin.log("TexEditorTools:getIndentation", e);
		}
		return indentation;
	}
	
	IDocument around(IDocument doc): internalGetDocumentHandler() && args(doc,*) {
		try {
			return proceed(doc);
		} catch (CoreException e) {
			return doc;
		}
	}

//	void around(): internalDoExitHandler3() {
//		try {
//			proceed();
//		} catch (BadLocationException e) {
//		}
//	}

//	void around(): internalDoExitHandler2() {
//		try {
//			proceed();
//		} catch (BadLocationException e1) {
//			// Should not happen
//		}
//	}

	ExitFlags around(): internalDoExitHandler1() {
		try {
			return proceed();
		} catch (BadLocationException e) {
		}
		return new ExitFlags(0, true);
	}

	ICompletionProposal[] around(): internalComputeCompletionProposals2Handler() {
		try {
			return proceed();
		} catch (BadLocationException e) {
			TexlipsePlugin.log("TexCompletionProcessor: ", e);
			return new ICompletionProposal[0];
		}
	}

//	ICompletionProposal[] around(): internalComputeCompletionProposalsHandler() {
//		try {
//			return proceed();
//		} catch (BadLocationException e) {
//		}
//		return null;
//	}

	boolean around(int lines): internaIitemInsertedHandler()&&args(..,lines,*) {
		try {
			return proceed(lines);
		} catch (Exception e) {
			lines = -1;
		}
		return false;
	}

	void around(TexAutoIndentStrategy tex): dropItemHandler()&& this(tex) {
		try {
			proceed(tex);
		} catch (Exception e) {
			//XXX LOG - nao dah p gerenalizar totalmente
			TexlipsePlugin.log("TexAutoIndentStrategy:dropItem", e);
		} finally {
			tex.itemSetted = false;
		}
	}

//	String around():internalGetHoverInfoHandler() {
//		try {
//			return proceed();
//		} catch (CoreException e) {
//		}
//		return null;
//	}

//	void around(): intenalleft2Handler(){
//		try {
//			proceed();
//		} catch (BadPositionCategoryException e) {
//			// JavaPlugin.log(e);
//		}
//	}
//
//	void around(): intenalleftHandler(){
//		try {
//			proceed();
//		} catch (BadLocationException e) {
//			// JavaPlugin.log(e);
//		}
//	}

	boolean around(boolean left): intenalVerifyKey3Handler()&& args(..,left) {
		try {
			return proceed(left);
		} catch (BadLocationException e) {
			// Could happen, no worry
		}
		return left;
	}

	int around(): intenalVerifyKey2Handler(){
		try {
			return proceed();
		} catch (BadPartitioningException e) {
			//XXX LOG - nao dah p gerenalizar totalmente
			TexlipsePlugin.log("Bad partitioning", e);
		}
		return 1;
	}

//	void around():intenalVerifyKeyHandler(){
//		try {
//			proceed();
//		} catch (BadLocationException e) {
//		} catch (BadPositionCategoryException e) {
//		}
//	}

//	void around():internalUpdateHandler() {
//		try {
//			proceed();
//		} catch (BadPositionCategoryException e) {
//			// ignore and return
//		}
//	}

	boolean around(): isMaskedHandler() {
		try {
			return proceed();
		} catch (BadLocationException e) {
		}
		return false;
	}

	ExitFlags around(ExitFlags exitFlags): internalDoExitHandler()&& args(..,exitFlags) {
		try {
			proceed(exitFlags);
		} catch (BadLocationException e) {
		}
		return exitFlags;
	}

}
