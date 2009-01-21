
package com.atlassw.tools.eclipse.checkstyle.config.configtypes;

import org.eclipse.swt.widgets.Shell;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import java.net.MalformedURLException;

public aspect RemoteConfigurationEditorHandler
{
    declare soft: CheckstylePluginException: internalCreateEditorControlHandler();

    pointcut internalCreateEditorControlHandler():
        execution(* RemoteConfigurationEditor.internalCreateEditorControl(..));

    void around(Shell shell): internalCreateEditorControlHandler() && args(shell){
        try
        {
            proceed(shell);
        }
        catch (CheckstylePluginException e)
        {
            CheckstyleLog.errorDialog(shell, e, true);
        }
    }

    declare soft: MalformedURLException: internalGetEditedWorkingCopyHandler();

    pointcut internalGetEditedWorkingCopyHandler():
        execution(* RemoteConfigurationEditor.internalGetEditedWorkingCopy(..));

    void around() throws CheckstylePluginException: internalGetEditedWorkingCopyHandler(){
        try
        {
            proceed();
        }
        catch (MalformedURLException e)
        {
            CheckstylePluginException.rethrow(e);
        }
    }
}
