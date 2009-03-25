
package com.atlassw.tools.eclipse.checkstyle.projectconfig.filters;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.internal.ccvs.core.CVSException;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

public privileged aspect FiltersHandler
{

    // ---------------------------
    // Declare soft's
    // ---------------------------

    declare soft : CoreException : SourceFolderContentProvider_handleProjectHandler() || 
                                   SourceFolderContentProvider_handleContainerHandler();

    declare soft : TeamException : FilesInSyncFilter2_getSyncInfoHandler();

    declare soft : CVSException : FilesInSyncFilter_acceptHandler();

    // ---------------------------
    // PointCut's
    // ---------------------------

    pointcut FilesInSyncFilter2_getSyncInfoHandler(): 
        execution(*  FilesInSyncFilter2.accept(..));

    pointcut FilesInSyncFilter_acceptHandler(): 
        execution(* FilesInSyncFilter.accept(..));

    pointcut SourceFolderContentProvider_handleProjectHandler(): 
        execution(* PackageFilterEditor.SourceFolderContentProvider.handleProject(..));

    pointcut SourceFolderContentProvider_handleContainerHandler(): 
        execution(* PackageFilterEditor.SourceFolderContentProvider.handleContainer(..));

    // ---------------------------
    // Advices's
    // ---------------------------

    List around() : SourceFolderContentProvider_handleProjectHandler() || 
                    SourceFolderContentProvider_handleContainerHandler() {
        List c = null;
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

    boolean around(Object element) : FilesInSyncFilter2_getSyncInfoHandler() && 
            args(element) {
        boolean result = false;
        try
        {
            result = proceed(element);
        }
        catch (TeamException e)
        {
            CheckstyleLog.log(e);
        }
        return result;
    }

    boolean around() : FilesInSyncFilter_acceptHandler() {
        boolean c = false;
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
