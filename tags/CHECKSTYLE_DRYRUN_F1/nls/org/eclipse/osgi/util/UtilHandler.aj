/*
 * 25th, November, 2008
 */
package org.eclipse.osgi.util;

import java.lang.Integer;
import java.lang.NumberFormatException;
import java.lang.IllegalArgumentException;


/**
 * @author juliana
 *
 */
public aspect UtilHandler {
    
    pointcut NLS_internalBindHandler(): call(* Integer.parseInt(..)) &&
                withincode(* NLS.internalBind(..));
    
    int around() : NLS_internalBindHandler() {
            try{
                return proceed();
            }catch (NumberFormatException e) {
                throw new IllegalArgumentException();
            }//catch()
            
    }//around()

}//NLSHandler{}
