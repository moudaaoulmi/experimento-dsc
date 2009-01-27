package org.eclipse.osgi.framework.internal.core;

import java.lang.Exception;
import java.lang.reflect.Field;

import java.io.IOException;
import java.io.InputStream;

import java.util.Map;


public privileged aspect CoreHandler
{

    // ---------------------------
    // Declare soft's
    // ---------------------------
    declare soft : Exception : MessageResourceBundle_setHandler() || 
                               MessageResourceBundle_internalComputeMissingMessagesHandler();

    declare soft: IOException: MessageResourceBundle_internalLoadHandler() ||
                               MessageResourceBundle_internalInternalLoadHandler() ;

    // ---------------------------
    // Pointcut's
    // ---------------------------
    pointcut MessageResourceBundle_setHandler():
        execution (* MessageResourceBundle.MessagesProperties.internalPut(..));

    pointcut MessageResourceBundle_internalComputeMissingMessagesHandler():
        execution (* MessageResourceBundle.internalComputeMissingMessages(..));

    pointcut MessageResourceBundle_internalInternalLoadHandler():
         execution(* MessageResourceBundle.internalInternalLoad(..));

    pointcut MessageResourceBundle_internalLoadHandler():
          execution(* MessageResourceBundle.internalLoad(..)); //&&
          //withincode (* MessageResourceBundle.internalLoad());

    // ---------------------------
    // Advice's
    // ---------------------------
    void around(): MessageResourceBundle_setHandler(){
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
        MessageResourceBundle_internalComputeMissingMessagesHandler() 
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

    void around(): MessageResourceBundle_internalInternalLoadHandler(){
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
            final String[] variants, int i, final InputStream input): 
                MessageResourceBundle_internalLoadHandler() &&
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

