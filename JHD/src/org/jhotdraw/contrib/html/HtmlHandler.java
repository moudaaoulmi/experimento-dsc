package org.jhotdraw.contrib.html;

import org.jhotdraw.exception.ExceptionHandler;
import org.jhotdraw.exception.GeneralException;

@ExceptionHandler
public class HtmlHandler extends GeneralException {

	public void contentProducerRegistryRead() {
		// the class does not exist in this application
		// cannot do much about it so ignore it, the entities of
		// this class will get their toString() value instead
	}

	public void disposableResourceManagerFactoryInitManager() {
		// we set it so we shouldn't get here
	}

	public void eTSLADisposalStrategyStopDisposing1() {
		// ignore
	}

	public void eTSLADisposalStrategyDisposalThreadRun() {
		// just exit
	}

	public void hTMLTextAreaFigureSubstituteEntityKeywords() {
		// invalid marker, ignore
	}

	public String resourceContentProducerGetContent(Exception ex) {
		super.printStackTraceException(ex);
		return ex.toString();
	}
	
}
