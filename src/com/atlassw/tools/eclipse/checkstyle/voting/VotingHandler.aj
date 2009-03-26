package com.atlassw.tools.eclipse.checkstyle.voting;

import java.io.IOException;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

public privileged aspect VotingHandler
{
    // ---------------------------
    // Declare soft
    // ---------------------------
    declare soft : IOException : VotingPreferencePage_castHandler();

    // ---------------------------
    // Pointcut
    // ---------------------------
    pointcut VotingPreferencePage_castHandler(): 
        call(* Vote.cast(..)) &&
        withincode(* VotingPreferencePage.PageController.widgetSelected(..)) ;

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
