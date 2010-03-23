package ish.ecletex.wizards.export.pdf;

import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.operation.IRunnableWithProgress;

public privileged aspect PdfHandler {

	pointcut internalPerformFinishHandler() : execution(private boolean PDFExportWizard.internalPerformFinish(IRunnableWithProgress));

	pointcut internalCreateControlHandler() : execution(private String PDFWizardPageOne.internalCreateControl(String));	
	

	declare soft: CoreException: internalCreateControlHandler();
	declare soft: InvocationTargetException: internalPerformFinishHandler();
	declare soft: InterruptedException: internalPerformFinishHandler();
	
	

	String around(String target): internalCreateControlHandler() && args(target) {
		try {
			return proceed(target);
		} catch (CoreException ex) {
		}
		return target;
	}

	boolean around(PDFExportWizard pdf): internalPerformFinishHandler() && this(pdf) {
		try {
			return proceed(pdf);
		} catch (InvocationTargetException e) {
			pdf.handleException(e.getTargetException());
			return false;
		} catch (InterruptedException e) {
			return false;
		}
	}

	

}
