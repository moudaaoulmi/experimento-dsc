package com.atlassw.tools.eclipse.checkstyle.duplicates;

import java.util.MissingResourceException;
import java.lang.NumberFormatException;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

public aspect DuplicatedCodeHandler
{
    pointcut internalHandler() : execution(* DuplicatedCode.internal(..));
    pointcut internalGetNumberOfDuplicatedLinesHandler() : execution(* DuplicatedCode.internalGetNumberOfDuplicatedLines(..));
    
    void around() : internalHandler(){
        String DUPLICATES_MESSAGE_BUNDLE = "com.puppycrawl.tools.checkstyle.checks.duplicates.messages";
        try{
            proceed();
        }
        catch (MissingResourceException e){
            CheckstyleLog.log(e, "Unable to get the resource bundle " //$NON-NLS-1$
                    + DUPLICATES_MESSAGE_BUNDLE + "."); //$NON-NLS-1$
        }
    }
    
    int around(String number, int result): internalGetNumberOfDuplicatedLinesHandler() && args(number,result){
        try {
            return proceed(number,result);
        }catch (NumberFormatException e){
            result = 0;
        }
        return result;
    }
}
