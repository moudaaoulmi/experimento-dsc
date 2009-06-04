
package com.atlassw.tools.eclipse.checkstyle.config.gui;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import org.osgi.service.prefs.BackingStoreException;
import java.util.ArrayList;

public privileged aspect CheckConfigurationConfigureDialogHandler
{
    // ---------------------------
    // Declare soft's
    // ---------------------------

    declare soft: BackingStoreException: internalCreateButtonBarHandler();

    declare soft: CheckstylePluginException: internalInitializeHandler() ||
                    ModuleHandler();

    // ---------------------------
    // Pointcut's
    // ---------------------------
    
    pointcut internalCreateButtonBarHandler():
        execution(* CheckConfigurationConfigureDialog.internalCreateButtonBar(..));

    pointcut internalInitializeHandler(): 
        execution(* CheckConfigurationConfigureDialog.internalInitialize(..));

    pointcut ModuleHandler():
        execution(* CheckConfigurationConfigureDialog.PageController.openModule(..)) ||
        execution(* CheckConfigurationConfigureDialog.PageController.internalNewModule(..)) ||
        execution(* CheckConfigurationConfigureDialog.internalOkPressed(..));

    // ---------------------------
    // Advice's
    // ---------------------------

    void around(): ModuleHandler(){
        CheckConfigurationConfigureDialog cD = (CheckConfigurationConfigureDialog) thisJoinPoint
                .getThis();
        try
        {
            proceed();
        }
        catch (CheckstylePluginException e)
        {
            CheckstyleLog.errorDialog(cD.getShell(), e, true);
        }
    }

    void around(): internalCreateButtonBarHandler(){
        try
        {
            proceed();
        }
        catch (BackingStoreException e1)
        {
            CheckstyleLog.log(e1);
        }
    }

    void around():internalInitializeHandler(){
        CheckConfigurationConfigureDialog cD = (CheckConfigurationConfigureDialog) thisJoinPoint
                .getThis();
        try
        {
            proceed();
        }
        catch (CheckstylePluginException e)
        {
            cD.mModules = new ArrayList();
            CheckstyleLog.errorDialog(cD.getShell(), e, true);
        }
    }

}
