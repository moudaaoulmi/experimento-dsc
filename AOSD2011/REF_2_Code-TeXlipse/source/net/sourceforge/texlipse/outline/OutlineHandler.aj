package net.sourceforge.texlipse.outline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.texlipse.TexlipsePlugin;
import net.sourceforge.texlipse.model.MarkerHandler;
import net.sourceforge.texlipse.model.OutlineNode;

import org.aspectj.lang.SoftException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Position;
import org.eclipse.swt.dnd.DragSourceEvent;

public privileged aspect OutlineHandler {

	pointcut internalPerformDropHandler(): execution(private void TexOutlineDNDAdapter.internalPerformDrop(Object, int));

	pointcut internalPerformDropHandlerSoft(): execution(public boolean TexOutlineDNDAdapter.performDrop(Object));

	pointcut internalDragSetDataHandler(): execution(private String TexOutlineDNDAdapter.internalDragSetData(Position,
			String));

	pointcut internalDragSetDataHandlerSoft(): execution(public void TexOutlineDNDAdapter.dragSetData(DragSourceEvent));

	

	pointcut intenalSelectionChangedHandler(): execution(private void TexOutlinePage.intenalSelectionChanged(Position));

	pointcut internalGetSelectedTextHandler(): execution(private String TexOutlinePage.internalGetSelectedText(Position, String));

	pointcut internalGetSelectedTextHandlerSoft(): execution(public String TexOutlinePage.getSelectedText());

	pointcut internalRemoveSelectedTextHandler(): execution(private void TexOutlinePage.internalRemoveSelectedText(Position));

	pointcut internalRemoveSelectedTextHandlerSoft(): execution(public void TexOutlinePage.removeSelectedText());

	pointcut internalPasteHandler(): execution(private void TexOutlinePage.internalPaste(String, Position));

	pointcut internalPasteHandlerSoft(): execution(public boolean TexOutlinePage.paste(String) );

	pointcut internalGetFullOutlineHandler(): execution(private ArrayList<OutlineNode> TexProjectOutline.internalGetFullOutline(IFile,
			String));

	pointcut internalLoadInputHandler(): execution(private List<OutlineNode> TexProjectOutline.internalLoadInput(IFile,
			int, String, MarkerHandler,IFile, List<OutlineNode>));

	pointcut internalLoadInputHandleSoft(): execution(private List<OutlineNode> TexProjectOutline.loadInput(IFile, IFile,int ));

	declare soft: BadLocationException: internalPerformDropHandler()||internalDragSetDataHandler()||internalGetSelectedTextHandler()||internalRemoveSelectedTextHandler()||internalPasteHandler();
	declare soft: IOException: internalGetFullOutlineHandler()||internalLoadInputHandler();

	List<OutlineNode> around(): internalLoadInputHandleSoft() {
		try {
			return proceed();
		} catch (IOException ioe) {
			return new ArrayList<OutlineNode>();
		}
	}

	List<OutlineNode> around(IFile referringFile, int lineNumber,
			String fullName, MarkerHandler marker): internalLoadInputHandler() && args(referringFile,
			lineNumber, fullName,marker,..) {
		try {
			return proceed(referringFile, lineNumber, fullName, marker);
		} catch (IOException ioe) {
			marker.createErrorMarker(referringFile, "Could not parse file "
					+ fullName + ", reason: " + ioe.getMessage(), lineNumber);
			// return new ArrayList<OutlineNode>();
			throw new SoftException(ioe);
		}
	}

	ArrayList<OutlineNode> around(): internalGetFullOutlineHandler() {
		try {
			return proceed();
		} catch (IOException ioe) {
			TexlipsePlugin
					.log(
							"Unable to create full document outline; main file is not parsable",
							ioe);
			return new ArrayList<OutlineNode>();
		}
	}

	//XXX rethrow
	void around(): internalPasteHandler() {
		try {
			proceed();
		} catch (BadLocationException e) {
			// return false;
			throw new SoftException(e);
		}
	}

	//XXX rethrow
	void around(): internalRemoveSelectedTextHandler() {
		try {
			proceed();
		} catch (BadLocationException e) {
			// return;
			throw new SoftException(e);
		}
	}

	String around(): internalGetSelectedTextHandlerSoft() {
		try {
			return proceed();
		} catch (SoftException e) {
			return null;
		}
	}

	//XXX rethrow
	String around(): internalGetSelectedTextHandler() {
		try {
			return proceed();
		} catch (BadLocationException e) {
			// return null;
			throw new SoftException(e);
		}
	}

	void around(TexOutlinePage tex):intenalSelectionChangedHandler()&& this(tex) {
		try {
			proceed(tex);
		} catch (IllegalArgumentException x) {
			tex.editor.resetHighlightRange();
		}
	}

	void around():internalDragSetDataHandlerSoft()||internalRemoveSelectedTextHandlerSoft(){
		try {
			proceed();
		} catch (SoftException e) {
			return;
		}
	}

	String around(): internalDragSetDataHandler() {
		try {
			return proceed();
		} catch (BadLocationException e) {
			TexlipsePlugin.log("Could not set drag data.", e);
			// return;
			throw new SoftException(e);
		}
	}

	boolean around():internalPerformDropHandlerSoft()||internalPasteHandlerSoft(){
		try {
			return proceed();
		} catch (SoftException e) {
			return false;
		}
	}

	void around(): internalPerformDropHandler() {
		try {
			proceed();
		} catch (BadLocationException e) {
			TexlipsePlugin.log("Could not perform drop operation.", e);
			// return false;
			throw new SoftException(e);
		}
	}

}
