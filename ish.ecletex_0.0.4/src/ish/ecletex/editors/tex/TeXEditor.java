package ish.ecletex.editors.tex;

import ish.ecletex.ecletexPlugin;
import ish.ecletex.editors.bibtex.BibTeXEditor;
import ish.ecletex.editors.tex.actions.AddWordAction;
import ish.ecletex.editors.tex.actions.CommentLineAction;
import ish.ecletex.editors.tex.spelling.GlobalDictionary;
import ish.ecletex.preferences.TeXPreferencePage;
import ish.ecletex.properties.texFileProperties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.TextOperationAction;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

public class TeXEditor extends TextEditor {

	private ColorManager colorManager;

	TeXOutline outline;

	BibTeXEditor bibeditor;

	ProjectionSupport fProjectionSupport;

	public boolean wrap_support = false;

	TexFoldingStructureProvider fProjectionModelUpdater;

	public TeXEditor(BibTeXEditor bibeditor) {
		this();
		this.bibeditor = bibeditor;
	}

	public TeXEditor() {
		super();
		colorManager = ecletexPlugin.getDefault().getColorManager();
		setSourceViewerConfiguration(new TeXConfiguration(colorManager, this));
		setDocumentProvider(new TeXDocumentProvider(this));
		if (ecletexPlugin.getDefault().getPreferenceStore().getBoolean(
				TeXPreferencePage.WORD_WRAP_2)) {
			wrap_support = true;
		}

	}

	// Overridden so as to set the properties of the file incase the file doesnt
	// have any.
	public void doSetInput(IEditorInput input) throws CoreException {
		if (input instanceof IFileEditorInput) {
			IFile file = ((IFileEditorInput) input).getFile();
			CheckProperties(file);
		}
		super.doSetInput(input);
	}

	private void CheckProperties(IFile file) {
		if ((file.getPersistentProperty(new QualifiedName("ish.ecletex",
				texFileProperties.ALTERNATE_SUPPORT)) == null)
				|| (file.getPersistentProperty(new QualifiedName("ish.ecletex",
						texFileProperties.DICTIONARY_PROPERTY)) == null)) {

			file.setPersistentProperty(new QualifiedName("ish.ecletex",
					texFileProperties.ALTERNATE_SUPPORT),
					texFileProperties.DEFAULT_ALTERNATE_SUPPORT);
			file.setPersistentProperty(new QualifiedName("ish.ecletex",
					texFileProperties.DICTIONARY_PROPERTY),
					texFileProperties.DEFAULT_DICTIONARY);

			System.out.println("Set File Defaults.");
		}
	}

	public void dispose() {
		// colorManager.dispose();
		if (outline != null)
			outline.setInput(null);
		super.dispose();

	}

	public String[] getBibtexFilenames() {
		if (outline == null) {
			getAdapter(IContentOutlinePage.class);
		}
		return outline.getBibtexFilenames();

	}

	public IFile getFile() {
		IEditorInput input = this.getEditorInput();
		IFile file = null;
		if (input instanceof IFileEditorInput) {
			file = ((IFileEditorInput) input).getFile();
		}
		return file;
	}

	public Object getAdapter(Class required) {
		// System.out.println("ADAPTER ["+required.getName()+"]");
		if (IContentOutlinePage.class.equals(required)) {
			if (outline == null) {
				outline = new TeXOutline(getDocumentProvider(), this);
				if (getEditorInput() != null) {
					outline.setInput(getEditorInput());
				}
			}
			return outline;
		}

		if (fProjectionSupport != null) {
			Object adapter = fProjectionSupport.getAdapter(getSourceViewer(),
					required);
			if (adapter != null)
				return adapter;
		}

		return super.getAdapter(required);
	}

	protected ISourceViewer createSourceViewer(Composite parent,
			IVerticalRuler ruler, int styles) {
		// ProjectionViewer viewer = new ProjectionViewer(parent,ruler,new
		// OverviewRuler())
		// ISourceViewer viewer=null;
		// if(ecletexPlugin.getDefault().getPreferenceStore().getBoolean(TeXPreferencePage.WORD_WRAP)){
		// viewer= new ProjectionViewer(parent, ruler, getOverviewRuler(),
		// isOverviewRulerVisible(), styles | SWT.WRAP);
		// }else{
		// viewer= new ProjectionViewer(parent, ruler, getOverviewRuler(),
		// isOverviewRulerVisible(), styles);
		// }
		ISourceViewer viewer = new ProjectionViewer(parent, ruler,
				getOverviewRuler(), isOverviewRulerVisible(), styles);
		// ISourceViewer viewer = super.createSourceViewer(parent, ruler,
		// styles);
		if (!wrap_support) {
			viewer.getTextWidget().setWordWrap(
					ecletexPlugin.getDefault().getPreferenceStore().getBoolean(
							TeXPreferencePage.WORD_WRAP));
		}
		return viewer;
	}

