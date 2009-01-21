package com.atlassw.tools.eclipse.checkstyle;

import java.util.logging.Logger;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

public aspect CheckstylePluginHandle {
    pointcut trataGeral2():  withincode(* CheckstylePlugin.start(..))
    && call (* Logger.*(..));
    
    after() throwing(SecurityException e): trataGeral2(){
        CheckstyleLog.log(e);
    }
}
