package ish.ecletex.editors.tex;

import ish.ecletex.ecletexPlugin;
import ish.ecletex.preferences.TeXPreferencePage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.DefaultPartitioner;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ContainerGenerator;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.texteditor.ResourceMarkerAnnotationModel;

public class TeXDocumentProvider extends FileDocumentProvider {

	private TeXEditor editor;

	/**
	 * First element is the TeX Version, the second is the display version.
	 */
	private String[][] filterList = new String[][] { { "!'", "¡" },
			{ "?`", "¿" }, { "{\\aa}", "å" }, { "{\\ae}", "æ" },
			{ "{\\cdot}", "·" }, { "{\\copyright}", "©" }, { "{\\div}", "÷" },
			{ "{\\o}", "ø" }, { "{\\pm}", "±" }, { "{\\pounds}", "£" },
			{ "{\\ss}", "ß" }, { "{\\times}", "×" }, { "\\\"\"{A}", "Ä" },
			{ "\\\"\"{E}", "Ë" }, { "\\\"\"{I}", "Ï" }, { "\\\"\"{O}", "Ö" },
			{ "\\\"\"{U}", "Ü" }, { "\\\"\"{\\i}", "ï" }, { "\\\"\"{a}", "ä" },
			{ "\\\"\"{e}", "ë" }, { "\\\"\"{o}", "ö" }, { "\\\"\"{u}", "ü" },
			{ "\\\"\"{y}", "ÿ" }, { "\\'{A}", "Á" }, { "\\'{E}", "É" },
			{ "\\'{I}", "Í" }, { "\\'{O}", "Ó" }, { "\\'{U}", "Ú" },
			{ "\\'{Y}", "Ý" }, { "\\'{\\i}", "í" }, { "\\'{a}", "á" },
			{ "\\'{e}", "é" }, { "\\'{o}", "ó" }, { "\\'{u}", "ú" },
			{ "\\'{y}", "ý" }, { "\\^{A}", "Â" }, { "\\^{E}", "Ê" },
			{ "\\^{I}", "Î" }, { "\\^{O}", "Ô" }, { "\\^{U}", "Û" },
			{ "\\^{\\i}", "î" }, { "\\^{a}", "â" }, { "\\^{e}", "ê" },
			{ "\\^{o}", "ô" }, { "\\^{u}", "û" }, { "\\`{A}", "À" },
			{ "\\`{E}", "È" }, { "\\`{I}", "Ì" }, { "\\`{O}", "Ò" },
			{ "\\`{U}", "Ù" }, { "\\`{\\i}", "ì" }, { "\\`{a}", "à" },
			{ "\\`{e}", "è" }, { "\\`{o}", "ò" }, { "\\`{u}", "ù" },
			{ "\\c{C}", "Ç" }, { "\\c{c}", "ç" }, { "\\~{A}", "Ã" },
			{ "\\~{N}", "Ñ" }, { "\\~{O}", "Õ" }, { "\\~{a}", "ã" },
			{ "\\~{n}", "ñ" }, { "\\~{o}", "õ" }, { "{\\\"\"A}", "Ä" },
			{ "{\\\"\"E}", "Ë" }, { "{\\\"\"I}", "Ï" }, { "{\\\"\"O}", "Ö" },
			{ "{\\\"\"U}", "Ü" }, { "{\\\"\"\\i}", "ï" }, { "{\\\"\"a}", "ä" },
			{ "{\\\"\"e}", "ë" }, { "{\\\"\"o}", "ö" }, { "{\\\"\"u}", "ü" },
			{ "{\\\"\"y}", "ÿ" }, { "{\\'A}", "Á" }, { "{\\'E}", "É" },
			{ "{\\'I}", "Í" }, { "{\\'O}", "Ó" }, { "{\\'U}", "Ú" },
			{ "{\\'Y}", "Ý" }, { "{\\'\\i}", "í" }, { "{\\'a}", "á" },
			{ "{\\'e}", "é" }, { "{\\'o}", "ó" }, { "{\\'u}", "ú" },
			{ "{\\'y}", "ý" }, { "{\\AA}", "Å" }, { "{\\AE}", "Æ" },
			{ "{\\O}", "Ø" }, { "{\\P}", "¶" }, { "{\\S}", "§" },
			{ "{\\^A}", "Â" }, { "{\\^E}", "Ê" }, { "{\\^I}", "Î" },
			{ "{\\^O}", "Ô" }, { "{\\^U}", "Û" }, { "{\\^\\i}", "î" },
			{ "{\\^a}", "â" }, { "{\\^e}", "ê" }, { "{\\^o}", "ô" },
			{ "{\\^u}", "û" }, { "{\\`A}", "À" }, { "{\\`E}", "È" },
			{ "{\\`I}", "Ì" }, { "{\\`O}", "Ò" }, { "{\\`U}", "Ù" },
			{ "{\\`\\i}", "ì" }, { "{\\`a}", "à" }, { "{\\`e}", "è" },
			{ "{\\`o}", "ò" }, { "{\\`u}", "ù" }, { "{\\~A}", "Ã" },
			{ "{\\~N}", "Ñ" }, { "{\\~O}", "Õ" }, { "{\\~a}", "ã" },
			{ "{\\~n}", "ñ" }, { "{\\~o}", "õ" }

	};

	public TeXDocumentProvider(TeXEditor editor) {
		super();
		this.editor = editor;
	}

	private Flow2DocumentListener flow_listener;

