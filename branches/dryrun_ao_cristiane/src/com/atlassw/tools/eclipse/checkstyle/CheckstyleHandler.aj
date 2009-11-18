package com.atlassw.tools.eclipse.checkstyle;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

@com.atlassw.tools.eclipse.checkstyle.exception.ExceptionHandler
public aspect CheckstyleHandler
{
    pointcut startHandle(): execution(* start(..));
   
    void around() throws Exception: startHandle(){
        try{
            proceed();
        }catch (SecurityException e) {
          //XXX LOG  - nao dah p gerenalizar totalmente
            CheckstyleLog.log(e);
        }
    }

}
