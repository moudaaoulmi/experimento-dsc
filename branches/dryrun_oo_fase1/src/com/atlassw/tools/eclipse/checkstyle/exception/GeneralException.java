
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
    
    public void closeQuietlyInputStream(InputStream in)
    {
        IOUtils.closeQuietly(in);
    }
    
    public void closeQuietlyInputStream(InputStream inStream, InputStream defaultConfigStream)
    {
        IOUtils.closeQuietly(inStream);
        IOUtils.closeQuietly(defaultConfigStream);
    }
    
    public void closeQuietlyOutputStream(OutputStream out)
    {
        IOUtils.closeQuietly(out);
    }
    
    public void closeQuietlyOutputStream(OutputStream byteOut, OutputStream out)
    {
        IOUtils.closeQuietly(byteOut);
        IOUtils.closeQuietly(out);
    }
    
    public void closeQuietlyInputOutputStream(InputStream in,OutputStream out)
    {
        IOUtils.closeQuietly(in);
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
    
    public boolean warningDialogHandler(Shell shell, String msg, Exception e)
    {
        CheckstyleLog.warningDialog(shell, msg, e);
        return false;
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
    
    public int returnDefault(int result)
    {
        return result = 0;
    }
    
    public boolean returnDefault(boolean result)
    {
        return result = false;
    }

}
