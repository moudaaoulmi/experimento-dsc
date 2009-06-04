
package org.eclipse.osgi.framework.internal.core;

import java.io.IOException;
import java.io.InputStream;

import com.atlassw.tools.eclipse.checkstyle.exception.ExceptionHandler;
import com.atlassw.tools.eclipse.checkstyle.exception.GeneralException;

@ExceptionHandler
public class CoreHandler extends GeneralException
{
    public void messageResourceBundlePut(Exception e, String msg)
    {
        MessageResourceBundle.log(MessageResourceBundle.SEVERITY_ERROR, msg, e); //$NON-NLS-1$
    }
    
    public void messageResourceBundleLoad(InputStream input){
        if (input != null)
            try
            {
                input.close();
            }
            catch (IOException e)
            {
                // ignore
            }
    }
}
