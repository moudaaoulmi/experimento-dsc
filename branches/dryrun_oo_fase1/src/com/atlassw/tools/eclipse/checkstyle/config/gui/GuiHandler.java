package com.atlassw.tools.eclipse.checkstyle.config.gui;

import org.eclipse.swt.widgets.Shell;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

public class GuiHandler
{

    public static void moduleHandler(CheckstylePluginException e, Shell shell){
        CheckstyleLog.errorDialog(shell, e, true);
    }
}