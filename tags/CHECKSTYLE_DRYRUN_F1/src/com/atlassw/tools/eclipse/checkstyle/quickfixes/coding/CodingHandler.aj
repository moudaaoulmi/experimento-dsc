package com.atlassw.tools.eclipse.checkstyle.quickfixes.coding;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import  java.lang.reflect.InvocationTargetException;

public aspect CodingHandler
{

    // ---------------------------
    // Declare soft's
    // ---------------------------
    declare soft : InvocationTargetException : replaceNodeHandler();
    declare soft : IllegalAccessException : replaceNodeHandler() ;
    declare soft : NoSuchMethodException : replaceNodeHandler() ;
    
    // ---------------------------
    // Pointcut's
    // ---------------------------   
    pointcut replaceNodeHandler():  execution(* StringLiteralEqualityQuickfix.replaceNode(..));
    
    // ---------------------------
    // Advice's
    // ---------------------------    
    void around(): replaceNodeHandler() {
        try{
            proceed();
        } 
        catch (InvocationTargetException e)
        {
            CheckstyleLog.log(e);
        }
        catch (IllegalAccessException e)
        {
            CheckstyleLog.log(e);
        }
        catch (NoSuchMethodException e)
        {
            CheckstyleLog.log(e);
        }
    }
}
