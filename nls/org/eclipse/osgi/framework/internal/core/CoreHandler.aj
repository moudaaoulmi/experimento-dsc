
package org.eclipse.osgi.framework.internal.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.lang.reflect.Field;

public privileged aspect CoreHandler
{
    declare soft: Exception:  Core_internalPutHandle() ||
                              Core_internalComputeMissingMessagesHandler();

    declare soft: IOException: Core_internalLoadHandler();

    pointcut Core_internalPutHandle(): execution(void MessageResourceBundle.MessagesProperties.internalPut(..));

    pointcut Core_internalComputeMissingMessagesHandler(): execution(* MessageResourceBundle.internalComputeMissingMessages(..));

    pointcut Core_internalLoadHandler(): execution (* MessageResourceBundle.internalLoad(..));

    void around(): Core_internalPutHandle() {
        try
        {
            proceed();
        }
        catch (Exception e)
        {
            MessageResourceBundle.log(MessageResourceBundle.SEVERITY_ERROR,
                    "Exception setting field value.", e); //$NON-NLS-1$
        }
    }

    void around(String bundleName, boolean isAccessible, Field field): 
        Core_internalComputeMissingMessagesHandler() && args(bundleName, isAccessible, field){
        try
        {
            proceed(bundleName, isAccessible, field);
        }
        catch (Exception e)
        {
            MessageResourceBundle.log(MessageResourceBundle.SEVERITY_ERROR,
                    "Error setting the missing message value for: " + field.getName(), e); //$NON-NLS-1$
        }
    }

    void around(final String bundleName, boolean isAccessible, Map fields, final String[] variants,
            int i, final InputStream input): 
                Core_internalLoadHandler() &&
                args (bundleName, isAccessible, fields, variants, i, input){
        try
        {
            proceed(bundleName, isAccessible, fields, variants, i, input);
        }
        catch (IOException e)
        {
            MessageResourceBundle.log(MessageResourceBundle.SEVERITY_ERROR,
                    "Error loading " + variants[i], e); //$NON-NLS-1$
        }
        finally
        {
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

}
