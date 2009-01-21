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

public privileged aspect MetadataFactoryHandler
{
    declare soft: Exception : internalRuleMetadataHandler() || internalStartElementHandler();
    declare soft: CheckstylePluginException : refreshHandler() || startElementHandler();
    declare soft: SAXParseException : internalDoInitializationHandler();
    declare soft: SAXException : internalDoInitializationHandler();
    declare soft: ParserConfigurationException : internalDoInitializationHandler();
    declare soft: IOException : internalDoInitializationHandler() || resolveEntityHandler();
    declare soft: ClassNotFoundException: startElementHandler();
    declare soft: InstantiationException: startElementHandler();
    declare soft: IllegalAccessException: startElementHandler();
    
    pointcut internalRuleMetadataHandler() : execution(* MetadataFactory.internalRuleMetadata(..));
    pointcut refreshHandler() : call (* MetadataFactory.doInitialization(..)) && withincode(* MetadataFactory.refresh(..));
    pointcut internalDoInitializationHandler() : execution(* MetadataFactory.internalDoInitialization(..));
    pointcut getMetadataI18NBundleHandler() : execution(* MetadataFactory.getMetadataI18NBundle(..));
    pointcut resolveEntityHandler() : execution(* MetadataFactory.MetaDataHandler.internalResolveEntity(..));
    pointcut internalStartElementHandler() : execution(* MetadataFactory.MetaDataHandler.internalStartElement(..));
    pointcut localizeHandler() : execution(* MetadataFactory.MetaDataHandler.localize(..));
    pointcut internalDoHandler() : execution(* MetadataFactory.internalDo(..));
    pointcut startElementHandler() : execution(* MetadataFactory.MetaDataHandler.startElement(..));
    
    String around(Module module,String parent): internalRuleMetadataHandler() && args(module,parent){
        try {
            return proceed(module,parent);
        }catch (Exception e){
            // Ok we tried... default to TreeWalker
            parent = XMLTags.TREEWALKER_MODULE;
        }
        return parent;
    }
    
    void around() : refreshHandler(){
        try{
            proceed();
        }
        catch (CheckstylePluginException e){
            CheckstyleLog.log(e);
        }
    }
    void around(ClassLoader customsLoader, String metadataFile,
            InputStream metadataStream): internalDoInitializationHandler() && args(customsLoader,metadataFile,metadataStream){
        try {
            proceed(customsLoader,metadataFile,metadataStream);
        }
        catch (SAXParseException e){
            CheckstyleLog.log(e, NLS.bind("Could not parse metadata file {0} at {1}:{2}", //$NON-NLS-1$
                    new Object[] { metadataFile, new Integer(e.getLineNumber()),
                        new Integer(e.getColumnNumber()) }));
        }
        catch (SAXException e){
            CheckstyleLog.log(e, "Could not read metadata " + metadataFile); //$NON-NLS-1$
        }
        catch (ParserConfigurationException e){
            CheckstyleLog.log(e, "Could not read metadata " + metadataFile); //$NON-NLS-1$
        }
        catch (IOException e){
            CheckstyleLog.log(e, "Could not read metadata " + metadataFile); //$NON-NLS-1$
        }
        finally
        {
            IOUtils.closeQuietly(metadataStream);
        }
    }
    ResourceBundle around() : getMetadataI18NBundleHandler(){
        try{
            return proceed();
        }
        catch (MissingResourceException e){
            return null;
        }
    }

    after() throwing(Exception e) throws SAXException : 
            resolveEntityHandler(){    
            throw new SAXException("" + e, e); //$NON-NLS-1$
    }
    
    int around(Attributes attributes,int priority): internalStartElementHandler() && args(attributes,priority){
        try {
            return proceed(attributes,priority);
        }catch (Exception e){
            CheckstyleLog.log(e);
            priority = Integer.MAX_VALUE;
        }
        return priority;
    }
    
    String around(String localizationCandidate): localizeHandler() && args(localizationCandidate){
        String retorno = localizationCandidate;
        try {
            return proceed(localizationCandidate);
        }catch (MissingResourceException e){
            return retorno;
        }
    }
    void around(ClassLoader contextClassLoader): internalDoHandler() && args(contextClassLoader){
        try {
            proceed(contextClassLoader);
        }finally{
            // restore the original classloader
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }
    
    void around() throws SAXException : startElementHandler(){
        try{
            proceed();
        }catch (CheckstylePluginException e){
            throw new SAXException(e.getLocalizedMessage(), e);
        }catch (ClassNotFoundException e){
            throw new SAXException(e.getLocalizedMessage(), e);
        }catch (InstantiationException e){
            throw new SAXException(e.getLocalizedMessage(), e);
        }catch (IllegalAccessException e){
            throw new SAXException(e.getLocalizedMessage(), e);
        }

    }
}
