package com.touchgraph.graphlayout;

import exception.ExceptionHandler;

@ExceptionHandler
public privileged aspect GraphlayoutHandler {

	pointcut internalAddNearNodesHandler(): execution(private void LocalityUtils.internalAddNearNodes(int));

	pointcut internalRunHandler(): execution(private void LocalityUtils.ShiftLocaleThread.internalRun());

	pointcut internalSetLocaleHandler(): execution(private void LocalityUtils.internalSleep(int));

	pointcut internalExpandNodeHandler(): execution(private void LocalityUtils.internalExpandNode(Node));

	pointcut internalRunHandler2(): execution(private void LocalityUtils.internalRun(Node));

	pointcut myWaitHandler(): execution(private void TGLayout.myWait());

	pointcut internalRunHandler3(): execution(private void TGLayout.internalRun(Thread));

	pointcut internalMainHandler(): execution(private static void TGPanel.internalMain(TGPanel));

	pointcut internalMouseDraggedHandler(): execution(private void TGPanel.BasicMouseMotionListener.internalMouseDragged());

	declare soft: InterruptedException: internalAddNearNodesHandler()||internalRunHandler()||internalSetLocaleHandler()||internalExpandNodeHandler()||myWaitHandler()||internalRunHandler3()||internalMouseDraggedHandler();
	declare soft: TGException: internalAddNearNodesHandler()||internalRunHandler()||internalExpandNodeHandler()||internalRunHandler2()||internalMainHandler();

	void around(): internalMainHandler() {
		try {
			proceed();
		} catch (TGException tge) {
			System.err.println(tge.getMessage());
		}
	}

	void around(): myWaitHandler()|| internalRunHandler3()||internalMouseDraggedHandler()||internalAddNearNodesHandler() ||internalSetLocaleHandler(){
		try {
			proceed();
		} catch (InterruptedException e) {
			// break;
		}
	}

	void around(): internalRunHandler2() {
		try {
			proceed();
		} catch (TGException tge) {
			tge.printStackTrace();
		}
	}

	void around(): internalRunHandler() ||internalExpandNodeHandler(){
		try {
			proceed();
		} catch (TGException tge) {
			System.err.println("TGException: " + tge.getMessage());
		} catch (InterruptedException ex) {
		}
	}

}
