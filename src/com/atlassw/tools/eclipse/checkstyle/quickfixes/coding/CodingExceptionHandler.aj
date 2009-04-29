package com.atlassw.tools.eclipse.checkstyle.quickfixes.coding;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import  java.lang.reflect.InvocationTargetException;

public privileged aspect CodingExceptionHandler
{
    // ---------------------------
    // Declare soft's
    // ---------------------------
    declare soft : InvocationTargetException : StringLiteralEqualityQuickfix_replaceNodeHandler();
    
    declare soft : IllegalAccessException : StringLiteralEqualityQuickfix_replaceNodeHandler() ;
    
    declare soft : NoSuchMethodException : StringLiteralEqualityQuickfix_replaceNodeHandler() ;
    
    // ---------------------------
    // Pointcut's
    // ---------------------------   
    pointcut StringLiteralEqualityQuickfix_replaceNodeHandler():  
        execution(* StringLiteralEqualityQuickfix.ASTVisitorImplementation.replaceNode(..));
    
    // ---------------------------
    // Advice's
    // ---------------------------    
    void around(): StringLiteralEqualityQuickfix_replaceNodeHandler() {
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
