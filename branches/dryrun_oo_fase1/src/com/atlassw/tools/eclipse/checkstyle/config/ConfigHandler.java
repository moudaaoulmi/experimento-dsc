
package com.atlassw.tools.eclipse.checkstyle.config;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.JavaModelException;
import org.xml.sax.SAXException;

import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.builder.BuilderHandler;
import com.atlassw.tools.eclipse.checkstyle.builder.PackageObjectFactory;
import com.atlassw.tools.eclipse.checkstyle.exception.GeneralException;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

/**
 * @author julianasaraiva
 */

public class ConfigHandler extends GeneralException
{
    // -------------------------------
    // Não da pra reusar pois mesmo com os metodos tendo o mesmo nome,
    // eles não aceitam um Object, e sim apenas OutputStream e inputStream,
    // respectivamente.
    public void exportConfigurationHandler(InputStream in, OutputStream out)
    {
        IOUtils.closeQuietly(in);
        IOUtils.closeQuietly(out);
    }

    public void migrateFINALLYHandler(InputStream inStream, InputStream defaultConfigStream)
    {
        IOUtils.closeQuietly(inStream);
        IOUtils.closeQuietly(defaultConfigStream);
    }
    
    public void storeToPersistenceHandler (ByteArrayOutputStream byteOut, BufferedOutputStream out){
        IOUtils.closeQuietly(byteOut);
        IOUtils.closeQuietly(out);
    }
    // -------------------------------

    public void endElementHandler(Exception e) throws SAXException
    {
        throw new SAXException(e);
    }

    public Object createModuleHandler(String aName, PackageObjectFactory class_)
        throws CheckstyleException
    {
        return class_.packageObjectFactory_createModuleHandler(aName);
    }

}
