package com.atlassw.tools.eclipse.checkstyle.config.gui;

import org.eclipse.swt.widgets.Shell;

import com.atlassw.tools.eclipse.checkstyle.exception.ExceptionHandler;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;


@ExceptionHandler
public class GuiHandler
{

    public static void moduleHandler(CheckstylePluginException e, Shell shell){
        CheckstyleLog.errorDialog(shell, e, true);
    }
   
//    //catch (CheckstylePluginException ex)
//    public void widgetSelectedHandler(CheckstylePluginException ex, CheckConfigurationPropertiesDialog class_){
//        
//        class_.setErrorMessage(ex.getLocalizedMessage());
//        
//    }
}
