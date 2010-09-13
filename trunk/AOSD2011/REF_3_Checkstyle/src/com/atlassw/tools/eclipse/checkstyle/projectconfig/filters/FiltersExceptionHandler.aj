
package com.atlassw.tools.eclipse.checkstyle.projectconfig.filters;

import java.util.List;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.team.core.TeamException;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.exception.ExceptionHandler;

import org.eclipse.team.internal.ccvs.core.CVSException;

@ExceptionHandler
public privileged aspect FiltersExceptionHandler
{

    // ---------------------------
    // Declare soft's
    // ---------------------------

    declare soft : CoreException : SourceFolderContentProvider_handleProjectHandler() || 
                                   SourceFolderContentProvider_handleContainerHandler();

    declare soft : CVSException : FilesInSyncFilter_acceptHandler();

    // ---------------------------
    // PointCut's
    // ---------------------------

    pointcut FilesInSyncFilter_acceptHandler(): 
        call(* FilesInSyncFilter.internalAccent(..)) &&
        withincode(* FilesInSyncFilter.accept(..));

    pointcut SourceFolderContentProvider_handleProjectHandler(): 
        execution(* PackageFilterEditor.SourceFolderContentProvider.handleProject(..));

    pointcut SourceFolderContentProvider_handleContainerHandler(): 
        execution(* PackageFilterEditor.SourceFolderContentProvider.handleContainer(..));

    // ---------------------------
    // Advices's
    // ---------------------------

    List<Object> around() : SourceFolderContentProvider_handleProjectHandler() || 
                    SourceFolderContentProvider_handleContainerHandler() {
        List<Object> c = null;
        try
        {
            c = proceed();
        }
        catch (CoreException e)
        {
            // this should never happen because we call
            // #isAccessible before invoking #members
        }
        return c;
    }

   

    boolean around() : FilesInSyncFilter_acceptHandler() {
        boolean c = true;
        try
        {
            c = proceed();
        }
        catch (CVSException e)
        {
            //XXX LOG - n dah p generalizar
            CheckstyleLog.log(e);
        }
        return c;
    }

}
