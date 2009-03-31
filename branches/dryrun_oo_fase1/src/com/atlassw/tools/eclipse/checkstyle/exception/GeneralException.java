
package com.atlassw.tools.eclipse.checkstyle.exception;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Shell;
import org.xml.sax.SAXException;

import com.atlassw.tools.eclipse.checkstyle.CheckstylePlugin;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

@ExceptionHandler
public class GeneralException
{

    public Object checkstyleLog(Exception e)
    {
        CheckstyleLog.log(e);
        return null;
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
    
    public void closeQuietlyInputStream(InputStream in)
    {
        IOUtils.closeQuietly(in);
    }
    
    public void closeQuietlyOutputStream(OutputStream out)
    {
        IOUtils.closeQuietly(out);
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
    
    public void rethrowSAXException(Exception e) throws SAXException
    {
        throw new SAXException(e);
    }
    
    public Object warningDialogHandler(Shell shell, String msg, Exception e)
    {
        CheckstyleLog.warningDialog(shell, msg, e);
        return null;
    }
    
    public void setErrorMessage(TitleAreaDialog titleAreaDialog,
            String msg)
    {
        titleAreaDialog.setErrorMessage(msg);
        return;
    }
    
    public Object throwInternalError() throws InternalError
    {
       throw new InternalError();
    }
    
    public void returnDefault(Object result)
    {
        result = null;
    }

}
