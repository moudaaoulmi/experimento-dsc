
package com.atlassw.tools.eclipse.checkstyle.config;

import org.eclipse.osgi.util.NLS;
import br.upe.dsc.reusable.exception.*;
import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.ProjectConfigurationFactory;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.puppycrawl.tools.checkstyle.api.SeverityLevel;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExceptionHandler
public privileged aspect ConfigExceptionHandler extends EmptyBlockAbstractExceptionHandling
{
    // ---------------------------
    // Atributos
    // ---------------------------
    
    Map<String,ByteArrayInputStream> byteArrayInputStream = new HashMap<String,ByteArrayInputStream>();

    Map<String,BufferedInputStream> inputStream = new HashMap<String,BufferedInputStream>();

    Map<String,BufferedOutputStream> outputStream = new HashMap<String,BufferedOutputStream>();

    Map<String,ByteArrayOutputStream> outputStreamByteOut = new HashMap<String,ByteArrayOutputStream>();

    // ---------------------------
    // Declare soft's
    // ---------------------------
    declare soft: Exception: CheckConfigurationWorkingCopy_setLocationHandle() 
                            || RetrowException_loadFromPersistenceHandle()
                            || RetrowException_migrateHandle()
                            || RetrowException_storeToPersistenceHandle();

    declare soft: CoreException: CheckConfigurationWorkingCopy_setModulesIterationHandle();

    declare soft: CheckstylePluginException: CheckstyleLogMessage_removeCheckConfigurationHandle()
                            || CheckConfigurationWorkingCopy_internalGetModules();

    declare soft: IOException: ConfigurationReaderHandle_startElementHandleHandle();

    // ---------------------------
    // Pointcut's
    // ---------------------------
    public pointcut emptyBlockException(): CheckConfigurationWorkingCopy_setModulesIterationHandle();
    
    pointcut CheckConfigurationWorkingCopy_setLocationHandle():
        execution (private void CheckConfigurationWorkingCopy.setLocationHandle(..)) ;

    pointcut CheckConfigurationWorkingCopy_setModulesIterationHandle(): 
        call (public void IResource.refreshLocal(..)) &&
        withincode (public void CheckConfigurationWorkingCopy.internalSetModules(..)) ;

    pointcut CheckstyleLogMessage_removeCheckConfigurationHandle():
        call(public boolean ProjectConfigurationFactory.isCheckConfigInUse(..)) &&
        withincode(public boolean GlobalCheckConfigurationWorkingSet.removeCheckConfiguration(..)) ;

    pointcut ConfigurationReaderHandle_getAdditionalConfigDataHandler(): 
        execution (private static int ConfigurationReader.internalGetAdditionalConfigData(String,int)) ;

    pointcut ConfigurationReaderHandle_startElementHandleHandle(): 
        execution (private void ConfigurationReader.ConfigurationHandler.startElementHandle(..)) ;

    pointcut RetrowException_loadFromPersistenceHandle(): 
        execution (private static void CheckConfigurationFactory.internalLoadFromPersistence(..)) ;

    pointcut RetrowException_migrateHandle(): 
        execution (private static void CheckConfigurationFactory.internalMigrate(..)) ;

    pointcut RetrowException_storeToPersistenceHandle(): 
        execution (private void GlobalCheckConfigurationWorkingSet.internalStoreToPersistence(..)) ;

    pointcut CheckConfigurationWorkingCopy_internalGetModules():
        execution(private List CheckConfigurationWorkingCopy.internalGetModules(..));
  
    
 
    pointcut getInputStreamInternalGetModulesHandler() :
        call(ByteArrayInputStream CheckstyleConfigurationFile.getCheckConfigFileStream()) &&
        withincode(private List CheckConfigurationWorkingCopy.internalGetModules(..));

    pointcut getInputStreamInternalLoadFromPersistenceHandler() :
        call(public BufferedInputStream.new(..)) &&
        withincode( private static void CheckConfigurationFactory.internalLoadFromPersistence(..));

    pointcut getInputStreamMigrateHandler() :
        call(public BufferedInputStream.new(..)) &&
        withincode(private static void CheckConfigurationFactory.internalMigrate(..));

    pointcut getOutputStreamStoreToPersistenceHandler() :
        call(public BufferedOutputStream.new(..)) &&
        withincode(private void GlobalCheckConfigurationWorkingSet.internalStoreToPersistence(..));

    pointcut getByteArrayOutputStreamStoreToPersistence1Handler() :
        call(public ByteArrayOutputStream.new(..)) &&
        withincode(private void GlobalCheckConfigurationWorkingSet.internalStoreToPersistence(..));

    // ---------------------------
    // Advice's
    // ---------------------------
    Object around(InputStream in, Object result) throws CheckstylePluginException:
                CheckConfigurationWorkingCopy_internalGetModules() &&
                args(in, result){
        try
        {
            result = proceed(in, result);
        }
        finally
        {
            IOUtils.closeQuietly((ByteArrayInputStream) byteArrayInputStream.get(Thread.currentThread().getName()));
        }
        return result;
    }

    void around(String location, String oldLocation) throws CheckstylePluginException: 
                CheckConfigurationWorkingCopy_setLocationHandle() 
                && args(location, oldLocation) {
        try
        {
            proceed(location, oldLocation);
        }
        catch (CheckstylePluginException e)
        {
            CheckConfigurationWorkingCopy c = (CheckConfigurationWorkingCopy) thisJoinPoint
                    .getThis();
            c.mEditedLocation = oldLocation;
            CheckstylePluginException.rethrow(e, NLS.bind(ErrorMessages.errorResolveConfigLocation,
                    location, e.getLocalizedMessage()));
        }
    }

//    void around(): CheckConfigurationWorkingCopy_setModulesIterationHandle()  {
//        try
//        {
//            proceed();
//        }
//        catch (CoreException e)
//        {
//            // NOOP - just ignore
//        }
//    }

    // esses dois nao podem ser reusados pq o com o retorno boolean está
    // utilizando valores iniciais diferentes do valor default.
    boolean around(): CheckstyleLogMessage_removeCheckConfigurationHandle()
        {
        boolean result = true;
        try
        {
            result = proceed();
        }
        catch (CheckstylePluginException e)
        {
          //XXX LOG - n dah p generalizar totalmente
            CheckstyleLog.log(e);
        }
        return result;
    }

    int around(String tabWidthProp, int tabWidth): 
            ConfigurationReaderHandle_getAdditionalConfigDataHandler() &&
            args(tabWidthProp, tabWidth){
        try
        {
            tabWidth = proceed(tabWidthProp, tabWidth);
        }
        catch (Exception se)
        {
            // ignore
        }
        return tabWidth;
    }

    void around(String value, Module module): 
            ConfigurationReaderHandle_startElementHandleHandle() &&
            args(value, module) {
        try
        {
            proceed(value, module);
        }
        catch (IllegalArgumentException e)
        {
            module.setSeverity(SeverityLevel.WARNING);
        }
    }

    void around(InputStream inStream) throws CheckstylePluginException: 
        RetrowException_loadFromPersistenceHandle() 
        && args(inStream){
        try
        {
            proceed(inStream);
        }
        catch (Exception e)
        {
            CheckstylePluginException.rethrow(e, ErrorMessages.errorLoadingConfigFile);
        }
        finally
        {
            IOUtils.closeQuietly((BufferedInputStream) inputStream.get(Thread.currentThread().getName()));
        }
    }

    void around(InputStream inStream, InputStream defaultConfigStream)
        throws CheckstylePluginException: 
        RetrowException_migrateHandle()  && 
        args(inStream, defaultConfigStream){
        try
        {
            proceed(inStream, defaultConfigStream);
        }
        catch (Exception e)
        {
            CheckstylePluginException.rethrow(e, ErrorMessages.errorMigratingConfig);
        }
        finally
        {
            IOUtils.closeQuietly((BufferedInputStream) inputStream.get(Thread.currentThread().getName()));
            IOUtils.closeQuietly(defaultConfigStream);// defaultConfigStream nao
                                                      // muda durante a execucao
                                                      // do metodo
        }
    }

    void around(BufferedOutputStream out, ByteArrayOutputStream byteOut)
        throws CheckstylePluginException: RetrowException_storeToPersistenceHandle() &&
        args( out,  byteOut){
        try
        {
            proceed(out, byteOut);
        }
        catch (Exception e)
        {
            CheckstylePluginException.rethrow(e, ErrorMessages.errorWritingConfigFile);
        }
        finally
        {
            IOUtils.closeQuietly((ByteArrayOutputStream) outputStreamByteOut.get(Thread
                    .currentThread().getName()));
            IOUtils.closeQuietly((BufferedOutputStream) outputStream.get(Thread.currentThread()
                    .getName()));
        }
    }

    // Em getInputStreamMigrateHandler apenas inStream é alterado...

    ByteArrayInputStream around(): getInputStreamInternalGetModulesHandler(){
        ByteArrayInputStream in = proceed();
        byteArrayInputStream.put(Thread.currentThread().getName(), in);
        return in;
    }
    
    BufferedInputStream around(): getInputStreamInternalLoadFromPersistenceHandler() || getInputStreamMigrateHandler(){
        BufferedInputStream in = proceed();
        inputStream.put(Thread.currentThread().getName(), in);
        return in;
    }
    

    BufferedOutputStream around(): getOutputStreamStoreToPersistenceHandler(){
        BufferedOutputStream out = proceed();
        outputStream.put(Thread.currentThread().getName(), out);
        return out;
    }

    ByteArrayOutputStream around(): getByteArrayOutputStreamStoreToPersistence1Handler(){
        ByteArrayOutputStream byteOut = proceed();
        outputStreamByteOut.put(Thread.currentThread().getName(), byteOut);
        return byteOut;
    }

}
