package com.touchgraph.graphlayout.graphelements;

import com.touchgraph.graphlayout.Node;
import br.upe.dsc.reusable.exception.*;
@ExceptionHandler
public privileged aspect GraphelementsHandler extends PrintStackTraceAbstractExceptionHandler {
	
	public pointcut printStackTraceException() : execution(private void VisibleLocality.internalUpdateLocalityFromVisibility(Node));

//	pointcut internalUpdateLocalityFromVisibilityHandler(): execution(private void VisibleLocality.internalUpdateLocalityFromVisibility(Node));
//
//	declare soft: TGException : internalUpdateLocalityFromVisibilityHandler();
//
//	void around(): internalUpdateLocalityFromVisibilityHandler() {
//		try {
//			proceed();
//		} catch (TGException ex) {
//			ex.printStackTrace();
//		}
//	}

}
