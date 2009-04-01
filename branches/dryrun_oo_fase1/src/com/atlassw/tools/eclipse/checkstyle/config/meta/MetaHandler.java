
package com.atlassw.tools.eclipse.checkstyle.config.meta;

import org.xml.sax.SAXException;

import com.atlassw.tools.eclipse.checkstyle.config.XMLTags;
import com.atlassw.tools.eclipse.checkstyle.exception.ExceptionHandler;
import com.atlassw.tools.eclipse.checkstyle.exception.GeneralException;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

@ExceptionHandler
public class MetaHandler extends GeneralException
{
    public String metadataFactoryCreateGenericMetadata(String parent)
    {
        // Ok we tried... default to TreeWalker
        return parent = XMLTags.TREEWALKER_MODULE;
    }

    public void metadataFactoryDoInitializationHandler(ClassLoader contextClassLoader)
    {
        Thread.currentThread().setContextClassLoader(contextClassLoader);
    }

    public void throwSAXException_Msg(String msg, Exception e) throws SAXException
    {
        throw new SAXException(msg, e);
    }

    public int metadataInternalFactorystartElement(Exception e, int priority)
    {
        CheckstyleLog.log(e);
        return priority = Integer.MAX_VALUE;
    }

    public String metadataFactoryLocalize(String localizationCandidate)
    {
        return localizationCandidate;
    }

}
