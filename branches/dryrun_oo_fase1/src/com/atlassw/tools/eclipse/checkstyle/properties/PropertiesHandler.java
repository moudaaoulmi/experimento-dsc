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
import com.atlassw.tools.eclipse.checkstyle.exception.GeneralException;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.sun.tools.javac.jvm.Gen;

/**
 * @author Nathalia Temudo
 */
public class PropertiesHandler extends GeneralException
{
    public Object warningDialogHandler(Shell shell, String msg, Exception e)
    {
        CheckstyleLog.warningDialog(shell, msg, e);
        return null;
    }

    public void setErrorMessageCkeckedHandler(TitleAreaDialog titleAreaDialog,
            Exception e)
    {
        titleAreaDialog.setErrorMessage(e.getLocalizedMessage());
        return;
    }

}
