package com.touchgraph.graphlayout.interaction;

import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGException;
import exception.ExceptionHandler;

import br.upe.dsc.reusable.exception.*;

@ExceptionHandler
public privileged aspect IteractionHandler extends PrintStackTraceAbstractExceptionHandler {
	
	public pointcut printStackTraceException(): internalRunHandler2();

	pointcut internalMouseReleasedHandler(): execution(private void DragAddUI.internalMouseReleased());

	pointcut internalSetUpBackPopupHandler(): execution(private void GLEditUI.internalSetUpBackPopup());

	pointcut internalMouseClickedHandler(): execution(private void GLNavigateUI.GLNavigateMouseListener.internalMouseClicked(Node));

	pointcut internalRunHandler(): execution(private void HVScroll.internalRun());

	pointcut internalSlowScrollToCenterHandler(): execution(private boolean internalSlowScrollToCenter(boolean,int));

	pointcut internalLocalityAdjustmentListenerHandler(): execution(private void LocalityScroll.localityAdjustmentListener.internalLocalityAdjustmentListener(Node));

	pointcut internalRunHandler2(): execution(private void TGAbstractMousePausedUI.PauseThread.internalRun());

	declare soft: TGException : internalMouseReleasedHandler()||internalSetUpBackPopupHandler()||internalMouseClickedHandler()||internalLocalityAdjustmentListenerHandler();
	declare soft: InterruptedException : internalRunHandler()||internalSlowScrollToCenterHandler();
	declare soft: InterruptedException : internalRunHandler2();

//	void around(): internalRunHandler2() {
//		try {
//			proceed();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	boolean around(boolean keepScrolling): internalSlowScrollToCenterHandler() && args(keepScrolling,*) {
		try {
			proceed(keepScrolling);
		} catch (InterruptedException ex) {
			keepScrolling = false;
		}
		return keepScrolling;
	}

	void around(): internalRunHandler() {
		try {
			proceed();
		} catch (InterruptedException ex) {
		}
	}

	void around(): internalMouseClickedHandler() ||internalLocalityAdjustmentListenerHandler(){
		try {
			proceed();
		} catch (TGException ex) {
			System.out.println("Error setting locale");
			ex.printStackTrace();
		}
	}

	void around(): internalMouseReleasedHandler()||internalSetUpBackPopupHandler() {
		try {
			proceed();
		} catch (TGException tge) {
			System.err.println(tge.getMessage());
			tge.printStackTrace(System.err);
		}
	}

}
