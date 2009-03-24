
package org.eclipse.osgi.framework.internal.core;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import org.eclipse.osgi.framework.internal.core.MessageResourceBundle.*;

public privileged aspect CoreHandler
{
    declare soft: Exception: Core_putHandle() ||
                             Core_internalComputeMissingMessagesHandler();

    declare soft: IOException: Core_internalLoadHandler();

    pointcut Core_putHandle(): 
        call(* Field.set(..)) &&
        withincode(* MessageResourceBundle.MessagesProperties.put(..));

    pointcut Core_internalComputeMissingMessagesHandler(): 
        execution(* MessageResourceBundle.internalComputeMissingMessages(..));

    pointcut Core_internalLoadHandler(): 
        execution (* MessageResourceBundle.internalLoad(..));

    void around(): Core_putHandle() {
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

    void around(Field field, String value): 
                Core_internalComputeMissingMessagesHandler() && 
                args(field, value){
        try
        {
            proceed(field, value);
        }
        catch (Exception e)
        {
            MessageResourceBundle.log(MessageResourceBundle.SEVERITY_ERROR,
                    "Error setting the missing message value for: " + field.getName(), e); //$NON-NLS-1$
        }
    }

    void around(MessagesProperties properties, final String[] variants,int i, final InputStream input): 
                Core_internalLoadHandler() &&
                args (properties, variants, i, input){
        try
        {
            proceed(properties, variants, i, input);
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
