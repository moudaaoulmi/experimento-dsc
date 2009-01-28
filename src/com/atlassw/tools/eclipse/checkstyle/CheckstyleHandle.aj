package com.atlassw.tools.eclipse.checkstyle;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

public aspect CheckstyleHandle {
    // ---------------------------
    // Pointcut's
    // ---------------------------
    pointcut startHandle(): execution(* start(..));
   
    // ---------------------------
    // Advice's
    // ---------------------------
    void around() throws Exception: startHandle(){
        try{
            proceed();
        }catch (SecurityException e) {
            CheckstyleLog.log(e);
        }
    }
}
