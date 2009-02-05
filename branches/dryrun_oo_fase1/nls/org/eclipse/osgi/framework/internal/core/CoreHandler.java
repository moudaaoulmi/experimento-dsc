
package org.eclipse.osgi.framework.internal.core;

import java.io.IOException;
import java.io.InputStream;

public class CoreHandler
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
