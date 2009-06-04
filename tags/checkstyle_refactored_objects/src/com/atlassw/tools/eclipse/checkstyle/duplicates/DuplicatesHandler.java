
package com.atlassw.tools.eclipse.checkstyle.duplicates;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.util.NLS;

import com.atlassw.tools.eclipse.checkstyle.CheckstylePlugin;
import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.exception.ExceptionHandler;
import com.atlassw.tools.eclipse.checkstyle.exception.GeneralException;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

/**
 * @author juliana
 */
@ExceptionHandler
public class DuplicatesHandler extends GeneralException
{

    public Object[] getChildrenHandler()
    {
        return new Object[0];
    }

    public boolean selectHandler()
    {
        return false;
    }
    
    public Status contributeToActionBars(CoreException e, IProject project){
        CheckstyleLog
        .log(e, NLS.bind(ErrorMessages.errorWhileBuildingProject,
                project.getName()));
        return new Status(IStatus.ERROR, CheckstylePlugin.PLUGIN_ID,
        IStatus.OK, NLS.bind(
                ErrorMessages.errorWhileBuildingProject, project
                        .getName()), e);
    }
}
