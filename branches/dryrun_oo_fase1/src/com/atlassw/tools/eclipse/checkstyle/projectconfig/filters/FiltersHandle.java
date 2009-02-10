package com.atlassw.tools.eclipse.checkstyle.projectconfig.filters;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

public class FiltersHandle
{
    public void cloneNotSupported() throws InternalError {
        // this shouldn't happen, since we are Cloneable
        throw new InternalError();
    }
    
    public void checkstyleLog(Exception e) {
        CheckstyleLog.log(e);
    }
    
    public void commentedCode() {
        // this should never happen because we call
        // #isAccessible before invoking #members
    }
}
