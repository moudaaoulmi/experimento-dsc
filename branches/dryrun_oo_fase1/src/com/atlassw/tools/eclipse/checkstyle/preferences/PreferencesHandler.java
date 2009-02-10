package com.atlassw.tools.eclipse.checkstyle.preferences;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Shell;

import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

/**
 * 
 * @author julianasaraiva
 *
 */

public class PreferencesHandler {

    public void initializeDefaultPreferencesHandler(Exception e){
        CheckstyleLog.log(e);
    }
    
    //catch (CheckstylePluginException e)
    public void performOkHandler(Exception e, Shell shell){
        CheckstyleLog.errorDialog(shell, NLS.bind(
                ErrorMessages.errorFailedRebuild, e.getMessage()), e, true);
    }
    
    //catch (CheckstylePluginException e)
    //catch (BackingStoreException e)
    public void performOk2Handler(Exception e, Shell shell){
        CheckstyleLog.errorDialog(shell, NLS.bind(
                ErrorMessages.errorFailedSavePreferences, e.getLocalizedMessage()), e, true);
    }
    
    
}//PreferencesHandler{}
