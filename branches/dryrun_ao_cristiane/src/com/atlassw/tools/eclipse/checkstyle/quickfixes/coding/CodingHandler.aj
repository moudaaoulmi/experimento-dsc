package com.atlassw.tools.eclipse.checkstyle.quickfixes.coding;

import com.atlassw.tools.eclipse.checkstyle.exception.ExceptionHandler;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import  java.lang.reflect.InvocationTargetException;
import org.eclipse.jdt.core.dom.ASTVisitor;

@ExceptionHandler
public aspect CodingHandler
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
        execution(private void replaceNode(..)) && 
        within(StringLiteralEqualityQuickfix) && 
        within(ASTVisitor+);
    
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