	protected IDocument createDocument(Object element) throws CoreException {
		// IDocument document = super.createDocument(element);
		IDocument document = new TexFileDocument();
		if (setDocumentContent(document, (IEditorInput) element,
				getEncoding(element))) {
			setupDocument(element, document);
		}

		if (document != null) {
			IDocumentPartitioner partitioner = new DefaultPartitioner(
					new TeXPartitionScanner(), new String[] {
							TeXPartitionScanner.TEX_MATH,
							TeXPartitionScanner.TEX_CURLY_BRACKETS,
							TeXPartitionScanner.TEX_SQUARE_BRACKETS,
							TeXPartitionScanner.TEX_COMMENTS });
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}

		if (ecletexPlugin.getDefault().getPreferenceStore().getBoolean(
				TeXPreferencePage.CHARACTER_FILTER)) {
			document.set(filterRead(document.get()));
		}
		if (ecletexPlugin.getDefault().getPreferenceStore().getBoolean(
				TeXPreferencePage.WORD_WRAP_2)) {
			flow_listener = new Flow2DocumentListener(editor);
			document.addDocumentListener(flow_listener);

		}

		return document;
	}

	public void ReflowDocument(IEditorInput input) {
		IDocument doc = this.getDocument(input);
		if (flow_listener == null) {
			System.out.println("Flow Manager is NULL");
			return;
		}

		for (int i = 0; i < doc.getNumberOfLines(); i++) {
			internalReflowDocument(doc, i);
		}
	}

	private void internalReflowDocument(IDocument doc, int i) {
		System.out.println("Fixing [" + i + "] of [" + doc.getNumberOfLines()
				+ "]");
		flow_listener.FixLine(i, doc);
	}

	private String filterWrite(String doc) {

		for (int i = 0; i < filterList.length; i++) {
			doc = Replace(doc, filterList[i][1], filterList[i][0]);
		}
		return doc;
	}

	private String filterRead(String doc) {
		for (int i = 0; i < filterList.length; i++) {
			doc = Replace(doc, filterList[i][0], filterList[i][1]);
		}
		return doc;
	}

	private String deWordWrap(String doc) {
		doc = Replace(doc, "\r\r\n", "");
		return doc;
	}

	private String Replace(String origonal, String search, String replace) {
		int offset = 0;
		int matchStart = 0;
		while ((matchStart = origonal.indexOf(search, offset)) >= 0) {
			origonal = origonal.substring(0, matchStart)
					+ replace
					+ origonal.substring(matchStart + search.length(),
							(origonal.length()));
			offset = matchStart;
		}
		return origonal;
	}

	protected void doSaveDocument(IProgressMonitor monitor, Object element,
			IDocument document, boolean overwrite) throws CoreException {
		if (element instanceof IFileEditorInput) {
			if (ecletexPlugin.getDefault().getPreferenceStore().getBoolean(
					TeXPreferencePage.CHARACTER_FILTER_SAVE)
					|| editor.wrap_support) {
				try {
					IFileEditorInput input = (IFileEditorInput) element;
					String encoding = getEncoding(input);
					if (encoding == null)
						encoding = getDefaultEncoding();

					String doc = document.get();

					if (ecletexPlugin
							.getDefault()
							.getPreferenceStore()
							.getBoolean(TeXPreferencePage.CHARACTER_FILTER_SAVE)) {
						System.out.println("Using TeX Filter Save Method.");
						doc = filterWrite(doc);
						String docRead = filterRead(document.get());
						document.set(docRead);
					}

					if (editor.wrap_support) {
						System.out.println("Using De-Word wrap Save Method.");
						doc = deWordWrap(doc);
					}

					InputStream stream = new ByteArrayInputStream(doc
							.getBytes(encoding));
					IFile file = input.getFile();

					if (file.exists()) {

						FileInfo info = (FileInfo) getElementInfo(element);

						if (info != null && !overwrite)
							checkSynchronizationState(info.fModificationStamp,
									file);

						// inform about the upcoming content change
						fireElementStateChanging(element);
						internalDoSaveDocument(element, monitor, overwrite,
								stream, file);

						// If here, the editor state will be flipped to
						// "not dirty".
						// Thus, the state changing flag will be reset.

						if (info != null) {

							ResourceMarkerAnnotationModel model = (ResourceMarkerAnnotationModel) info.fModel;
							model.updateMarkers(info.fDocument);

							info.fModificationStamp = computeModificationStamp(file);
						}

					} else {
						internalDoSaveDocument(monitor, stream, file);
					}

				} catch (IOException x) {
					String message = (x.getMessage() != null ? x.getMessage()
							: ""); //$NON-NLS-1$
					IStatus s = new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID,
							IStatus.OK, message, x);
					throw new CoreException(s);
				}

			} else {
				super.doSaveDocument(monitor, element, document, overwrite);
			}

		} else {
			super.doSaveDocument(monitor, element, document, overwrite);
		}
	}

	private void internalDoSaveDocument(IProgressMonitor monitor,
			InputStream stream, IFile file) throws CoreException {
			monitor.beginTask(
					"Saving and Filtering File.", 2000); //$NON-NLS-1$
			ContainerGenerator generator = new ContainerGenerator(
					file.getParent().getFullPath());
			generator.generateContainer(new SubProgressMonitor(
					monitor, 1000));
			file.create(stream, false, new SubProgressMonitor(
					monitor, 1000));
	}

	private void internalDoSaveDocument(Object element,
			IProgressMonitor monitor, boolean overwrite, InputStream stream,
			IFile file) throws CoreException {
		file.setContents(stream, overwrite, true, monitor);
	}

}