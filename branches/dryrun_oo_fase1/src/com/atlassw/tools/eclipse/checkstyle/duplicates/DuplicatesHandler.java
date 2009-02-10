
package com.atlassw.tools.eclipse.checkstyle.duplicates;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IWorkbenchPart;

import com.atlassw.tools.eclipse.checkstyle.CheckstylePlugin;
import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.exception.GeneralException;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

/**
 * @author juliana
 */
public class DuplicatesHandler extends GeneralException
{

    public void getNumberOfDuplicatedLinesHandler(int result)
    {
        result = 0;
    }

    public Object[] getChildrenHandler()
    {
        return new Object[0];
    }

    public boolean selectHandler()
    {
        return false;
    }
}
