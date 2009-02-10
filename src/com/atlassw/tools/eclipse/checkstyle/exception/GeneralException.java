package com.atlassw.tools.eclipse.checkstyle.exception;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

public class GeneralException
{
    
    public void checkstyleLog(Exception e)
    {
        CheckstyleLog.log(e);
    }

    public void checkstyleLog_E_MSG(Exception e, String msg)
    {
        CheckstyleLog.log(e, msg);
    }
}
