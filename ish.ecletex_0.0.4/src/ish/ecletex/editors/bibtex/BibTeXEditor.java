package ish.ecletex.editors.bibtex;

import ish.ecletex.editors.bibtex.dom.BibtexEntry;
import ish.ecletex.editors.bibtex.dom.BibtexFile;
import ish.ecletex.editors.bibtex.dom.BibtexString;
import ish.ecletex.editors.bibtex.parser.BibtexParser;
import ish.ecletex.editors.bibtex.parser.ParseException;
import ish.ecletex.editors.tex.TeXEditor;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;

import org.eclipse.swt.layout.FillLayout;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.*;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.ide.IDE;

/**
 * An example showing how to create a multi-page editor. This example has 3
 * pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to change the font used in page 2
 * <li>page 2 shows the words in page 0 in sorted order
 * </ul>
 */
public class BibTeXEditor extends MultiPageEditorPart implements
		IResourceChangeListener {

	/** The text editor used in page 0. */
	private TextEditor editor;

	private Composite composite;

	private HashMap entries;

	private BibtexFile bfile;

	private boolean dirty = false;

	/**
	 * Creates a multi-page editor example.
	 */
	public BibTeXEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	void createSourceViewer() {
		editor = new TeXEditor(this);
		int index = addPage(editor, getEditorInput());
		setPageText(index, "Source");
	}

	public void updateFormPage() {
		createFormPage();
	}

	void createFormPage() {
		entries = new HashMap();
		boolean updating = false;
		if (composite != null) {
			for (int i = 0; i < composite.getChildren().length; i++) {
				composite.getChildren()[i].dispose();
			}
			updating = true;
		} else {
			composite = new Composite(getContainer(), SWT.NONE);
		}
		composite.setLayout(new FillLayout());

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		bfile = new BibtexFile();
		BibtexParser parser = new BibtexParser(false);
		IPath path = ((FileEditorInput) getEditorInput()).getPath();
		internalCreateFormPage(parser, path);
		FormToolkit toolkit = new FormToolkit(composite.getDisplay());
		ScrolledForm base = toolkit.createScrolledForm(composite);
		base.getBody().setLayout(layout);
		List list = bfile.getEntries();
		for (int j = 0; j < list.size(); j++) {
			Object o = list.get(j);
			if (o instanceof BibtexEntry) {
				BibTexFormGen gen = new BibTexFormGen((BibtexEntry) o, bfile,
						toolkit, base.getBody(), entries, this);
				gen.GenerateForm();
			}
		}
		if (!updating) {
			int index = addPage(composite);
			setPageText(index, "Details");
		}
		composite.layout(true);

	}

	private void internalCreateFormPage(BibtexParser parser, IPath path) {
		parser.parse(bfile, new FileReader(path.toFile()));
	}

	/**
	 * Creates the pages of the multi-page editor.
	 */
	protected void createPages() {
		createFormPage();
		createSourceViewer();
	}

	/**
	 * The <code>MultiPageEditorPart</code> implementation of this
	 * <code>IWorkbenchPart</code> method disposes all nested editors.
	 * Subclasses may extend.
	 */
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}

	/**
	 * Saves the multi-page editor's document.
	 */
	public void doSave(IProgressMonitor monitor) {

		int active = getActivePage();
		if (active == 0) {
			saveFormPage();
			editor.setInput(editor.getEditorInput());
		} else {
			System.out.println("Saving Source View.");
			editor.doSave(monitor);
			// createFormPage();
		}
		setClean();

	}

	public void saveFormPage() {
		System.out.println("Saving Form Page...");
		if (entries == null)
			return;
		Set s = entries.keySet();
		String[] keys = new String[entries.size()];
		keys = (String[]) s.toArray(keys);

		for (int i = 0; i < keys.length; i++) {
			Object[] data = (Object[]) entries.get(keys[i]);
			HashMap fields = (HashMap) data[0];
			BibtexEntry entry = (BibtexEntry) data[1];
			Set fieldset = fields.keySet();
			String[] fieldkeys = new String[fieldset.size()];
			fieldkeys = (String[]) fieldset.toArray(fieldkeys);
			for (int j = 0; j < fieldkeys.length; j++) {
				String fieldValue = ((Text) fields.get(fieldkeys[j])).getText();
				BibtexString bs = bfile.makeString(fieldValue);
				entry.setField(fieldkeys[j], bs);
			}
		}
		internalSaveFormPage();

	}

	private void internalSaveFormPage() {
		FileWriter fwriter = new FileWriter(
				((FileEditorInput) getEditorInput()).getPath().toFile(), false);
		PrintWriter pw = new PrintWriter(fwriter);
		bfile.printBibtex(pw);
		pw.flush();
		fwriter.flush();
		pw.close();
		fwriter.close();
	}

	/**
	 * Saves the multi-page editor's document as another file. Also updates the
	 * text for page 0's tab, and updates this multi-page editor's input to
	 * correspond to the nested editor's.
	 */
	public void doSaveAs() {
		IEditorPart editor = getEditor(0);
		editor.doSaveAs();
		setPageText(0, editor.getTitle());
		setInput(editor.getEditorInput());
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart
	 */
	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}

	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method
	 * checks that the input is an instance of <code>IFileEditorInput</code>.
	 */
	public void init(IEditorSite site, IEditorInput editorInput)
			throws PartInitException {
		if (!(editorInput instanceof IFileEditorInput))
			throw new PartInitException(
					"Invalid Input: Must be IFileEditorInput");
		setTitle(editorInput.getName());
		super.init(site, editorInput);
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart.
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}

	public void setDirty() {
		dirty = true;
		firePropertyChange(PROP_DIRTY);
	}

	public void setClean() {
		dirty = false;
		firePropertyChange(PROP_DIRTY);
	}

	public boolean isDirty() {
		if (dirty) {
			return true;
		} else {
			return super.isDirty();
		}

	}

	protected void pageChange(int newPageIndex) {

	}

	/**
	 * Closes all project files on project close.
	 */
	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow()
							.getPages();
					for (int i = 0; i < pages.length; i++) {
						if (((FileEditorInput) editor.getEditorInput())
								.getFile().getProject().equals(
										event.getResource())) {
							IEditorPart editorPart = pages[i]
									.findEditor((FileEditorInput) editor
											.getEditorInput());
							pages[i].closeEditor(editorPart, true);
						}
					}
				}
			});
		}
	}

}
