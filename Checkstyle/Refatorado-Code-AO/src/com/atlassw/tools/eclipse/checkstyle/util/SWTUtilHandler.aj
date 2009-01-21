/*
 * 25th, November, 2008
 */
package com.atlassw.tools.eclipse.checkstyle.util;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;


/**
 * @author juliana
 *
 */
public privileged aspect SWTUtilHandler {
    declare soft: Exception : internalShellActivatedHandler();
    
    pointcut intenalVerifyTextHandler(): execution (* SWTUtil.OnlyDigitsVerifyListener.intenalVerifyText(..));
    pointcut internalShellActivatedHandler(): execution (* SWTUtil.ShellResizeSupportListener.internalShellActivated(..));
    pointcut internalShellActivate2dHandler(): execution (* SWTUtil.ShellResizeSupportListener.internalShellActivated2(..));
    
    
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
        }
        catch (Exception e1)
        {
            initialSize = new Point(0, 0);
        }
        return initialSize;
    }
    
    void around(Shell shell, IDialogSettings bounds): internalShellActivate2dHandler() && args(shell,bounds){
        SWTUtil.ShellResizeSupportListener swt = ((SWTUtil.ShellResizeSupportListener) thisJoinPoint.getThis());
        try{
            proceed(shell,bounds);
        }
        catch (NumberFormatException ex)
        {
            swt.mNewBounds = shell.getBounds();
        }
    }


}
