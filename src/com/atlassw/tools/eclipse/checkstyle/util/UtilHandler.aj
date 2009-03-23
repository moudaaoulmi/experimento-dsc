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
    declare soft: Exception : SWTUtil_internalShellActivatedHandler();

    declare soft : TransformerConfigurationException : XMLUtil_writeWithSaxInternalHandler();

    // ---------------------------
    // Pointcut's
    // ---------------------------
    pointcut SWTUtil_intenalVerifyTextHandler(): 
        execution (* SWTUtil.OnlyDigitsVerifyListener.intenalVerifyText(..));

    pointcut SWTUtil_internalShellActivatedHandler(): 
        execution (* SWTUtil.ShellResizeSupportListener.internalShellActivated(..));

    pointcut SWTUtil_internalShellActivate2dHandler(): 
        execution (* SWTUtil.ShellResizeSupportListener.internalShellActivated2(..));

    pointcut XMLUtil_getDocumentBuilderHandler(): 
        execution(* XMLUtil.getDocumentBuilder(..)) ;

    pointcut XMLUtil_writeWithSaxInternalHandler() : 
        execution (* XMLUtil.writeWithSaxInternal(..));

    // ---------------------------
    // Advice's
    // ---------------------------
    boolean around(VerifyEvent e, boolean doit): SWTUtil_intenalVerifyTextHandler() && 
            args(e,doit) {
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

    Point around(Point initialSize): SWTUtil_internalShellActivatedHandler() && 
            args(initialSize){
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

    void around(Shell shell, IDialogSettings bounds): SWTUtil_internalShellActivate2dHandler() &&
            args(shell,bounds){
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
    DocumentBuilder around() throws ParserConfigurationException : 
            XMLUtil_getDocumentBuilderHandler(){
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

    void around(InputStream in, Templates templates, SAXTransformerFactory saxFactory) :
            XMLUtil_writeWithSaxInternalHandler() 
            && args(in, templates, saxFactory){
        try
        {
            proceed(in, templates, saxFactory);
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }
    }

}
