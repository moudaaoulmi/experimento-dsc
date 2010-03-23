package ish.ecletex.editors.tex;

import ish.ecletex.ecletexPlugin;
import ish.ecletex.editors.tex.hover.TexHover;
import ish.ecletex.preferences.TeXPreferencePage;
import ish.ecletex.properties.ecletexProjectProperties;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class TeXConfiguration extends SourceViewerConfiguration {
	private TeXDoubleClickStrategy doubleClickStrategy;
	private TeXMathScanner mathScanner;
	private TeXScanner scanner;
	private TeXCommentScanner commentScanner;
	private TeXArgumentsScanner argumentScanner;
	private TeXOptionalScanner optionalScanner;
	private ColorManager colorManager;
	private TeXEditor editor;
	private TexHover hover;

	public TeXConfiguration(ColorManager colorManager,TeXEditor editor) {
		this.colorManager = colorManager;
		this.editor = editor;
	}
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
			IDocument.DEFAULT_CONTENT_TYPE,
			TeXPartitionScanner.TEX_MATH,
			TeXPartitionScanner.TEX_CURLY_BRACKETS,
			TeXPartitionScanner.TEX_SQUARE_BRACKETS,
			TeXPartitionScanner.TEX_COMMENTS
			};
	}
	public ITextDoubleClickStrategy getDoubleClickStrategy(
		ISourceViewer sourceViewer,
		String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new TeXDoubleClickStrategy();
		return doubleClickStrategy;
	}

	protected TeXScanner getTeXScanner() {
		if (scanner == null) {
			scanner = new TeXScanner(colorManager,editor);
			
		}
		return scanner;
	}
	protected TeXMathScanner getTeXMathScanner() {
		if (mathScanner == null) {
			mathScanner = new TeXMathScanner(colorManager);
		}
		return mathScanner;
	}
	
	protected TeXCommentScanner getTeXCommentScanner() {
		if (commentScanner == null) {
			commentScanner = new TeXCommentScanner(colorManager,editor);
		}
		return commentScanner;
	}
	
	protected TeXArgumentsScanner getTeXArgumentsScanner() {
		if (argumentScanner == null) {
			argumentScanner = new TeXArgumentsScanner(colorManager,editor);
		}
		return argumentScanner;
	}
	
	protected TeXOptionalScanner getTeXOptionalScanner() {
		if (optionalScanner == null) {
			optionalScanner = new TeXOptionalScanner(colorManager,editor);
		}
		return optionalScanner;
	}


	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		/*TeXDamagerRepairer tdr = new TeXDamagerRepairer(getTeXScanner(),editor);
		reconciler.setDamager(tdr, TeXPartitionScanner.TEX_MATH);
		reconciler.setRepairer(tdr, TeXPartitionScanner.TEX_MATH);
		
		tdr = new TeXDamagerRepairer(getTeXScanner(),editor);
		reconciler.setDamager(tdr, TeXPartitionScanner.TEX_CURLY_BRACKETS);
		reconciler.setRepairer(tdr, TeXPartitionScanner.TEX_CURLY_BRACKETS);
		
		tdr = new TeXDamagerRepairer(getTeXScanner(),editor);
		reconciler.setDamager(tdr, TeXPartitionScanner.TEX_SQUARE_BRACKETS);
		reconciler.setRepairer(tdr, TeXPartitionScanner.TEX_SQUARE_BRACKETS);
		
		tdr = new TeXDamagerRepairer(getTeXScanner(),editor);
		reconciler.setDamager(tdr, TeXPartitionScanner.TEX_COMMENTS);
		reconciler.setRepairer(tdr, TeXPartitionScanner.TEX_COMMENTS);*/
		
		DefaultDamagerRepairer dr =
			new DefaultDamagerRepairer(getTeXMathScanner());
		reconciler.setDamager(dr, TeXPartitionScanner.TEX_MATH);
		reconciler.setRepairer(dr, TeXPartitionScanner.TEX_MATH);
		
		dr = new DefaultDamagerRepairer(getTeXArgumentsScanner());
		reconciler.setDamager(dr, TeXPartitionScanner.TEX_CURLY_BRACKETS);
		reconciler.setRepairer(dr, TeXPartitionScanner.TEX_CURLY_BRACKETS);
		
		dr = new DefaultDamagerRepairer(getTeXOptionalScanner());
		reconciler.setDamager(dr, TeXPartitionScanner.TEX_SQUARE_BRACKETS);
		reconciler.setRepairer(dr, TeXPartitionScanner.TEX_SQUARE_BRACKETS);
		
		dr = new DefaultDamagerRepairer(getTeXCommentScanner());
		reconciler.setDamager(dr, TeXPartitionScanner.TEX_COMMENTS);
		reconciler.setRepairer(dr, TeXPartitionScanner.TEX_COMMENTS);
		

		dr = new DefaultDamagerRepairer(getTeXScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
		
		//tdr = new TeXDamagerRepairer(getTeXScanner(),editor);
		//reconciler.setDamager(tdr, IDocument.DEFAULT_CONTENT_TYPE);
		//reconciler.setRepairer(tdr, IDocument.DEFAULT_CONTENT_TYPE);
		

		/*NonRuleBasedDamagerRepairer ndr =
			new NonRuleBasedDamagerRepairer(
				new TextAttribute(
					colorManager.getColor(IXMLColorConstants.XML_COMMENT)));
		reconciler.setDamager(ndr, TeXPartitionScanner.XML_COMMENT);
		reconciler.setRepairer(ndr, TeXPartitionScanner.XML_COMMENT);*/

		return reconciler;
	}
	
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
	
		ContentAssistant assistant = new ContentAssistant();
		assistant.setContentAssistProcessor(
			new TeXCompletionEngine(editor),
			IDocument.DEFAULT_CONTENT_TYPE);

		assistant.enableAutoActivation(true);
		
		assistant.setAutoActivationDelay(ecletexPlugin.getDefault().getPreferenceStore().getInt(TeXPreferencePage.TRIGGER_TIME));
		assistant.setProposalPopupOrientation(
			IContentAssistant.PROPOSAL_OVERLAY);
		assistant.setContextInformationPopupOrientation(
			IContentAssistant.CONTEXT_INFO_ABOVE);
		return assistant;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getTextHover(org.eclipse.jface.text.source.ISourceViewer, java.lang.String)
	 */
	public ITextHover getTextHover(ISourceViewer sourceViewer,
			String contentType) {
		if(hover==null){
			hover = new TexHover(editor);
		}
		return hover;
	}
}