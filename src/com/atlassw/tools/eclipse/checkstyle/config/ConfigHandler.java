package com.atlassw.tools.eclipse.checkstyle.config;

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
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

/**
 * 
 * @author julianasaraiva
 *
 */

public class ConfigHandler {
    
    //catch (CheckstylePluginException e)
   public void refreshHandler( Exception e) {
        CheckstyleLog.log(e);
    }

   //catch (Exception e)
   public void exportConfigurationHandler(Exception e) throws CheckstylePluginException{
       CheckstylePluginException.rethrow(e);
   }
   //finally
   public void exportConfigurationFINALLYHandler(InputStream in, OutputStream out){
       IOUtils.closeQuietly(in);
       IOUtils.closeQuietly(out);
   }
   
   //catch (Exception e)
   public void loadFromPersistence(Exception e) throws CheckstylePluginException{
       CheckstylePluginException.rethrow(e, ErrorMessages.errorLoadingConfigFile);
   }
   //finally
   public void  loadFromPersistenceFINALLY (InputStream inStream){
       IOUtils.closeQuietly(inStream);
   }
   
   //catch (Exception e)
   public void migrateHandler(Exception e) throws CheckstylePluginException {
       CheckstylePluginException.rethrow(e, ErrorMessages.errorMigratingConfig);
   }
   
   //finally
   public void migrateFINALLYHandler(InputStream inStream, InputStream defaultConfigStream){ 
       IOUtils.closeQuietly(inStream);
       IOUtils.closeQuietly(defaultConfigStream);
   }
   
   //catch (Exception e)
   public void endElementHandler(Exception e) throws SAXException {
       throw new SAXException(e);
   }
   
   //catch (CheckstyleException ex)
   public Object createModuleHandler(String aName, PackageObjectFactory class_) throws CheckstyleException{
       return class_.packageObjectFactory_createModuleHandler(aName);
   }
   
}//ConfigHandler{}
