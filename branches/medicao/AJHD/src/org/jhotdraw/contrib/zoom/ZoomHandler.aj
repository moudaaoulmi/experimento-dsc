package org.jhotdraw.contrib.zoom;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

public aspect ZoomHandler {
	
	// Declare Soft
	declare soft: NoninvertibleTransformException : getInverseSubjectTransforHandler();
	
	// Poitcuts
	pointcut getInverseSubjectTransforHandler(): execution(private AffineTransform MiniMapZoomableView.internalInverseSubjectTransForm(..));
	
	// Around 
	AffineTransform around(double subjectsScale, AffineTransform at) : getInverseSubjectTransforHandler()&& args(subjectsScale,at) {
		try {
			return proceed(subjectsScale,at);
		}
		catch (NoninvertibleTransformException nte) {
			// all scale-only transforms should be invertable
		}
		return at;
	}

}
