
package com.atlassw.tools.eclipse.checkstyle.exception;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Shell;

import com.atlassw.tools.eclipse.checkstyle.CheckstylePlugin;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

public class GeneralException
{

    public void checkstyleLog(Exception e)
    {
        CheckstyleLog.log(e);
    }

    public void checkstyleLog_E_MSG(Exception e, String msg)
    {
        CheckstyleLog.log(e, msg);
    }

    public void rethrowCheckstylePluginException_E_MSG(Exception e, String msg)
        throws CheckstylePluginException
    {
        CheckstylePluginException.rethrow(e, msg);
    }

    public void rethrowCheckstylePluginException(Exception e) throws CheckstylePluginException
    {
        CheckstylePluginException.rethrow(e);
    }
    
    public void closeQuietlyInputStream(InputStream inStream)
    {
        IOUtils.closeQuietly(inStream);
    }
    
    public void errorDialogCheckstyleLog(Exception e, Shell shell, boolean b){
        CheckstyleLog.errorDialog(shell, e, b);
    }
    
    public void errorDialogCheckstyleLog_Msg(Exception e, Shell shell, String msg, boolean b){
        CheckstyleLog.errorDialog(shell, msg, e, b);
    }
    
    public Status newStatus (Exception e, String msg){
        return new Status(IStatus.ERROR, CheckstylePlugin.PLUGIN_ID, IStatus.OK,
                msg, e);
    }
    

    
}
