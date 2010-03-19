package org.tigris.aopmetrics.ajdt;

import org.aspectj.ajde.Ajde;
//Aspecto privileged não pode conter annotations
public privileged aspect AjdtHandlerExceptionHandler {

	pointcut internalDoSynchronousBuildHandler(): execution(private void AjdtBuilder.internalDoSynchronousBuild());

	pointcut initHandler(): execution(public void NullIdeManager.init());

	declare soft: InterruptedException: internalDoSynchronousBuildHandler();
	declare soft: Throwable: initHandler();

	void around(): initHandler() {
		try {
			proceed();
		} catch (Throwable t) {
			Ajde.getDefault().getErrorHandler().handleError(
					"Null IDE failed to initialize.", t);
		}
	}

	void around(): internalDoSynchronousBuildHandler(){
		try {
			proceed();
		} catch (InterruptedException ie) {
		}
	}

}
