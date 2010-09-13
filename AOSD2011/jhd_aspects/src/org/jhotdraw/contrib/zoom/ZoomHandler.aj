package org.jhotdraw.contrib.zoom;



import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

import org.jhotdraw.exception.ExceptionHandler;

@ExceptionHandler
public aspect ZoomHandler {
	
	// Declare Soft
	declare soft: NoninvertibleTransformException : MiniMapZoomableView_getInverseSubjectTransforHandler();
	
	// Poitcuts
	pointcut MiniMapZoomableView_getInverseSubjectTransforHandler(): execution (AffineTransform MiniMapZoomableView.internalInverseSubjectTransForm(..));
	
	// Around 
	AffineTransform around(double subjectsScale, AffineTransform at) : MiniMapZoomableView_getInverseSubjectTransforHandler()&& args(subjectsScale,at) {
		try {
			return proceed(subjectsScale,at);
		}
		catch (NoninvertibleTransformException nte) {
			// all scale-only transforms should be invertable
		}
		return at;
	}

}
