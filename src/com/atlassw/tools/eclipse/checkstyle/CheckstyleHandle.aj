package com.atlassw.tools.eclipse.checkstyle;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

public aspect CheckstyleHandle {
    pointcut startHandle(): execution(* start(..));
    
    void around() throws Exception: startHandle(){
        try{
            proceed();
        }catch (SecurityException e) {
            CheckstyleLog.log(e);
        }
    }
}
