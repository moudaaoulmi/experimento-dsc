package com.atlassw.tools.eclipse.checkstyle.quickfixes;


import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.text.edits.MalformedTreeException;

import com.atlassw.tools.eclipse.checkstyle.CheckstylePlugin;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

public class QuickFixesHandler{
    
    //CoreException
    //MalformedTreeException
    //BadLocationException
    public void checkstyleLogHandler(Exception e, String msg){
        CheckstyleLog.log(e, msg);
    }
    
    public void checkstyleLogHandler2(MalformedTreeException e, String msg){
        CheckstyleLog.log(e, msg);
    }
    
    //CoreException
    public void checkstyleErrorDialogHandler(CoreException e, Shell shell){
        CheckstyleLog.errorDialog(shell, e, true);
    }
    
    //CoreException
    public IStatus runInUIThreadHandler (CoreException e){
        return new Status(IStatus.ERROR, CheckstylePlugin.PLUGIN_ID, IStatus.OK,
                e.getMessage(), e);
    }

}
