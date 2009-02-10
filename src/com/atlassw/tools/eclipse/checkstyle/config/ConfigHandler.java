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
   
   //catch (CoreException e)
   public void runHandler(CoreException e, BuilderHandler builderHandler, IStatus status){
       status = builderHandler.buildProjectJob_runHandler(e);
   }
   //finally
   public void runFINALLYHandler(BuilderHandler builderHandler , IProgressMonitor monitor){
       builderHandler.buildProjectJob_runHandler2(monitor);
   }
   
   //catch (CoreException e)
   public void buildProjectsHandler(Exception e, BuilderHandler builderHandler) throws CheckstylePluginException{
       builderHandler.builderHandlerRethrowException(e);
   }
   
  // catch (CheckstylePluginException e)
   public void  handleBuildSelectionHandler(CheckstylePluginException e , BuilderHandler builderHandler) throws CoreException{
       builderHandler.builderHandlerRethrowCoreException(e);
   }
   
   //catch (CheckstylePluginException e)
   public void  buildHandler(CheckstylePluginException e, BuilderHandler builderHandler) throws CoreException{
       builderHandler.checkstyleBuilder_buildHandler(e);
   }
   
   
   //catch (ParserConfigurationException e)
   public void getPackageNames1Handler(ParserConfigurationException e, BuilderHandler builderHandler, String DEFAULT_PACKAGES) throws CheckstylePluginException{
       builderHandler.packageNamesLoader_getPackageNames(e, DEFAULT_PACKAGES);
   }
   
   //catch (SAXException e)
   public void getPackageNames2Handler(SAXException e, BuilderHandler builderHandler , String DEFAULT_PACKAGES) throws CheckstylePluginException{
       builderHandler.packageNamesLoader_getPackageNames2(e, DEFAULT_PACKAGES);
   }
   
   //catch (IOException e)
   public void getPackageNames3Handler(IOException e, BuilderHandler builderHandler , String DEFAULT_PACKAGES) throws CheckstylePluginException{
       builderHandler.packageNamesLoader_getPackageNames3(e, DEFAULT_PACKAGES);                
   }
   
   //catch (SAXException e)
   public void getPackageNames4Handler(SAXException e, URL  aPackageFile,  BuilderHandler builderHandler){
       builderHandler.packageNamesLoader_getPackageNames4(aPackageFile, e);
   }
   
   //catch (IOException e)
   public void getPackageNames5Handler (IOException e, URL  aPackageFile, BuilderHandler builderHandler){ 
       builderHandler.packageNamesLoader_getPackageNames5(aPackageFile, e);
   }
   
   //finally
   public void getPackageNames6Handler (InputStream iStream, BuilderHandler builderHandler ){
       builderHandler.packageNamesLoader_getPackageNames6(iStream);
   }
   
   //catch (IOException e1)
   public void getPackageNames7Handler (IOException e1, BuilderHandler builderHandler ) throws CheckstylePluginException{
       builderHandler.builderHandlerRethrowException(e1);
   }
   
   //catch (CheckstyleException ex
   public void doMakeObjectHandler(CheckstyleException ex, BuilderHandler builderHandler){
       builderHandler.projectObjectFactory_doMakeObjectHandler();
   }
   
   //catch (ClassNotFoundException e)
   public void createObject1Handler(ClassNotFoundException e, BuilderHandler builderHandler, String aClassName) throws CheckstyleException{
       builderHandler.packageObjectFactory_createObjectHandler2(aClassName, e);
   }
   
 //catch (InstantiationException e)
   public void createObject2Handler(InstantiationException e, BuilderHandler builderHandler, String aClassName) throws CheckstyleException{
       builderHandler.packageObjectFactory_createObjectHandler2(aClassName, e);
   }
   
   
   //catch (IllegalAccessException e)
   public void createObject3Handler(IllegalAccessException e, BuilderHandler builderHandler, String aClassName) throws CheckstyleException{
       builderHandler.packageObjectFactory_createObjectHandler2(aClassName, e);
   }
   
   //catch (CheckstyleException ex)
   public Object createModuleHandler(String aName, PackageObjectFactory class_) throws CheckstyleException{
       return class_.packageObjectFactory_createModuleHandler(aName);
   }
   
   //catch (CheckstyleException ex2)
   public void packageObjectFactory_createModuleHandler(String aName, CheckstyleException ex2, BuilderHandler builderHandler) throws CheckstyleException{
       builderHandler.packageObjectFactory_createModuleHandlerHandler(aName, ex2);          
   }
   
   //catch (JavaModelException jme)
   public void addToClassPathHandler(BuilderHandler builderHandler, JavaModelException jme){
       builderHandler.builderHandlerCheckstyleLog(jme);
   }
   
   //catch (MalformedURLException mfe)
   public void  handlePathHandler(MalformedURLException mfe, BuilderHandler builderHandler){
       builderHandler.projectClassLoader_handlePathHandler(mfe);
   }
   
  // catch (CheckstylePluginException e)
  public void runInWorkspaceHandler(CheckstylePluginException e, BuilderHandler builderHandler) throws CoreException {
       builderHandler.builderHandlerRethrowCoreException(e);
   }
   
}//ConfigHandler{}
