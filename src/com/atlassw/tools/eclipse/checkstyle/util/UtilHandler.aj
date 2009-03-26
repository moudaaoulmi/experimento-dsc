
package com.atlassw.tools.eclipse.checkstyle.util;

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

    // ---------------------------
    // Pointcut's
    // ---------------------------
    pointcut SWTUtil_intenalVerifyTextHandler(): 
        execution (* SWTUtil.OnlyDigitsVerifyListener.intenalVerifyText(..));

    // esse caso tinham duas escritas em variáveis, mas apenas uma das variáveis
    // eram usadas após o catch, com isso, pode ser feita a extração.
    pointcut SWTUtil_internalShellActivatedHandler(): 
        execution (* SWTUtil.ShellResizeSupportListener.internalShellActivated(..));

    pointcut SWTUtil_internalShellActivate2dHandler(): 
        execution (* SWTUtil.ShellResizeSupportListener.internalShellActivated2(..));

    pointcut XMLUtil_getDocumentBuilderHandler(): 
        execution(* XMLUtil.getDocumentBuilder(..)) ;

    // esse caso tinham duas escritas em variáveis, mas apenas uma das variáveis
    // eram usadas após o catch, com isso, pode ser feita a extração.
    pointcut XMLUtil_internalWriteWithSaxHandler() : 
        call(* XMLUtil.internalWriteWithSax(..)) &&
        withincode  (* XMLUtil.writeWithSax(..));

    // ---------------------------
    // Advice's
    // ---------------------------
    boolean around(VerifyEvent e, boolean doit): SWTUtil_intenalVerifyTextHandler() && 
            args(e,doit) {
        try
        {
            doit = proceed(e, doit);
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
     * O advice n‹o p™de afetar o mŽtodo createDocumentBuilder() porque o mesmo
     * Ž private static synchronized
     */
    DocumentBuilder around() throws ParserConfigurationException : 
            XMLUtil_getDocumentBuilderHandler(){
        DocumentBuilder dB = null;
        try
        {
            dB = proceed();
        }
        catch (EmptyStackException e)
        {
            XMLUtil xMLU = (XMLUtil) thisJoinPoint.getThis();
            dB = xMLU.createDocumentBuilder();
        }
        return dB;
    }

    Templates around(SAXTransformerFactory saxFactory, InputStream in) :
            XMLUtil_internalWriteWithSaxHandler() 
            && args(saxFactory, in){
        Templates result = null;
        try
        {
            result = proceed(saxFactory, in);
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }
        return result;
    }

}
