
package com.atlassw.tools.eclipse.checkstyle.preferences;

import java.util.Collection;

import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.duplicates.DuplicatedCodeAction;
import com.atlassw.tools.eclipse.checkstyle.exception.ExceptionHandler;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.osgi.util.NLS;
import org.osgi.service.prefs.BackingStoreException;
import org.eclipse.swt.widgets.Shell;

@ExceptionHandler
public aspect PreferencesHandler
{
    // ---------------------------
    // Declare soft's
    // ---------------------------
    declare soft: CheckstylePluginException : CheckstylePreferencePage_performOkHandler() || CheckstylePreferencePage_internarPerformOkInternalHandler();

    declare soft: BackingStoreException : CheckstylePreferencePage_performOkHandler();

    // ---------------------------
    // Pointcut's
    // ---------------------------
    pointcut CheckstylePreferencePage_performOkHandler() : 
        execution (* CheckstylePreferencePage.performOk(..));

    pointcut CheckstylePreferencePage_internarPerformOkInternalHandler() :
        execution (* CheckstylePreferencePage.internarPerformOkInternal(..));

    // ---------------------------
    // Advice's
    // ---------------------------
    boolean around() : CheckstylePreferencePage_performOkHandler(){
        boolean result = true;
        try
        {
            result = proceed();
        }
        catch (CheckstylePluginException e)
        {
            CheckstylePreferencePage checkstylePreferencePage = ((CheckstylePreferencePage) thisJoinPoint
                    .getThis());
            CheckstyleLog.errorDialog(checkstylePreferencePage.getShell(), NLS.bind(
                    ErrorMessages.errorFailedSavePreferences, e.getLocalizedMessage()), e, true);
        }
        catch (BackingStoreException e)
        {
            CheckstylePreferencePage checkstylePreferencePage = ((CheckstylePreferencePage) thisJoinPoint
                    .getThis());
            CheckstyleLog.errorDialog(checkstylePreferencePage.getShell(), NLS.bind(
                    ErrorMessages.errorFailedSavePreferences, e.getLocalizedMessage()), e, true);
        }
        return result;
    }

    void around(boolean needRebuildAllProjects, Collection projectsToBuild, Shell shell) : 
            CheckstylePreferencePage_internarPerformOkInternalHandler() && 
            args(needRebuildAllProjects,projectsToBuild,shell){
        try
        {
            proceed(needRebuildAllProjects, projectsToBuild, shell);
        }
        catch (CheckstylePluginException e)
        {
            CheckstyleLog.errorDialog(shell, NLS.bind(ErrorMessages.errorFailedRebuild, e
                    .getMessage()), e, true);
        }
    }

}
