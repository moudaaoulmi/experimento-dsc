
package com.atlassw.tools.eclipse.checkstyle.config.configtypes;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.osgi.util.NLS;
import org.eclipse.core.resources.IFile;
import com.atlassw.tools.eclipse.checkstyle.Messages;
import com.atlassw.tools.eclipse.checkstyle.config.GlobalCheckConfigurationWorkingSet;
import com.atlassw.tools.eclipse.checkstyle.config.ICheckConfigurationWorkingSet;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.LocalCheckConfigurationWorkingSet;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.CoreException;
import java.io.OutputStream;
import java.lang.IllegalArgumentException;

public privileged aspect ProjectConfigurationEditorHandler
{
    // ---------------------------
    // Declare soft's
    // ---------------------------
    
    declare soft: CheckstylePluginException: internalGetEditedWorkingCopyHandler();

    declare soft: IOException: internalEnsureFileExistsHandler();

    declare soft: CoreException: internalEnsureFileExistsHandler();

    // ---------------------------
    // Pointcut's
    // ---------------------------
    
    pointcut internalGetEditedWorkingCopyHandler():
        execution(* ProjectConfigurationEditor.internalGetEditedWorkingCopy(..));

    pointcut internalEnsureFileExistsHandler():
        execution(* ProjectConfigurationEditor.internalEnsureFileExists(..));

    pointcut secInternalEnsureFileExistsHandler():
        execution(* ProjectConfigurationEditor.secInternalEnsureFileExists(..));

    // ---------------------------
    // Advice's
    // ---------------------------
    
    void around() throws CheckstylePluginException : internalGetEditedWorkingCopyHandler(){
        ProjectConfigurationEditor pCE = (ProjectConfigurationEditor) thisJoinPoint.getThis();
        try
        {
            proceed();
        }
        catch (CheckstylePluginException e)
        {
            String location = pCE.mLocation.getText();

            if (StringUtils.trimToNull(location) == null)
            {
                throw e;
            }

            ICheckConfigurationWorkingSet ws = pCE.mCheckConfigDialog
                    .getCheckConfigurationWorkingSet();
            IPath tmp = new Path(location);
            boolean isFirstPartProject = ResourcesPlugin.getWorkspace().getRoot().getProject(
                    tmp.segment(0)).exists();

            if (ws instanceof LocalCheckConfigurationWorkingSet && !isFirstPartProject)
            {
                location = ((LocalCheckConfigurationWorkingSet) ws).getProject().getFullPath()
                        .append(location).toString();
                pCE.mLocation.setText(location);
            }
            else if (ws instanceof GlobalCheckConfigurationWorkingSet && !isFirstPartProject)
            {
                throw new CheckstylePluginException(NLS
                        .bind(Messages.ProjectConfigurationEditor_msgNoProjectInWorkspace, tmp
                                .segment(0)));
            }

            if (pCE.ensureFileExists(location))
            {
                pCE.mWorkingCopy.setLocation(pCE.mLocation.getText());
            }
            else
            {
                throw e;
            }
        }
    }

    void around(IFile file, OutputStream out) throws CheckstylePluginException: 
        internalEnsureFileExistsHandler()&& args(file, out){
        try
        {
            proceed(file, out);
        }
        catch (IOException ioe)
        {
            CheckstylePluginException.rethrow(ioe);
        }
        catch (CoreException e)
        {
            CheckstylePluginException.rethrow(e);
        }
        finally
        {
            IOUtils.closeQuietly(out);
        }
    }

    void around() throws CheckstylePluginException : secInternalEnsureFileExistsHandler(){
        try
        {
            proceed();
        }
        catch (IllegalArgumentException e)
        {
            CheckstylePluginException.rethrow(e);
        }
    }

}
