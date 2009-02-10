package com.atlassw.tools.eclipse.checkstyle.projectconfig;

import org.xml.sax.SAXException;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

public class ProjectconfigHandle
{
    public void rethrows(Exception e) throws CheckstylePluginException {
        CheckstylePluginException.rethrow(e); // wrap the exception
    }
    
    public void rethrows2(SAXException exception) throws CheckstylePluginException {
        Exception ex = exception.getException() != null ? exception.getException() : exception;
        CheckstylePluginException.rethrow(ex);
    }
    
    public void rethrows(Exception e, String message) throws CheckstylePluginException {
        CheckstylePluginException.rethrow(e, message);
    }
    
    public void rethrowSAXException(Exception e) throws SAXException {
        throw new SAXException(e);
    }
    
    public void cloneNotSupported() throws InternalError {
        throw new InternalError();
    }
    
    public void checkstyleLog(Exception e) {
        CheckstyleLog.log(e);
    }
}
