package com.touchgraph.graphlayout.graphelements;

import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGException;
@ExceptionHandler
public privileged aspect GraphelementsHandler {

	pointcut internalUpdateLocalityFromVisibilityHandler(): execution(private void VisibleLocality.internalUpdateLocalityFromVisibility(Node));

	declare soft: TGException : internalUpdateLocalityFromVisibilityHandler();

	void around(): internalUpdateLocalityFromVisibilityHandler() {
		try {
			proceed();
		} catch (TGException ex) {
			ex.printStackTrace();
		}
	}

}
