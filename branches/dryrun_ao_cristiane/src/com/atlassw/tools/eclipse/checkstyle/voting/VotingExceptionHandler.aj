package com.atlassw.tools.eclipse.checkstyle.voting;

import java.io.IOException;

import com.atlassw.tools.eclipse.checkstyle.exception.ExceptionHandler;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

public privileged aspect VotingExceptionHandler
{
    // ---------------------------
    // Pointcut
    // ---------------------------
    pointcut VotingPreferencePage_castHandler(): 
        call(* Vote.cast(..)) &&
        withincode(* VotingPreferencePage.PageController.widgetSelected(..)) ;
    
    // ---------------------------
    // Declare soft
    // ---------------------------
    declare soft : IOException : VotingPreferencePage_castHandler();

    // ---------------------------
    // Advice
    // ---------------------------
    void around() : VotingPreferencePage_castHandler() {
        try
        {
            proceed();
        }
        catch (IOException e)
        {
            VotingPreferencePage vPP = (VotingPreferencePage) thisJoinPoint.getThis();
            CheckstyleLog.errorDialog(vPP.getShell(), e, false);
        }
    }
}
