package net.sourceforge.texlipse.editor.hover;

import java.io.FileNotFoundException;
import java.io.IOException;
import net.sourceforge.texlipse.TexlipsePlugin;
import net.sourceforge.texlipse.model.ReferenceEntry;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.swt.events.PaintEvent;

public privileged aspect HoverHandler {

	pointcut getHoverInfoHandler(): execution(public String TexHover.getHoverInfo(ITextViewer, IRegion));

	pointcut getHoverRegionHandler(): execution(public IRegion TexHover.getHoverRegion(ITextViewer, int) );

	pointcut internalGetDocumentExtractHandler(): execution(private void TexInformationControl.internalGetDocumentExtract(String, int, StringBuffer));

	pointcut internaSetRefHoverHandler(): execution(private String TexInformationControl.internaSetRefHover(ReferenceEntry, String) );

	declare soft:BadLocationException: getHoverInfoHandler()||getHoverRegionHandler()||internaSetRefHoverHandler();
	declare soft:FileNotFoundException: internalGetDocumentExtractHandler();
	declare soft:IOException: internalGetDocumentExtractHandler();

	String around(String extract): internaSetRefHoverHandler()&& args(*,extract) {
		try {
			proceed(extract);
		} catch (BadLocationException e) {
			TexlipsePlugin.log("TexInformationControl: ", e);
		}
		return extract;
	}

	void around(): internalGetDocumentExtractHandler() {
		try {
			proceed();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}
	
	IRegion around(int offset):getHoverRegionHandler() && args(offset){
		try {
			return proceed(offset);
		} catch (BadLocationException ex) {
			return new Region(offset, 0);
		}
	}

	String around(): getHoverInfoHandler() {
		try {
			return proceed();
		} catch (BadLocationException ex) {
			return "";
		}
	}

}
