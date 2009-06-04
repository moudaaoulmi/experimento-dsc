
package com.atlassw.tools.eclipse.checkstyle.projectconfig.filters;

import java.util.List;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.internal.ccvs.core.CVSException;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;


@ExceptionHandler
public privileged aspect FiltersExceptionHandler
{

    // ---------------------------
    // Declare soft's
    // ---------------------------

    declare soft : CoreException : SourceFolderContentProvider_handleProjectHandler() || 
                                   SourceFolderContentProvider_handleContainerHandler();

    declare soft : TeamException : FilesInSyncFilter2_acceptHandler();

    declare soft : CVSException : FilesInSyncFilter_acceptHandler();

    // ---------------------------
    // PointCut's
    // ---------------------------

    pointcut FilesInSyncFilter2_acceptHandler(): 
        call(* FilesInSyncFilter2.internalAccent(..)) &&
        withincode(* FilesInSyncFilter2.accept(..));

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

    boolean around() : FilesInSyncFilter2_acceptHandler(){
        boolean result = false;
        try
        {
            result = proceed();
        }
        catch (TeamException e)
        {
            CheckstyleLog.log(e);
        }
        return result;
    }

    boolean around() : FilesInSyncFilter_acceptHandler() {
        boolean c = true;
        try
        {
            c = proceed();
        }
        catch (CVSException e)
        {
            CheckstyleLog.log(e);
        }
        return c;
    }

}
