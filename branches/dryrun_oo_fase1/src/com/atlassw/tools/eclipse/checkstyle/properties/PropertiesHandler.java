/**
 * 
 */
package com.atlassw.tools.eclipse.checkstyle.properties;

import java.util.regex.PatternSyntaxException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.Messages;
import com.atlassw.tools.eclipse.checkstyle.config.ICheckConfiguration;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

/**
 * @author Nathalia Temudo
 *
 */
public class PropertiesHandler
{
    //CoreException
    //CheckstylePluginException
    public void errorDialogHandler(Shell shell,  String string, Exception e){
            CheckstyleLog.errorDialog(shell, string, e, true);
    }
    
    //CheckstylePluginException
    //InstantiationException
    public void errorDialogHandlerTwo(Shell shell, Exception e){
        CheckstyleLog.errorDialog(shell, e, true);
    }
    
    public void errorDialogHandlerThree(Composite mComposite, String string, CheckstylePluginException e){
        CheckstyleLog.errorDialog(mComposite.getShell(), NLS.bind(string, e.getMessage()), e, true);
    }
    
    public boolean warningDialogHandler(Shell shell, ICheckConfiguration checkConfig, CheckstylePluginException e){
        CheckstyleLog.warningDialog(shell, NLS.bind(
                ErrorMessages.errorCannotResolveCheckLocation, checkConfig
                        .getLocation(), checkConfig.getName()), e);
        return false;
    }
    
    public void warningDialogHandlerTwo(CheckstylePropertyPage mPropertyPage, IProject project, ICheckConfiguration config, 
    CheckstylePluginException ex){
        CheckstyleLog.warningDialog(mPropertyPage.getShell(), Messages.bind(
                Messages.CheckstylePreferencePage_msgProjectRelativeConfigNoFound,
                project, config.getLocation()), ex);
    }
    
    public void setErrorMessageCkeckedHandler(TitleAreaDialog titleAreaDialog, CheckstylePluginException e){
        titleAreaDialog.setErrorMessage(e.getLocalizedMessage());
        return;
    }
    
    public void setErrorMessageNoCkeckedHandler(TitleAreaDialog titleAreaDialog, PatternSyntaxException e){
        titleAreaDialog.setErrorMessage(e.getLocalizedMessage());
        return;
    }
    
    public void logHandler(CoreException e){
         CheckstyleLog.log(e);
    }

}//PropertiesHandler
