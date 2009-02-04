package com.atlassw.tools.eclipse.checkstyle.config.gui;


import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.Messages;
import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationWorkingCopy;
import com.atlassw.tools.eclipse.checkstyle.config.ConfigProperty;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import org.osgi.service.prefs.BackingStoreException;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Nathalia Temudo
 *
 */
public class ConfigGuiHandler
{

    //catch(CheckstylePluginException e)
    //catch(Exception ex)
    public void errorDialogHandler(Exception e, Shell shell){
        CheckstyleLog.errorDialog(shell, e, true);
    }
    
    public void errorDialogTwoHandler(Exception e, Shell shell){
        CheckstyleLog.errorDialog(shell, ErrorMessages.msgErrorFailedExportConfig, e, true);
    }
    
    //catch(BackingStoreException e1)
    //catch(CheckstylePluginException e1)
    public void logHandler(Exception e){
        CheckstyleLog.log(e);
    }
    
    public void logRunTimeHandler(IllegalArgumentException e){
        CheckstyleLog.log(e);
    }
    
    
    public void warningDialogHandler(CheckstylePluginException e, CheckConfigurationWorkingCopy config, Shell shell){
        CheckstyleLog.warningDialog(shell, NLS.bind(
                ErrorMessages.errorCannotResolveCheckLocation, config.getLocation(), config
                        .getName()), e);
    }
    
    public void setErrorMessageHandler(TitleAreaDialog titleAreaDialog, CheckstylePluginException e){
        titleAreaDialog.setErrorMessage(e.getLocalizedMessage());
    }

    public void setErrorMessageHandlerTwo(TitleAreaDialog titleAreaDialog, CheckstylePluginException e, ConfigProperty property){
        titleAreaDialog.setErrorMessage(NLS.bind(
                Messages.RuleConfigurationEditDialog_msgInvalidPropertyValue, property
                .getMetaData().getName()));
        return;
    }
   
    
    
  
    
}//ConfigGuiHandler
