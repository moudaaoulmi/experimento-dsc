package org.jhotdraw.contrib.zoom;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

public aspect ZoomHandler {
	
	// Poitcuts
	pointcut MiniMapZoomableView_getInverseSubjectTransform(): execution(private AffineTransform MiniMapZoomableView.internalInverseSubjectTransForm(..));
	
	
	
	// Declare Soft
	declare soft: NoninvertibleTransformException : MiniMapZoomableView_getInverseSubjectTransform();
	
	
	// Around 
	
	AffineTransform around(double subjectsScale, AffineTransform at) : MiniMapZoomableView_getInverseSubjectTransform()&& args(subjectsScale,at) {
		try {
			return proceed(subjectsScale,at);
		}
		catch (NoninvertibleTransformException nte) {
			// all scale-only transforms should be invertable
		}
		return at;
	}

}
