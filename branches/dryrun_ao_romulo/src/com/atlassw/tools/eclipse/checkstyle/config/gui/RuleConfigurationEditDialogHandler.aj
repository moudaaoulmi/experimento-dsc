
package com.atlassw.tools.eclipse.checkstyle.config.gui;

import org.osgi.service.prefs.BackingStoreException;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.atlassw.tools.eclipse.checkstyle.config.gui.widgets.IConfigPropertyWidget;
import com.atlassw.tools.eclipse.checkstyle.config.ConfigProperty;
import org.eclipse.osgi.util.NLS;
import com.atlassw.tools.eclipse.checkstyle.Messages;

public aspect RuleConfigurationEditDialogHandler
{
    // ---------------------------
    // Declare Soft's
    // ---------------------------
    declare soft: BackingStoreException: internalWidgetSelectedHandler();

    declare soft: CheckstylePluginException: secInternalOkPressedHandler();

    // ---------------------------
    // Pointcut's
    // ---------------------------

    pointcut internalWidgetSelectedHandler():
        execution(* RuleConfigurationEditDialog.internalWidgetSelected(..));

    pointcut internalOkPressedHandler():
        execution(* RuleConfigurationEditDialog.internalOkPressed(..));

    pointcut secInternalOkPressedHandler():
        execution(* RuleConfigurationEditDialog.secInternalOkPressed(..));

    // ---------------------------
    // Advice's
    // ---------------------------

    void around():internalWidgetSelectedHandler()
    {
        try
        {
            proceed();
        }
        catch (BackingStoreException e1)
        {
            CheckstyleLog.log(e1);
        }
    }

    void around():internalOkPressedHandler(){
        try
        {
            proceed();
        }
        catch (IllegalArgumentException e)
        {
            CheckstyleLog.log(e);
        }
    }

    void around(IConfigPropertyWidget widget, ConfigProperty property):secInternalOkPressedHandler()
        && args (widget, property){
        RuleConfigurationEditDialog rC = (RuleConfigurationEditDialog) thisJoinPoint.getThis();
        try
        {
            proceed(widget, property);
        }
        catch (CheckstylePluginException e)
        {
            String message = NLS.bind(Messages.RuleConfigurationEditDialog_msgInvalidPropertyValue,
                    property.getMetaData().getName());
            rC.setErrorMessage(message);
            return;
        }
    }
}
