
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
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

public class MetaHandler
{

    public void metadataFactoryCreateGenericMetadata(String parent)
    {
        // Ok we tried... default to TreeWalker
        parent = XMLTags.TREEWALKER_MODULE;
    }

    public void refreshHandler(CheckstylePluginException e)
    {
        CheckstyleLog.log(e);
    }

    public void metadataFactoryInternalDoInitializationHandler1(SAXParseException e,
            String metadataFile)
    {
        CheckstyleLog.log(e, NLS.bind("Could not parse metadata file {0} at {1}:{2}", //$NON-NLS-1$
                new Object[] { metadataFile, new Integer(e.getLineNumber()),
                    new Integer(e.getColumnNumber()) }));
    }

    public void metadataFactoryInternalDoInitializationHandler2(Exception e, String metadataFile)
    {
        CheckstyleLog.log(e, "Could not read metadata " + metadataFile); //$NON-NLS-1$
    }

    public void metadataFactoryInternalDoInitializationHandler3(InputStream metadataStream)
    {
        IOUtils.closeQuietly(metadataStream);
    }

    public void metadataFactoryDoInitializationHandler(ClassLoader contextClassLoader)
    {
        Thread.currentThread().setContextClassLoader(contextClassLoader);
    }

    public void metadataFactoryResolveEntityHandler(IOException e) throws SAXException
    {
        throw new SAXException("" + e, e); //$NON-NLS-1$  
    }

    public void metadataInternalFactorystartElement(Exception e, int priority)
    {
        CheckstyleLog.log(e);
        priority = Integer.MAX_VALUE;
    }

    public void metadataFactorystartElement(Exception e) throws SAXException
    {
        throw new SAXException(e.getLocalizedMessage(), e);
    }

    public String metadataFactoryLocalize(String localizationCandidate)
    {
        return localizationCandidate;
    }

}
