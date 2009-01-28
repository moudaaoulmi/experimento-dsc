/* 
 * 25th, November, 2008 
 */
package com.atlassw.tools.eclipse.checkstyle.voting;

import java.io.IOException;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

/**
 * @author juliana
 *
 */
public privileged aspect VotingHandler {
    declare soft : IOException : castHandler();

    pointcut castHandler(): call(* Vote.cast(..)) &&
        withincode(* VotingPreferencePage.PageController.widgetSelected(..)) ;

    void around() : castHandler() {
        try{
            proceed();
        }catch (IOException e1)
        {
            VotingPreferencePage v = (VotingPreferencePage)thisJoinPoint.getThis();
            CheckstyleLog.errorDialog(v.getShell(), e1, false);
        }

    }//around()

}//VotingPreferencePageHandler{}
