
package com.atlassw.tools.eclipse.checkstyle.config.gui;

import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationFactory;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationWorkingCopy;
import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import org.eclipse.osgi.util.NLS;

public privileged aspect CheckConfigurationWorkingSetEditorHandler
{
    // ---------------------------
    // Declare Soft's
    // ---------------------------
    declare soft: CheckstylePluginException: internalSelectionChangedHandler() ||
                    internalCheckConfigHandler() ||
                    internalConfigureCheckConfigHandler() ||
                    exportCheckstyleCheckConfigHandler() ||
                    findPropertyItemsHandler();

    // ---------------------------
    // Pointcut's
    // ---------------------------

    pointcut internalSelectionChangedHandler():
        execution(* CheckConfigurationWorkingSetEditor.PageController.internalSelectionChanged(..));

    pointcut internalCheckConfigHandler():
        execution(* CheckConfigurationWorkingSetEditor.internalAddCheckConfig(..)) ||
        execution(* CheckConfigurationWorkingSetEditor.internalCopyCheckConfig(..));

    pointcut internalConfigureCheckConfigHandler():
        execution (* CheckConfigurationWorkingSetEditor.internalConfigureCheckConfig(..));

    pointcut exportCheckstyleCheckConfigHandler():
        call(* CheckConfigurationFactory.exportConfiguration(..)) &&
        withincode(* CheckConfigurationWorkingSetEditor.exportCheckstyleCheckConfig(..));

    pointcut findPropertyItemsHandler():
        execution(* ResolvablePropertiesDialog.Controller.findPropertyItems(..));

    // ---------------------------
    // Advice's
    // ---------------------------

    void around(): internalSelectionChangedHandler(){
        try
        {
            proceed();
        }
        catch (CheckstylePluginException e)
        {
            CheckstyleLog.log(e);
        }
    }

    void around(): internalCheckConfigHandler(){
        CheckConfigurationWorkingSetEditor cW = (CheckConfigurationWorkingSetEditor) thisJoinPoint
                .getThis();
        try
        {
            proceed();
        }
        catch (CheckstylePluginException ex)
        {
            CheckstyleLog.errorDialog(cW.getShell(), ex, true);
        }
    }

    void around(CheckConfigurationWorkingCopy config): internalConfigureCheckConfigHandler() && 
        args(config){
        CheckConfigurationWorkingSetEditor cC = (CheckConfigurationWorkingSetEditor) thisJoinPoint
                .getThis();
        try
        {
            proceed(config);
        }
        catch (CheckstylePluginException e)
        {
            CheckstyleLog.warningDialog(cC.getShell(), NLS.bind(
                    ErrorMessages.errorCannotResolveCheckLocation, config.getLocation(), config
                            .getName()), e);
        }
    }

    void around(): exportCheckstyleCheckConfigHandler(){
        CheckConfigurationWorkingSetEditor cW = (CheckConfigurationWorkingSetEditor) thisJoinPoint
                .getThis();
        try
        {
            proceed();
        }
        catch (CheckstylePluginException e)
        {
            CheckstyleLog.errorDialog(cW.getShell(), ErrorMessages.msgErrorFailedExportConfig, e,
                    true);
        }
    }

    void around(): findPropertyItemsHandler(){
        ResolvablePropertiesDialog c = (ResolvablePropertiesDialog) thisJoinPoint.getThis();
        try
        {
            proceed();
        }
        catch (CheckstylePluginException e)
        {
            CheckstyleLog.errorDialog(c.getShell(), e, true);
        }
    }
}
