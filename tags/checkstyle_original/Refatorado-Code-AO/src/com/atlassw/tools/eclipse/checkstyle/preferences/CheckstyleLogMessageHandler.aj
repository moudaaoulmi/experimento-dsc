package com.atlassw.tools.eclipse.checkstyle.preferences;

import java.util.Collection;

import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.osgi.util.NLS;
import org.osgi.service.prefs.BackingStoreException;
import org.eclipse.swt.widgets.Shell;

public aspect CheckstyleLogMessageHandler
{
    declare soft: CheckstylePluginException : performOkHandler() || internarPerformOkInternalHandler();
    declare soft: BackingStoreException : performOkHandler();
    
    pointcut performOkHandler() : execution (* CheckstylePreferencePage.internalPerformOk(..));
    pointcut internarPerformOkInternalHandler() : execution (* CheckstylePreferencePage.internarPerformOkInternal(..));
    
    void around(Shell shell) : performOkHandler() && args(shell){
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
            Collection projectsToBuild,Shell shell) : internarPerformOkInternalHandler() && 
            args(needRebuildAllProjects,projectsToBuild,shell){
        try{
            proceed(needRebuildAllProjects,projectsToBuild,shell);
        }
        catch (CheckstylePluginException e){
            CheckstyleLog.errorDialog(shell, NLS.bind(
                    ErrorMessages.errorFailedRebuild, e.getMessage()), e, true);
        }
    }
    
    declare soft: BackingStoreException : internalinitializeDefaultPreferencesHandler();
    
    pointcut internalinitializeDefaultPreferencesHandler() : execution(* PrefsInitializer.internalinitializeDefaultPreferences(..));
    
    void around(IEclipsePreferences prefs): internalinitializeDefaultPreferencesHandler() && args(prefs){
        try {
            proceed(prefs);
        }catch (BackingStoreException e) {
            CheckstyleLog.log(e);
        }
    }
}
