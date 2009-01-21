/*
 * 23th, November, 2008 
 */
package com.atlassw.tools.eclipse.checkstyle.quickfixes;


import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.atlassw.tools.eclipse.checkstyle.CheckstylePlugin;

/**
 * @author juliana
 *
 */
public aspect FixCheckstyleMarkersJobHandler {
    
    declare soft : CoreException : runInUIThreadHandler();
    
    pointcut runInUIThreadHandler(): execution(* FixCheckstyleMarkersJob.runInUIThread(..));
    
    IStatus around() : runInUIThreadHandler() {
        IStatus c = null;
            try{
                c = proceed();
            } catch (CoreException e)
            {
                return new Status(IStatus.ERROR, CheckstylePlugin.PLUGIN_ID, IStatus.OK,
                        e.getMessage(), e);
            }
            return c;
    }//around()


}//public aspect FixCheckstyleMarkersJobHandler{}

