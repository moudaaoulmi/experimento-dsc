package ish.ecletex.builders;

import org.aspectj.lang.SoftException;
import ish.ecletex.builders.latexlogparser.Model;

public privileged aspect BuildersHandler {

	pointcut internalBuildBibtexHandler(): execution(private void ecletexBibtexBuildManager.internalBuildBibtex(String));

	pointcut internalBuildBibtexHandlerSoft(): execution(private TeXBuildEvent[] ecletexBibtexBuildManager.buildBibtex());	

	pointcut internalBuildLatexHandler(): execution(private Model ecletexLatexBuildManager.internalBuildLatex(String));

	pointcut internalBuildPDFHandler(): execution(private TeXBuildEvent[] ecletexPDFBuildManager.internalBuildPDF(String, String));

	pointcut internalBuildPSHandler(): execution(private TeXBuildEvent[] ecletexPSBuildManager.internalBuildPS(String, String));

	declare soft: Exception: internalBuildBibtexHandler()||internalBuildLatexHandler()||internalBuildPDFHandler() || internalBuildPSHandler();

	void around(): internalBuildBibtexHandler(){
		try {
			proceed();
		} catch (Exception ex) {
			throw new SoftException(ex);
		}
	}
	

	Model around(): internalBuildLatexHandler() {
		try {
			return proceed();
		} catch (Exception ex) {
			return null;
			// TeXBuildEvent error = new
			// TeXBuildEvent(TeXBuildEvent.ERROR,0,"Build failed: "+ex.getMessage(),FULL_TARGET);
			// return new TeXBuildEvent[]{error};
		}
	}


	TeXBuildEvent[] around(ecletexPSBuildManager ecletex): (internalBuildBibtexHandlerSoft() || internalBuildPDFHandler()|| internalBuildPSHandler())&& this(ecletex) {
		try {
			return proceed(ecletex);
		} catch (Exception ex) {
			TeXBuildEvent error = new TeXBuildEvent(TeXBuildEvent.ERROR, 0,
					"Build failed: " + ex.getMessage(), ecletex.FULL_TARGET);
			return new TeXBuildEvent[] { error };
		}
	}

}
