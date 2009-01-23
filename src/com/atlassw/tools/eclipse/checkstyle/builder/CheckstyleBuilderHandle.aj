package com.atlassw.tools.eclipse.checkstyle.builder;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import com.atlassw.tools.eclipse.checkstyle.CheckstylePlugin;
import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import org.eclipse.core.runtime.CoreException;

public aspect CheckstyleBuilderHandle
{
    declare soft: CheckstylePluginException: checkstyleBuilder_buildHandler();
    
    pointcut checkstyleBuilder_buildHandler(): execution(* CheckstyleBuilder.build(..)) ;
    
    IProject[] around() throws CoreException:  checkstyleBuilder_buildHandler() {
        IProject[] result = null;
        try{
            result = proceed();
        }catch(CheckstylePluginException e){
            Status status = new Status(IStatus.ERROR, 
                    CheckstylePlugin.PLUGIN_ID,
                    IStatus.ERROR,
                    e.getMessage() != null ? e.getMessage(): ErrorMessages.CheckstyleBuilder_msgErrorUnknown, e);
             throw new CoreException(status);
        }
        return result;
    }
    
    declare soft: CheckstylePluginException: checkstyleBuilder_handleBuildSelectionHandler();
    
    pointcut checkstyleBuilder_handleBuildSelectionHandler(): execution(* CheckstyleBuilder.handleBuildSelection(..));
    
    void around()throws CoreException: checkstyleBuilder_handleBuildSelectionHandler() {
       try{
           proceed();
       } catch (CheckstylePluginException e)
       {
           Status status = new Status(IStatus.ERROR, 
                   CheckstylePlugin.PLUGIN_ID, 
                   IStatus.ERROR, e.getLocalizedMessage(), e);
           throw new CoreException(status);
       }
    }
    
}
