
package com.atlassw.tools.eclipse.checkstyle.projectconfig.filters;

import java.lang.CloneNotSupportedException;
import java.lang.InternalError;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.internal.ccvs.core.CVSException;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

public privileged aspect FiltersHandler
{

    // ---------------------------
    // Declare soft's
    // ---------------------------
    declare soft : CloneNotSupportedException : AbstractFilter_cloneHandler();

    declare soft : CheckstylePluginException : CheckFileOnOpenPartListener_isFileAffectedHandler();

    declare soft : CoreException : CheckFileOnOpenPartListener_isFileAffectedHandler() ||
                                   CheckFileOnOpenPartListener_internalPartClosedHandler() ||
                                   NonSrcDirsFilter_getSourceDirPathsHandler()  ||
                                   SourceFolderContentProvider_handleProjectHandler() || 
                                   SourceFolderContentProvider_handleContainerHandler();

    declare soft : TeamException : FilesInSyncFilter2_getSyncInfoHandler();

    declare soft : CVSException : FilesInSyncFilter_isIgnoredHandler();

    declare soft : JavaModelException : NonSrcDirsFilter_getSourceDirPathsHandler() ||
                                        SourceFolderContentProvider_handleProjectHandler();

    // ---------------------------
    // PointCut's
    // ---------------------------
    pointcut AbstractFilter_cloneHandler(): execution(* AbstractFilter.clone(..));

    pointcut CheckFileOnOpenPartListener_isFileAffectedHandler(): execution(* CheckFileOnOpenPartListener.isFileAffected(..));

    pointcut CheckFileOnOpenPartListener_internalPartClosedHandler(): 
        execution(* CheckFileOnOpenPartListener.internalPartClosed(..));

    pointcut FilesInSyncFilter2_getSyncInfoHandler(): execution(*  FilesInSyncFilter2.accept(..));

    pointcut FilesInSyncFilter_isIgnoredHandler(): execution(* FilesInSyncFilter.accept(..));

    pointcut NonSrcDirsFilter_getSourceDirPathsHandler(): execution(* NonSrcDirsFilter.getSourceDirPaths(..));

    pointcut SourceFolderContentProvider_handleProjectHandler(): execution(* PackageFilterEditor.SourceFolderContentProvider.handleProject(..));

    pointcut SourceFolderContentProvider_handleContainerHandler(): execution(* PackageFilterEditor.SourceFolderContentProvider.handleContainer(..));

    // ---------------------------
    // Advices's
    // ---------------------------
    Object around() : AbstractFilter_cloneHandler() {
        try
        {
            return proceed();
        }
        catch (CloneNotSupportedException e)
        {
            throw new InternalError(); // should never happen
        }
    }

    boolean around() : CheckFileOnOpenPartListener_isFileAffectedHandler() {
        boolean c = false;
        try
        {
            c = proceed();
        }
        catch (CheckstylePluginException e)
        {
            CheckstyleLog.log(e);
        }
        return c;
    }

    List around() : NonSrcDirsFilter_getSourceDirPathsHandler() ||
                    SourceFolderContentProvider_handleProjectHandler() || 
                    SourceFolderContentProvider_handleContainerHandler(){
        List c = null;
        try
        {
            c = proceed();
        }
        catch (JavaModelException e)
        {
            CheckstyleLog.log(e);
        }
        return c;
    }

    Object around(): CheckFileOnOpenPartListener_internalPartClosedHandler() ||
                    CheckFileOnOpenPartListener_isFileAffectedHandler() ||
                    NonSrcDirsFilter_getSourceDirPathsHandler(){
        Object result = null;
        try
        {
            result = proceed();
        }
        catch (CoreException e)
        {
            CheckstyleLog.log(e);
        }
        return result;
    }

    List around() : SourceFolderContentProvider_handleProjectHandler() || SourceFolderContentProvider_handleContainerHandler() {
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

    boolean around(Object element) : FilesInSyncFilter2_getSyncInfoHandler() && args(element) {
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

    boolean around() : FilesInSyncFilter_isIgnoredHandler() {
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