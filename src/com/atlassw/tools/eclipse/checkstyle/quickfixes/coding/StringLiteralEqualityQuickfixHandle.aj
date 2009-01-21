package com.atlassw.tools.eclipse.checkstyle.quickfixes.coding;


import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import  java.lang.reflect.InvocationTargetException;

public aspect StringLiteralEqualityQuickfixHandle
{
    declare soft : InvocationTargetException : replaceNodeHandler();
    declare soft : IllegalAccessException : replaceNodeHandler() ;
    declare soft : NoSuchMethodException : replaceNodeHandler() ;
    
    pointcut replaceNodeHandler():  execution(* StringLiteralEqualityQuickfix.replaceNode(..));
    
    
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
