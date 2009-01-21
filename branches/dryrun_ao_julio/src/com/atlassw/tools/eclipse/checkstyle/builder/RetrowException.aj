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
    declare soft: IOException: runAuditHandle();
    declare soft: CoreException: runAuditHandle();
    declare soft: CheckstyleException: runAuditHandle();
    
    pointcut runAuditHandle(): 
        execution (* Auditor.runAudit(..)) ;
    
    void around()throws CheckstylePluginException: runAuditHandle() {
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
    
    declare soft: CoreException: buildProjectsHandleHandle();
    
    pointcut buildProjectsHandleHandle(): execution(* CheckstyleBuilder.buildProjectsHandle(..));
    
    void around() throws CheckstylePluginException: buildProjectsHandleHandle() {
       try{
           proceed();
       } catch (CoreException e)
       {
           this.retrowException(e);
       }
    }
    
    declare soft: IOException: getPackageNameInteration1Handle();
    
    pointcut getPackageNameInteration1Handle(): 
        execution (* PackageNamesLoader.getPackageNameInteration1(..)) ;
    
    void around()throws CheckstylePluginException: getPackageNameInteration1Handle() {
        try{
            proceed();
        }catch (IOException e) {
            retrowException(e); //$NON-NLS-1$
        }
    }
    
    declare soft: ParserConfigurationException: getPackageNamesHandle();
    declare soft: SAXException: getPackageNamesHandle();
    declare soft: IOException: getPackageNamesHandle();
    
    pointcut getPackageNamesHandle(): 
        execution (* PackageNamesLoader.getPackageNames(..)) ;
    
    List around()throws CheckstylePluginException: getPackageNamesHandle() {
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
