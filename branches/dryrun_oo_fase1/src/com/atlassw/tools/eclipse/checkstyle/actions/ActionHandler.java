package com.atlassw.tools.eclipse.checkstyle.actions;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IWorkbenchPart;

import com.atlassw.tools.eclipse.checkstyle.CheckstylePlugin;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;


/**
 * @author juliana
 *
 */

public class ActionHandler {
    
    //catch (CoreException e)
    public void runHandler(Exception e, IWorkbenchPart mPart){
        CheckstyleLog.errorDialog(mPart.getSite().getShell(), e, true);
    }
    
  //catch (CheckstylePluginException e)
    public Status runInWorkspaceHandler(Exception e){
        return new Status(IStatus.ERROR, CheckstylePlugin.PLUGIN_ID, IStatus.OK, e
                .getMessage(), e);
    }
    
}//ActionHandler{}
