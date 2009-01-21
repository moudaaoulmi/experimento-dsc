package com.atlassw.tools.eclipse.checkstyle.builder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.BadLocationException;
import org.xml.sax.SAXException;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

public privileged aspect CheckstyleLogMessage
{
    
    declare soft: CoreException: addErrorHandle();
    pointcut addErrorHandle(): execution (* Auditor.CheckstyleAuditListener.addError(..)) ;
    
    void around(): addErrorHandle() {
        try{
            proceed();
        } catch (CoreException e) {
            checkstyleLogMessage(e);
        }
    }
    
    declare soft: BadLocationException: calculateMarkerOffsetHandle();
    pointcut calculateMarkerOffsetHandle(): 
        execution (* Auditor.CheckstyleAuditListener.calculateMarkerOffset(..)) ;
    
    void around(): calculateMarkerOffsetHandle() {
        try{
            proceed();
        } catch (BadLocationException e) {
         // seems to happen quite often so its no use to log since we
            // can't do anything about it
            // CheckstyleLog.log(e);
        }
    }
    
    declare soft: SAXException: getPackageNameInteration3Handle();
    declare soft: IOException: getPackageNameInteration3Handle();
    
    pointcut getPackageNameInteration3Handle(): 
        execution (* PackageNamesLoader.getPackageNameInteration3(..)) ;
    
    void around(PackageNamesLoader nameLoader, URL aPackageFile): getPackageNameInteration3Handle() 
            && args(nameLoader, aPackageFile) {
        try{
            proceed(nameLoader, aPackageFile);
        }catch (SAXException e) {
            checkstyleLogMessage(e, "unable to parse " + aPackageFile.toExternalForm() //$NON-NLS-1$
                    + " - " + e.getLocalizedMessage()); //$NON-NLS-1$
        }catch (IOException e) {
            checkstyleLogMessage(e, "unable to read " + aPackageFile.toExternalForm()); //$NON-NLS-1$
        }
    }
    
 declare soft: JavaModelException: addToClassPathHandle();
    
    pointcut addToClassPathHandle(): 
        execution (* ProjectClassLoader.addToClassPath(..)) ;
    
    void around(): addToClassPathHandle() {
        try{
            proceed();
        } catch (JavaModelException jme) {
            CheckstyleLog.log(jme);
        }
    }
    
    declare soft: MalformedURLException: handlePathHandle();
    
    pointcut handlePathHandle(): 
        execution (* ProjectClassLoader.handlePath(..)) ;
    
    void around(): handlePathHandle() {
        try{
            proceed();
        } catch (MalformedURLException mfe) { 
         // log the exception although this should not happen
            CheckstyleLog.log(mfe, mfe.getLocalizedMessage());
        }
    }
    
    
    private void checkstyleLogMessage(Exception e, String message) {
        CheckstyleLog.log(e,message);
    }
    
    private void checkstyleLogMessage(Exception e) {
        CheckstyleLog.log(e);
    }
    
}
