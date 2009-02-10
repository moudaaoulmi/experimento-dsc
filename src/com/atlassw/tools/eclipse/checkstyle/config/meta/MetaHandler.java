
package com.atlassw.tools.eclipse.checkstyle.config.meta;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

import org.apache.commons.io.IOUtils;
import org.eclipse.osgi.util.NLS;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.atlassw.tools.eclipse.checkstyle.config.XMLTags;
import com.atlassw.tools.eclipse.checkstyle.exception.GeneralException;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

public class MetaHandler extends GeneralException
{
    public void metadataFactoryCreateGenericMetadata(String parent)
    {
        // Ok we tried... default to TreeWalker
        parent = XMLTags.TREEWALKER_MODULE;
    }

    public void metadataFactoryDoInitializationHandler(ClassLoader contextClassLoader)
    {
        Thread.currentThread().setContextClassLoader(contextClassLoader);
    }

    public void throwSAXException_Msg(String msg, Exception e) throws SAXException
    {
        throw new SAXException(msg, e); //$NON-NLS-1$  
    }

    public void metadataInternalFactorystartElement(Exception e, int priority)
    {
        CheckstyleLog.log(e);
        priority = Integer.MAX_VALUE;
    }

    public String metadataFactoryLocalize(String localizationCandidate)
    {
        return localizationCandidate;
    }

}
