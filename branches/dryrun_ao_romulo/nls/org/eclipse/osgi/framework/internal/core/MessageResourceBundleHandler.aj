/*
 * 25th, November, 2008
 */

package org.eclipse.osgi.framework.internal.core;

import java.lang.Exception;
import java.lang.reflect.Field;
import java.io.IOException;
import java.util.Map;
import java.io.InputStream;

/**
 * @author juliana
 */
public privileged aspect MessageResourceBundleHandler
{
    // ---------------------------
    // Declare soft's
    // ---------------------------
    declare soft : Exception : setHandler();

    declare soft: Exception: internalComputeMissingMessagesHandler();

    declare soft: IOException: internalLoadHandler() ||
    internalInternalLoadHandler() ;

    // ---------------------------
    // Pointcut's
    // ---------------------------
    pointcut setHandler():
        execution (* MessageResourceBundle.MessagesProperties.internalPut(..));

    pointcut internalComputeMissingMessagesHandler():
        execution (* MessageResourceBundle.internalComputeMissingMessages(..));

    pointcut internalInternalLoadHandler():
         execution(* MessageResourceBundle.internalInternalLoad(..));

    pointcut internalLoadHandler():
          execution(* MessageResourceBundle.internalLoad(..)); //&&
          //withincode (* MessageResourceBundle.internalLoad());

    // ---------------------------
    // Advice's
    // ---------------------------
    void around(): setHandler(){
        MessageResourceBundle m = (MessageResourceBundle) thisJoinPoint.getThis();
        try
        {
            proceed();
        }
        catch (Exception e)
        {
            m.log(m.SEVERITY_ERROR, "Exception setting field value.", e); //$NON-NLS-1$
        }
    }

    // // around()

    void around(boolean isAccessible, Field field, String bundleName):
        internalComputeMissingMessagesHandler() 
        && args(isAccessible, field, bundleName){
        MessageResourceBundle m = (MessageResourceBundle) thisJoinPoint.getThis();
        try
        {
            proceed(isAccessible, field, bundleName);
        }
        catch (Exception e)
        {
            m.log(m.SEVERITY_ERROR,
                    "Error setting the missing message value for: " + field.getName(), e); //$NON-NLS-1$
        }
    }

    void around(): internalInternalLoadHandler(){
        try
        {
            proceed();
        }
        catch (IOException e)
        {
            // ignore
        }
    }

    void around(final String bundleName, boolean isAccessible, Map fields,
            final String[] variants, int i, final InputStream input): internalLoadHandler() &&
            args(bundleName, isAccessible, fields, variants, i, input){

        MessageResourceBundle m = (MessageResourceBundle) thisJoinPoint.getThis();
        try
        {
            proceed(bundleName, isAccessible, fields, variants, i, input);
        }
        catch (IOException e)
        {
            m.log(m.SEVERITY_ERROR, "Error loading " + variants[i], e); //$NON-NLS-1$
        }
        

    }

}
