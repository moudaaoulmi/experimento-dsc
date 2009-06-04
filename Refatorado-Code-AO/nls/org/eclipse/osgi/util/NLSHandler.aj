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
public aspect NLSHandler {
    
    pointcut internalBindHandler(): call(* Integer.parseInt(..)) &&
                withincode(* NLS.internalBind(..));
    
    int around() : internalBindHandler() {
            try{
                return proceed();
            }catch (NumberFormatException e) {
                throw new IllegalArgumentException();
            }//catch()
            
    }//around()

}//NLSHandler{}
