/**
 * 
 */

package com.atlassw.tools.eclipse.checkstyle.util;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.Templates;

import java.util.EmptyStackException;
import java.io.InputStream;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;

import org.apache.commons.io.IOUtils;
import org.eclipse.jface.dialogs.IDialogSettings;

public privileged aspect UtilHandler
{
    // ---------------------------
    // Declare soft's
    // ---------------------------
    declare soft: Exception : internalShellActivatedHandler();
    
    declare soft : TransformerConfigurationException : writeWithSaxInternalHandler();

    // ---------------------------
    // Pointcut's
    // ---------------------------
    pointcut intenalVerifyTextHandler(): 
        execution (* SWTUtil.OnlyDigitsVerifyListener.intenalVerifyText(..));

    pointcut internalShellActivatedHandler(): 
        execution (* SWTUtil.ShellResizeSupportListener.internalShellActivated(..));

    pointcut internalShellActivate2dHandler(): 
        execution (* SWTUtil.ShellResizeSupportListener.internalShellActivated2(..));

    pointcut getDocumentBuilderHandler(): 
        execution(* XMLUtil.getDocumentBuilder(..)) ;
    
    pointcut writeWithSaxInternalHandler() : 
        execution (* XMLUtil.writeWithSaxInternal(..));

    // ---------------------------
    // Advice's
    // ---------------------------
    boolean around(VerifyEvent e, boolean doit): intenalVerifyTextHandler() && args(e,doit) {
        try
        {
            proceed(e, doit);
        }
        catch (NumberFormatException ex)
        {
            doit = false;
        }
        return doit;
    }

    Point around(Point initialSize): internalShellActivatedHandler() && args(initialSize){
        try
        {
            initialSize = proceed(initialSize);
        }
        catch (Exception e1)
        {
            initialSize = new Point(0, 0);
        }
        return initialSize;
    }

    void around(Shell shell, IDialogSettings bounds): internalShellActivate2dHandler() && args(shell,bounds){
        try
        {
            proceed(shell, bounds);
        }
        catch (NumberFormatException ex)
        {
            SWTUtil.ShellResizeSupportListener swt = ((SWTUtil.ShellResizeSupportListener) thisJoinPoint
                    .getThis());
            swt.mNewBounds = shell.getBounds();
        }
    }

    /**
     * O advice não pôde afetar o método createDocumentBuilder() porque o mesmo
     * é private static synchronized
     */
    DocumentBuilder around() throws ParserConfigurationException : getDocumentBuilderHandler(){
        DocumentBuilder c = null;
        try
        {
            c = proceed();
        }
        catch (EmptyStackException e)
        {
            XMLUtil obj = (XMLUtil) thisJoinPoint.getThis();
            c = obj.createDocumentBuilder();
        }
        return c;
    }
    
    void around(InputStream in, Templates templates, SAXTransformerFactory saxFactory) : writeWithSaxInternalHandler() 
            && args(in, templates, saxFactory){
        try{
            proceed(in, templates, saxFactory);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

}
