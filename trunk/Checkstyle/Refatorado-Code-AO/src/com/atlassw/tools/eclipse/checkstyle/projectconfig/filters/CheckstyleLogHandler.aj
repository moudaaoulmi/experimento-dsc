/*
 * 23th, November, 2008
 */

package com.atlassw.tools.eclipse.checkstyle.projectconfig.filters;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.internal.ccvs.core.CVSException;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

/**
 * @author juliana, Cristiane
 */
public privileged aspect CheckstyleLogHandler
{
    // ---------------------------
    // Declare soft's
    // ---------------------------
    declare soft : CheckstylePluginException : isFileAffectedHandler();

    declare soft : CoreException : isFileAffectedHandler() ||
                                   internalPartClosedHandler();

    // ---------------------------
    // PointCut's
    // ---------------------------
    pointcut isFileAffectedHandler(): execution(* CheckFileOnOpenPartListener.isFileAffected(..));

    pointcut internalPartClosedHandler(): 
        execution(* CheckFileOnOpenPartListener.internalPartClosed(..));

    // ---------------------------
    // Advice's
    // ---------------------------
    boolean around() : isFileAffectedHandler() {
        boolean c = false;
        try
        {
            c = proceed();
        }
        catch (CheckstylePluginException e)
        {
            CheckstyleLog.log(e);
        }
        catch (CoreException e)
        {
            // should never happen, since editor cannot be open
            // when project isn't
            CheckstyleLog.log(e);
        }

        return c;
    }

    void around(): internalPartClosedHandler(){
        try
        {
            proceed();
        }
        catch (CoreException e)
        {
            CheckstyleLog.log(e);
        }
    }
    
    // ---------------------------
    // Declare soft's
    // ---------------------------
    declare soft : TeamException : getSyncInfoHandler();

    // ---------------------------
    // Pointcut's
    // ---------------------------
    pointcut getSyncInfoHandler(): execution(*  FilesInSyncFilter2.accept(..));

    // ---------------------------
    // Advices's
    // ---------------------------
    boolean around(Object element) : getSyncInfoHandler() && args(element) {
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
    
 // ---------------------------
    // Declare soft's
    // ---------------------------
    declare soft : CVSException : isIgnoredHandler();

    // ---------------------------
    // PointCut's
    // ---------------------------
    pointcut isIgnoredHandler(): execution(* FilesInSyncFilter.accept(..));

    // ---------------------------
    // Advice's
    // ---------------------------
    boolean around() : isIgnoredHandler() {
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
    }// around()
    
    // ---------------------------
    // Declare soft's
    // ---------------------------
    declare soft : JavaModelException : getSourceDirPathsHandler();

    declare soft : CoreException : getSourceDirPathsHandler();

    // ---------------------------
    // PointCut's
    // ---------------------------
    pointcut getSourceDirPathsHandler(): execution(* NonSrcDirsFilter.getSourceDirPaths(..));

    // ---------------------------
    // Advice's
    // ---------------------------
    List around() : getSourceDirPathsHandler() {
        List c = null;
        try
        {
            c = proceed();
        }
        catch (JavaModelException e)
        {
            CheckstyleLog.log(e);
        }
        catch (CoreException e)
        {
            // should never happen, since editor cannot be open
            // when project isn't
            CheckstyleLog.log(e);
        }
        return c;
    }// around()
    
    // ---------------------------
    // Declare soft's
    // ---------------------------
    declare soft : JavaModelException : handleProjectHandler();

    declare soft : CoreException : handleProjectHandler() ||handleContainerHandler();

    // ---------------------------
    // Pointcut's
    // ---------------------------
    pointcut handleProjectHandler(): execution(* PackageFilterEditor.SourceFolderContentProvider.handleProject(..));

    pointcut handleContainerHandler(): execution(* PackageFilterEditor.SourceFolderContentProvider.handleContainer(..));

    // ---------------------------
    // Advice's
    // ---------------------------
    List around() : handleProjectHandler() || handleContainerHandler() {
        List c = null;
        try
        {
            c = proceed();
        }
        catch (JavaModelException e)
        {
            CheckstyleLog.log(e);
        }
        catch (CoreException e)
        {
            // this should never happen because we call
            // #isAccessible before invoking #members
        }
        return c;
    }// around()


}// CheckFileOnOpenPartListenerHandler{}
