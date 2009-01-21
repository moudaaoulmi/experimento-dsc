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
public privileged aspect VotingPreferencePageHandler {
    
declare soft : Exception : castHandler();
    
    pointcut castHandler(): call(* Vote.cast(..)) &&
                           withincode(* VotingPreferencePage.PageController.widgetSelected(..)) ;
    
    void around() : castHandler() {
        VotingPreferencePage v = (VotingPreferencePage)thisJoinPoint.getThis();
            try{
                 proceed();
            }catch (IOException e1)
            {
                CheckstyleLog.errorDialog(v.getShell(), e1, false);
            }
            
    }//around()

}//VotingPreferencePageHandler{}
