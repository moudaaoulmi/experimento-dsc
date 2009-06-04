
package com.atlassw.tools.eclipse.checkstyle.config;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

import com.atlassw.tools.eclipse.checkstyle.builder.PackageObjectFactory;
import com.atlassw.tools.eclipse.checkstyle.exception.ExceptionHandler;
import com.atlassw.tools.eclipse.checkstyle.exception.GeneralException;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.SeverityLevel;

@ExceptionHandler
public class ConfigHandler extends GeneralException
{

    public Object rethrowSAXException_MSG(String msg, Exception e) throws SAXException
    {
        throw new SAXException(msg, e);
    }
    
    public void severityModule(Module module)
    {
        module.setSeverity(SeverityLevel.WARNING);
    }
    
    public void restoreClassLoader(ClassLoader classloader, InputStream in){
        IOUtils.closeQuietly(in);
        Thread.currentThread().setContextClassLoader(classloader);
    }
}
