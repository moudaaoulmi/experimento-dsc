
package com.atlassw.tools.eclipse.checkstyle.config.meta;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import java.io.IOException;
import java.util.MissingResourceException;
import javax.xml.parsers.ParserConfigurationException;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.config.Module;
import com.atlassw.tools.eclipse.checkstyle.config.XMLTags;
import java.io.InputStream;
import java.util.ResourceBundle;
import org.apache.commons.io.IOUtils;
import org.xml.sax.Attributes;
import org.eclipse.osgi.util.NLS;
import org.xml.sax.InputSource;

public privileged aspect MetaHandler
{
    // ---------------------------
    // Declare Soft's
    // ---------------------------
    declare soft: Exception : metadataFactory_internalRuleMetadataHandler() || 
                              metadataFactory_internalStartElementHandler();

    declare soft: CheckstylePluginException : metadataFactory_startElementHandler();

    declare soft: SAXParseException : metadataFactory_internalDoInitializationHandler();

    declare soft: SAXException : metadataFactory_internalDoInitializationHandler();

    declare soft: ParserConfigurationException : metadataFactory_internalDoInitializationHandler();

    declare soft: IOException : metadataFactory_internalDoInitializationHandler() || 
                                metadataFactory_resolveEntityHandler();

    declare soft: ClassNotFoundException: metadataFactory_startElementHandler();

    declare soft: InstantiationException: metadataFactory_startElementHandler();

    declare soft: IllegalAccessException: metadataFactory_startElementHandler();

    // ---------------------------
    // Point cut's
    // ---------------------------
    pointcut metadataFactory_internalRuleMetadataHandler() : 
        execution(* MetadataFactory.internalRuleMetadata(..));

    pointcut metadataFactory_internalDoInitializationHandler() : 
        execution(* MetadataFactory.internalDoInitialization(..));

    pointcut metadataFactory_getMetadataI18NBundleHandler() : 
        execution(* MetadataFactory.getMetadataI18NBundle(..));

    // TODO VERIFICAR ESSE COM ROMULO
    pointcut metadataFactory_resolveEntityHandler() : 
        execution(* MetadataFactory.MetaDataHandler.internalResolveEntity(..));

    pointcut metadataFactory_internalStartElementHandler() : 
        execution(* MetadataFactory.MetaDataHandler.internalStartElement(..));

    pointcut metadataFactory_localizeHandler() : 
        execution(* MetadataFactory.MetaDataHandler.localize(..));

    // aqui
    pointcut metadataFactory_internalDoHandler() : 
        execution(* MetadataFactory.internalDo(..));

    pointcut metadataFactory_startElementHandler() : 
        execution(* MetadataFactory.MetaDataHandler.startElement(..));

    // ---------------------------
    // Advice's
    // ---------------------------
    String around(Module module, String parent): metadataFactory_internalRuleMetadataHandler() && args(module,parent){
        try
        {
            return proceed(module, parent);
        }
        catch (Exception e)
        {
            // Ok we tried... default to TreeWalker
            parent = XMLTags.TREEWALKER_MODULE;
        }
        return parent;
    }

    void around(ClassLoader customsLoader, String metadataFile, InputStream metadataStream): metadataFactory_internalDoInitializationHandler() && args(customsLoader,metadataFile,metadataStream){
        try
        {
            proceed(customsLoader, metadataFile, metadataStream);
        }
        catch (SAXParseException e)
        {
            CheckstyleLog.log(e, NLS.bind("Could not parse metadata file {0} at {1}:{2}", //$NON-NLS-1$
                    new Object[] { metadataFile, new Integer(e.getLineNumber()),
                        new Integer(e.getColumnNumber()) }));
        }
        catch (SAXException e)
        {
            CheckstyleLog.log(e, "Could not read metadata " + metadataFile); //$NON-NLS-1$
        }
        catch (ParserConfigurationException e)
        {
            CheckstyleLog.log(e, "Could not read metadata " + metadataFile); //$NON-NLS-1$
        }
        catch (IOException e)
        {
            CheckstyleLog.log(e, "Could not read metadata " + metadataFile); //$NON-NLS-1$
        }
        finally
        {
            IOUtils.closeQuietly(metadataStream);
        }
    }

    ResourceBundle around() : metadataFactory_getMetadataI18NBundleHandler(){
        try
        {
            return proceed();
        }
        catch (MissingResourceException e)
        {
            return null;
        }
    }

    InputSource around() throws SAXException : metadataFactory_resolveEntityHandler(){
        try
        {
            return proceed();
        }
        catch (IOException e)
        {
            throw new SAXException("" + e, e); //$NON-NLS-1$
        }
    }

    int around(Attributes attributes, int priority): metadataFactory_internalStartElementHandler() && args(attributes,priority){
        try
        {
            return proceed(attributes, priority);
        }
        catch (Exception e)
        {
            CheckstyleLog.log(e);
            priority = Integer.MAX_VALUE;
        }
        return priority;
    }

    String around(String localizationCandidate): metadataFactory_localizeHandler() && args(localizationCandidate){
        String retorno = localizationCandidate;
        try
        {
            return proceed(localizationCandidate);
        }
        catch (MissingResourceException e)
        {
            return retorno;
        }
    }

    void around(ClassLoader contextClassLoader): metadataFactory_internalDoHandler() 
        && args(contextClassLoader){
        try
        {
            proceed(contextClassLoader);
        }
        finally
        {
            // restore the original classloader
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }

    void around() throws SAXException : metadataFactory_startElementHandler(){
        try
        {
            proceed();
        }
        catch (CheckstylePluginException e)
        {
            throw new SAXException(e.getLocalizedMessage(), e);
        }
        catch (ClassNotFoundException e)
        {
            throw new SAXException(e.getLocalizedMessage(), e);
        }
        catch (InstantiationException e)
        {
            throw new SAXException(e.getLocalizedMessage(), e);
        }
        catch (IllegalAccessException e)
        {
            throw new SAXException(e.getLocalizedMessage(), e);
        }

    }
}
