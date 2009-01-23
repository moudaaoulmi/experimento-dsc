package com.atlassw.tools.eclipse.checkstyle.builder;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.CoreException;
import org.xml.sax.SAXException;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

public privileged aspect RetrowException
{
    declare soft: IOException: auditor_runAuditHandle();
    declare soft: CoreException: auditor_runAuditHandle();
    declare soft: CheckstyleException: auditor_runAuditHandle();
    declare soft: CoreException: checkstyleBuilder_buildProjectsHandleHandle();
    declare soft: IOException: packageNamesLoader_getPackageNameInteration1Handle();
    declare soft: ParserConfigurationException: packageNamesLoader_getPackageNamesHandle();
    declare soft: SAXException: packageNamesLoader_getPackageNamesHandle();
    declare soft: IOException: packageNamesLoader_getPackageNamesHandle();
    
    
    pointcut auditor_runAuditHandle(): 
        execution (* Auditor.runAudit(..)) ;
    pointcut checkstyleBuilder_buildProjectsHandleHandle(): execution(* CheckstyleBuilder.buildProjectsHandle(..));
    pointcut packageNamesLoader_getPackageNameInteration1Handle(): 
        execution (* PackageNamesLoader.getPackageNameInteration1(..)) ;
    pointcut packageNamesLoader_getPackageNamesHandle(): 
        execution (* PackageNamesLoader.getPackageNames(..)) ;
    
    void around()throws CheckstylePluginException: auditor_runAuditHandle() {
        try{
            proceed();
        } catch (IOException e) {
            this.retrowException(e);
        }catch (CoreException e) {
            this.retrowException(e);
        }catch (CheckstyleException e) {
            this.retrowException(e);
        }
    }
    
   
    void around() throws CheckstylePluginException: checkstyleBuilder_buildProjectsHandleHandle() {
       try{
           proceed();
       } catch (CoreException e)
       {
           this.retrowException(e);
       }
    }
    
    void around()throws CheckstylePluginException: packageNamesLoader_getPackageNameInteration1Handle() {
        try{
            proceed();
        }catch (IOException e) {
            retrowException(e); //$NON-NLS-1$
        }
    }   
   
    
    List around()throws CheckstylePluginException: packageNamesLoader_getPackageNamesHandle() {
        List result = null;
        try{
            result = proceed();
        } catch (ParserConfigurationException e) {
            retrowException(e, "unable to parse " + PackageNamesLoader.DEFAULT_PACKAGES); //$NON-NLS-1$
        }catch (SAXException e) {
            retrowException(e, "unable to parse " + PackageNamesLoader.DEFAULT_PACKAGES + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
        }catch (IOException e) {
            retrowException(e, "unable to parse " + PackageNamesLoader.DEFAULT_PACKAGES); //$NON-NLS-1$
        }
        return result;
    }
    
    private void retrowException(Exception e) throws CheckstylePluginException {
        CheckstylePluginException.rethrow(e);
    }
    
    private void retrowException(Exception e, String message) throws CheckstylePluginException {
        CheckstylePluginException.rethrow(e, message);
    }
    
}
