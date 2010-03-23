/*
 * Created on 09-Jul-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ish.ecletex.wizards.template;

import ish.ecletex.templateProvider;
import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

/**
 * @author ish
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class templateCreationOperation implements IRunnableWithProgress {

	templateWizardPage page;

	public templateCreationOperation(templateWizardPage page) {
		this.page = page;
	}

	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		internalRun(monitor);
	}

	private void internalRun(IProgressMonitor monitor)
			throws InvocationTargetException {
		monitor.beginTask("Creating Template...", 1);
		templateProvider.AddUserTemplate(page.getCategory(), page.getName(),
				page.getTemplate());

	}

}
