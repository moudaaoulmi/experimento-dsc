package com.atlassw.tools.eclipse.checkstyle.config;

import org.eclipse.osgi.util.NLS;
import org.xml.sax.SAXException;

import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

import org.eclipse.core.runtime.CoreException;
import java.io.IOException;

public privileged aspect CheckConfigurationWorkingCopyHandle
{
    declare soft: Exception: setLocationHandle();
    pointcut setLocationHandle(): execution (* CheckConfigurationWorkingCopy.setLocationHandle(..)) ;
    
    void around(String location, String oldLocation) throws CheckstylePluginException: setLocationHandle() 
            && args(location, oldLocation) {
        try{
           proceed(location, oldLocation);
        } catch (CheckstylePluginException e) {
            CheckConfigurationWorkingCopy c = (CheckConfigurationWorkingCopy) thisJoinPoint.getThis();
            c.mEditedLocation = oldLocation;
            CheckstylePluginException.rethrow(e, NLS
                    .bind(ErrorMessages.errorResolveConfigLocation, location, e
                            .getLocalizedMessage()));
        }
    }
    
    declare soft: CoreException: setModulesIterationHandle();
    pointcut setModulesIterationHandle(): execution (* CheckConfigurationWorkingCopy.setModulesIteration(..)) ;
    
    void around(): setModulesIterationHandle()  {
        try{
           proceed();
        } catch (CheckstylePluginException e) {
            // NOOP - just ignore
        }
    }
  
    
}
