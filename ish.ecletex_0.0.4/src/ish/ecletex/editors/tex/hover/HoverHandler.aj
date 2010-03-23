package ish.ecletex.editors.tex.hover;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;

public privileged aspect HoverHandler {	

	pointcut getHoverRegionHandler(): execution(public IRegion TexHover.getHoverRegion(ITextViewer, int));
	pointcut paintControlHandler(): execution(public void PaintListener.paintControl(PaintEvent)) && within(TexInformationControl);	
	pointcut SetDataHandler(): execution(private void TexInformationControl.SetData(String));

	// declare soft

	declare soft: BadLocationException: getHoverRegionHandler();
	declare soft: Exception: paintControlHandler() || SetDataHandler();

	// Advices

	void around(TexInformationControl tex): SetDataHandler() && this(tex) {
		try {
			proceed(tex);
		} catch (Exception ex) {
			tex.resetData();
		}
	}

	
	void around(): paintControlHandler(){
		try {
			proceed();
		} catch (Exception ex) {
			System.out.println("Error Drawing Hover");
		}
	}

	IRegion around(int offset): getHoverRegionHandler() && args(*, offset) {
		try {
			return proceed(offset);
		} catch (BadLocationException ex) {
			return new Region(offset, 0);
		}
	}

	

}