	protected void createActions() {
		super.createActions();

		IAction a = new TextOperationAction(TeXEditorMessages
				.getResourceBundle(), "ContentAssistProposal.", this,
				ISourceViewer.CONTENTASSIST_PROPOSALS);
		a
				.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
		IAction comment = new CommentLineAction(this);
		comment.setText(TeXEditorMessages.getString("CommentAction.label"));
		comment.setToolTipText(TeXEditorMessages
				.getString("CommentAction.tooltip"));
		comment.setDescription(TeXEditorMessages
				.getString("CommentAction.decription"));
		IAction addword = new AddWordAction(this);

		setAction("ContentAssistProposal", a);
		setAction("CommentAction", comment);
		setAction("AddWordAction", addword);
	}

	public void editorContextMenuAboutToShow(IMenuManager menu) {
		super.editorContextMenuAboutToShow(menu);
		addAction(menu, "ContentAssistProposal");
		addAction(menu, "CommentAction");
		String currentword = AddWordAction.getCurrentWord(this);
		if (currentword != null) {
			if (!GlobalDictionary.isCorrect(currentword, this)) {
				IAction addword = getAction("AddWordAction");
				addword.setText("Add: " + AddWordAction.getCurrentWord(this));
				addAction(menu, "AddWordAction");
			}
		}
	}

	public IPath getPath() {
		IEditorInput input = getEditorInput();
		IFile file = null;
		if (input instanceof IFileEditorInput) {
			file = ((IFileEditorInput) input).getFile();
		}
		if (file == null)
			return null;

		IPath path = file.getLocation();
		IPath folderPath = path.removeLastSegments(1);
		return folderPath;
	}

	public IProject getProject() {
		IEditorInput input = getEditorInput();
		IFile file = null;
		if (input instanceof IFileEditorInput) {
			file = ((IFileEditorInput) input).getFile();
		}
		if (file == null)
			return null;

		// IPath filepath = file.getRawLocation();

		IProject project = file.getProject();

		// IProject[] projects =
		// ResourcesPlugin.getWorkspace().getRoot().getProjects();
		// int projectIndex = -1;
		// for(int i=0;i<projects.length;i++){
		// IResource resource = projects[i].findMember(file.ge);
		// if(resource!=null){
		// projectIndex=i;
		// break;
		// }
		// }
		// if(projectIndex==-1)
		// return null;
		return project;
	}

	public void doRevertToSaved() {

		super.doRevertToSaved();
		if (outline != null)
			outline.update();

	}

	public void doSave(IProgressMonitor monitor) {
		ITextSelection offset = ((ITextSelection) this.getSelectionProvider()
				.getSelection());
		super.doSave(monitor);
		if (outline != null)
			outline.update();
		this.getSelectionProvider().setSelection(offset);
		if (bibeditor != null) {
			bibeditor.updateFormPage();
			bibeditor.setClean();
		}
	}

	public void doSaveAs() {
		ITextSelection offset = ((ITextSelection) this.getSelectionProvider()
				.getSelection());
		super.doSaveAs();
		if (outline != null)
			outline.update();
		this.getSelectionProvider().setSelection(offset);
	}

	public int getCaretPos() {
		return getSourceViewer().getTextWidget().getCaretOffset();
	}

	public int getSelectionPos() {
		return getSourceViewer().getTextWidget().getSelection().x;
	}

	public void setSelectionPos(int pos) {
		this.getSelectionProvider().setSelection(new TextSelection(pos, 0));
	}

	public void setCaretPos(int pos) throws IllegalArgumentException {
		getSourceViewer().getTextWidget().setCaretOffset(pos);
	}

	public void replaceText(int offset, int length, String text) {
		getSourceViewer().getTextWidget()
				.replaceTextRange(offset, length, text);
	}

	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		super.createPartControl(parent);
		ProjectionViewer projectionViewer = (ProjectionViewer) getSourceViewer();

		fProjectionSupport = new ProjectionSupport(projectionViewer,
				getAnnotationAccess(), getSharedColors());
		fProjectionSupport
				.addSummarizableAnnotationType("org.eclipse.ui.workbench.texteditor.error"); //$NON-NLS-1$
		fProjectionSupport
				.addSummarizableAnnotationType("org.eclipse.ui.workbench.texteditor.warning"); //$NON-NLS-1$
		// fProjectionSupport.setHoverControlCreator(new
		// IInformationControlCreator() {
		// public IInformationControl createInformationControl(Shell shell) {
		// return new CustomSourceInformationControl(shell,
		// IDocument.DEFAULT_CONTENT_TYPE);
		// }
		// });
		fProjectionSupport.install();

		fProjectionModelUpdater = new TexFoldingStructureProvider();
		if (fProjectionModelUpdater != null)
			fProjectionModelUpdater.install(this, projectionViewer);

		if (isFoldingEnabled())
			projectionViewer.doOperation(ProjectionViewer.TOGGLE);

		if (wrap_support) {
			((TeXDocumentProvider) getDocumentProvider())
					.ReflowDocument(getEditorInput());
		}

	}

	boolean isFoldingEnabled() {
		return true;
		// return
		// JavaPlugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.EDITOR_FOLDING_ENABLED);
	}
}