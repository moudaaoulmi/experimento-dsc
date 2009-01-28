package com.atlassw.tools.eclipse.checkstyle.preferences;

import java.util.Collection;

import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.osgi.util.NLS;
import org.osgi.service.prefs.BackingStoreException;
import org.eclipse.swt.widgets.Shell;

public aspect PreferencesHandler
{
    // ---------------------------
    // Declare soft's
    // ---------------------------
    declare soft: CheckstylePluginException : CheckstylePreferencePage_performOkHandler() || CheckstylePreferencePage_internarPerformOkInternalHandler();
    declare soft: BackingStoreException : CheckstylePreferencePage_performOkHandler() || PrefsInitializer_internalinitializeDefaultPreferencesHandler();
    
    // ---------------------------
    // Pointcut's
    // ---------------------------
    pointcut CheckstylePreferencePage_performOkHandler() : execution (* CheckstylePreferencePage.internalPerformOk(..));
    pointcut CheckstylePreferencePage_internarPerformOkInternalHandler() : execution (* CheckstylePreferencePage.internarPerformOkInternal(..));
    pointcut PrefsInitializer_internalinitializeDefaultPreferencesHandler() : execution(* PrefsInitializer.internalinitializeDefaultPreferences(..));
    
    // ---------------------------
    // Advice's
    // ---------------------------
    void around(Shell shell) : CheckstylePreferencePage_performOkHandler() && args(shell){
        try{
            proceed(shell);
        }catch (CheckstylePluginException e){
            CheckstyleLog.errorDialog(shell, NLS.bind(
                    ErrorMessages.errorFailedSavePreferences, e.getLocalizedMessage()), e, true);
        }catch (BackingStoreException e){
            CheckstyleLog.errorDialog(shell, NLS.bind(
                    ErrorMessages.errorFailedSavePreferences, e.getLocalizedMessage()), e, true);
        }
    }
    
    void around(boolean needRebuildAllProjects,
            Collection projectsToBuild,Shell shell) : CheckstylePreferencePage_internarPerformOkInternalHandler() && 
            args(needRebuildAllProjects,projectsToBuild,shell){
        try{
            proceed(needRebuildAllProjects,projectsToBuild,shell);
        }
        catch (CheckstylePluginException e){
            CheckstyleLog.errorDialog(shell, NLS.bind(
                    ErrorMessages.errorFailedRebuild, e.getMessage()), e, true);
        }
    }
    
    void around(IEclipsePreferences prefs): PrefsInitializer_internalinitializeDefaultPreferencesHandler() && args(prefs){
        try {
            proceed(prefs);
        }catch (BackingStoreException e) {
            CheckstyleLog.log(e);
        }
    }
}
