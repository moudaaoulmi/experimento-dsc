package com.atlassw.tools.eclipse.checkstyle.config.gui;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Shell;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

public class GuiHandler
{
    public List guiHandler_initialize(List mModules, Exception e, Shell shell, boolean b){
        mModules = new ArrayList();
        CheckstyleLog.errorDialog(shell, e, b);
        return mModules;
    }
    
    public void guiHandler_okPressed(CheckstylePluginException e, 
            TitleAreaDialog titleAreaDialog){
        CheckstyleLog.log(e);
        titleAreaDialog.setErrorMessage(e.getLocalizedMessage());
    }

}
