package com.atlassw.tools.eclipse.checkstyle.voting;

import java.io.IOException;

import org.eclipse.swt.widgets.Shell;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

public class VotingHandler
{
    public void errorDialogHandler(IOException e, Shell shell)
    {
        CheckstyleLog.errorDialog(shell, e, false);
    }
}
