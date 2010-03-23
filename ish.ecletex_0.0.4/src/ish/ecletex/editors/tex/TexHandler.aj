package ish.ecletex.editors.tex;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Vector;
import ish.ecletex.xml.Element;
import org.aspectj.lang.SoftException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public privileged aspect TexHandler {
	
	String[] str = {"Error Loading XML.","Error Parsing XML."};
	
	pointcut LoadCommmadDatabaseHandler(): execution(private void TeXCompletionEngine.LoadCommmadDatabase());
	
	pointcut internalLoadCommmadDatabaseHandler(): execution(private void TeXCompletionEngine.internalLoadCommmadDatabase(Vector,
			Enumeration));	
	

	pointcut internalEndOfLineOfHandler(): execution(private int NonRuleBasedDamagerRepairer.internalEndOfLineOf(IRegion , int));

	pointcut internalGetDamageRegionHandler(): execution(private IRegion NonRuleBasedDamagerRepairer.internalGetDamageRegion(ITypedRegion,
			DocumentEvent));	

	pointcut internalEvaluateHandler(): execution(private IToken SpellingRule.internalEvaluate(String));	

	pointcut internalHandler(): execution(private Object TeXCompletionEngine.internal(Enumeration, Object));

	pointcut internal2Handler(): execution(private String TeXCompletionEngine.internal2(Element, String));

	pointcut internal3Handler(): execution(private Element internal3(Element , Element));

	pointcut internal4Handler(): execution(private Element TeXCompletionEngine.internal4(Element, Element));

	pointcut getWordHandler(): execution(private String TeXCompletionEngine.getWord(ITextViewer, int));

	pointcut completionFilterHandler(): execution(private String TeXCompletionEngine.completionFilter(ITextViewer, int));

	pointcut isTemplateHandler(): execution(private boolean TeXCompletionEngine.isTemplate(ITextViewer, int));

	pointcut getTemplateFilterHandler(): execution(private String TeXCompletionEngine.getTemplateFilter(ITextViewer, int));

	pointcut internalDoSaveDocumentHandler(): execution(private void TeXDocumentProvider.internalDoSaveDocument(Object,IProgressMonitor,
			 boolean , InputStream , IFile));	

	pointcut getStringHandler(): execution(public static String TeXEditorMessages.getString(String));	

	pointcut outlineLabelProviderHandler(): execution(TeXOutline$OutlineLabelProvider.new(..));	
	
	pointcut getDocumentHandler(): execution(private Document TeXOutline.getDocument(String));
	
	pointcut internalSelectionChangedHandler(): execution(private void TeXOutline.internalSelectionChanged(int));

	pointcut internalDoSaveDocumentHandler2(): execution(private void TeXDocumentProvider.internalDoSaveDocument(IProgressMonitor ,	InputStream , IFile));
	
	
	// Declare soft
	// ---------------------------------------------------------------------------

	declare soft: BadLocationException : internalEndOfLineOfHandler() || internalGetDamageRegionHandler();

	declare soft: CoreException: internalEvaluateHandler() || internalDoSaveDocumentHandler();

	declare soft: Exception: LoadCommmadDatabaseHandler() || getWordHandler() || internalLoadCommmadDatabaseHandler()|| internalHandler() || internal2Handler() || internal3Handler() || internal4Handler() || completionFilterHandler() || isTemplateHandler() || getTemplateFilterHandler() || getDocumentHandler();
	
	declare soft: IOException: outlineLabelProviderHandler();

	
	// Advices
	// --------------------------------------------------------------------
	
	

	void around(IProgressMonitor monitor): internalDoSaveDocumentHandler2() && args(monitor, ..){
		try {
			proceed(monitor);
		} finally {
			monitor.done();
		}
	}

	

	int around(NonRuleBasedDamagerRepairer nonRule): internalEndOfLineOfHandler() && this(nonRule) {
		try {
			return proceed(nonRule);
		} catch (BadLocationException x) {
			return nonRule.fDocument.getLength();
		}
	}

	IRegion around(ITypedRegion partition): internalGetDamageRegionHandler() && args(partition,*) {
		try {
			return proceed(partition);
		} catch (BadLocationException x) {
		}
		return partition;
	}

	

	IToken around(): internalEvaluateHandler() {
		try {
			return proceed();
		} catch (CoreException ex) {
			return Token.UNDEFINED;
		}
	}

	void around(): LoadCommmadDatabaseHandler() ||  internalLoadCommmadDatabaseHandler(){
		try {
			proceed();
		} catch (Exception ex) {
			System.out.println(str[thisEnclosingJoinPointStaticPart.getId()]);
			System.out.println(ex);
		}
	}


	Object around(): internalHandler() || internal2Handler(){
		try {
			return proceed();
		} catch (Exception ex) {
			// continue;
			throw new SoftException(ex);
		}
	}

	Element around(Element eoptionals): internal3Handler() && args(*, eoptionals) {
		try {
			return proceed(eoptionals);
		} catch (Exception ex) {
		}
		return eoptionals;
	}	

	

	String around(): getWordHandler()  || getTemplateFilterHandler(){
		try {
			return proceed();
		} catch (Exception ex) {
			System.out.println(ex);
			return "";
		}
	}

	Element around(Element earguments): internal4Handler() && args(* , earguments) {
		try {
			return proceed(earguments);
		} catch (Exception ex) {
			// No Arguments.
			// System.out.println("No Arguments for "+word);
		}
		return earguments;
	}

	Object around(): completionFilterHandler() || isTemplateHandler()  {
		try {
			return proceed();
		} catch (Exception ex) {
			System.out.println(ex);
			return null;
		}
	}	

	void around(Object element, TeXDocumentProvider tex) throws CoreException: internalDoSaveDocumentHandler() && args(element, ..) && this(tex) {
		try {
			proceed(element, tex);
		} catch (CoreException x) {
			// inform about failure
			tex.fireElementStateChangeFailed(element);
			throw x;
		} catch (RuntimeException x) {
			// inform about failure
			tex.fireElementStateChangeFailed(element);
			throw x;
		}
	}

	
	
	

	String around(String key): getStringHandler() && args(key) {
		try {
			return proceed(key);
		} catch (MissingResourceException e) {
			return "!" + key + "!";
		}
	}	

	Object around():  outlineLabelProviderHandler() {
		try {
			return proceed();
		} catch (IOException ex) {
			System.out.println(ex.toString());
		}
		return new Object();
	}	
	
	Document around(): getDocumentHandler() {
		try {
			return proceed();
		} catch (Exception ex) {
			return new Document("");
		}
	}
	
	void around(TeXOutline tex): internalSelectionChangedHandler() && this(tex) {
		try {
			proceed(tex);
		} catch (IllegalArgumentException x) {
			tex.editor.resetHighlightRange();
		}
	}

}
