package org.eclipse.osgi.util;

import java.lang.Integer;
import java.lang.NumberFormatException;
import java.lang.IllegalArgumentException;


public aspect UtilHandler
{
    // ---------------------------
    // Pointcut
    // ---------------------------
    pointcut NLS_internalBindHandler(): 
        call(* Integer.parseInt(..)) &&
        withincode(* NLS.internalBind(..));

    // ---------------------------
    // Advice
    // ---------------------------
    int around() : NLS_internalBindHandler() {
        try
        {
            return proceed();
        }
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException();
        }

    }

}
