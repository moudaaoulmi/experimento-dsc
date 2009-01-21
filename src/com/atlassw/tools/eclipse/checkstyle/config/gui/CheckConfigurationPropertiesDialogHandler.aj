
package com.atlassw.tools.eclipse.checkstyle.config.gui;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationWorkingCopy;

public aspect CheckConfigurationPropertiesDialogHandler
{
    // ---------------------------
    // Declare soft's
    // ---------------------------
    
    declare soft: CheckstylePluginException: internalSelectionChangedHandler() ||
                    widgetSelectedHandler()|| 
                    okPressedHandler() || 
                    setUniqueNameHandler();

    declare soft: Exception: createConfigurationEditorHandler();

    // ---------------------------
    // Pointcut's
    // ---------------------------

    pointcut internalSelectionChangedHandler(): 
        execution(* CheckConfigurationPropertiesDialog.internalSelectionChanged(..));

    pointcut widgetSelectedHandler():
        execution(* CheckConfigurationPropertiesDialog.getEditedWorkingCopyInternal(..));

    pointcut okPressedHandler(): execution(* CheckConfigurationPropertiesDialog.okPressed(..));

    pointcut createConfigurationEditorHandler():
        execution(* CheckConfigurationPropertiesDialog.createConfigurationEditor(..));

    pointcut setUniqueNameHandler():
        execution(* CheckConfigurationPropertiesDialog.internalSetUniqueName(..));
  
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
            // NOOP
        }
    }

    void around(): widgetSelectedHandler(){
        CheckConfigurationPropertiesDialog cD = (CheckConfigurationPropertiesDialog) thisJoinPoint
                .getThis();
        try
        {
            proceed();
        }
        catch (CheckstylePluginException ex)
        {
            cD.setErrorMessage(ex.getLocalizedMessage());
        }
    }

    void around():okPressedHandler(){
        CheckConfigurationPropertiesDialog cD = (CheckConfigurationPropertiesDialog) thisJoinPoint
                .getThis();
        try
        {
            proceed();
        }
        catch (CheckstylePluginException e)
        {
            CheckstyleLog.log(e);
            cD.setErrorMessage(e.getLocalizedMessage());
        }
    }

    void around():createConfigurationEditorHandler(){
        CheckConfigurationPropertiesDialog cD = (CheckConfigurationPropertiesDialog) thisJoinPoint
                .getThis();
        try
        {
            proceed();
        }
        catch (Exception ex)
        {
            CheckstyleLog.errorDialog(cD.getShell(), ex, true);
        }

    }

    void around(CheckConfigurationWorkingCopy config, String checkConfigName, String uniqueName,
            int counter):
            setUniqueNameHandler() && args(config, checkConfigName, uniqueName, counter){
        try
        {
            proceed(config, checkConfigName, uniqueName, counter);
        }
        catch (CheckstylePluginException e)
        {
            uniqueName = checkConfigName + " (" + counter + ")"; //$NON-NLS-1$ //$NON-NLS-2$
            counter++;
        }
    }

}
