/**
 * 
 */
package com.atlassw.tools.eclipse.checkstyle.util;

import java.io.IOException;
import java.util.EmptyStackException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;


/**
 * @author julianasaraiva
 *
 */
public privileged aspect UtilHandler {

    declare soft: Exception : internalShellActivatedHandler();
    declare soft : IOException : buildHandler();

    pointcut intenalVerifyTextHandler(): execution (* SWTUtil.OnlyDigitsVerifyListener.intenalVerifyText(..));
    pointcut internalShellActivatedHandler(): execution (* SWTUtil.ShellResizeSupportListener.internalShellActivated(..));
    pointcut internalShellActivate2dHandler(): execution (* SWTUtil.ShellResizeSupportListener.internalShellActivated2(..));
    pointcut getDocumentBuilderHandler(): execution(* XMLUtil.getDocumentBuilder(..)) ;
    pointcut buildHandler(): execution(* CustomLibrariesClassLoader.get(..)) ;
    
    boolean around(VerifyEvent e, boolean doit): intenalVerifyTextHandler() && args(e,doit) {
        try{
            proceed(e,doit);
        }catch (NumberFormatException ex){
            doit = false;
        }
        return doit;
    }

    Point around(): internalShellActivatedHandler(){
        Point initialSize;
        try{
            initialSize = proceed();
        } catch (Exception e1) {
            initialSize = new Point(0, 0);
        }
        return initialSize;
    }

    void around(Shell shell, IDialogSettings bounds): internalShellActivate2dHandler() && args(shell,bounds){
        try {
            proceed(shell,bounds);
        } catch (NumberFormatException ex) {
            SWTUtil.ShellResizeSupportListener swt = ((SWTUtil.ShellResizeSupportListener) thisJoinPoint.getThis());
            swt.mNewBounds = shell.getBounds();
        }
    }

    /**
     * 
     * O advice não pôde afetar o método createDocumentBuilder()
     * porque o mesmo é private static synchronized
     * 
     */
    DocumentBuilder around() throws ParserConfigurationException : getDocumentBuilderHandler(){
        DocumentBuilder c = null;
        try{
            c = proceed();
        } catch (EmptyStackException e) {
            XMLUtil obj = (XMLUtil) thisJoinPoint.getThis();
            c =   obj.createDocumentBuilder();
        }
        return c;
    }

    ClassLoader around() throws CheckstylePluginException : buildHandler() {
        ClassLoader c = null;
        try{
            c = proceed();
        }catch (IOException e)
        {
            CheckstylePluginException.rethrow(e);
        }
        return c;
    }//around()
}//UtilHandler
