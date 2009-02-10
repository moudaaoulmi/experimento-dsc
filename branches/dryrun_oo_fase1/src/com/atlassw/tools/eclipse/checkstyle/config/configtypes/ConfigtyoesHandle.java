package com.atlassw.tools.eclipse.checkstyle.config.configtypes;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.Messages;
import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationWorkingCopy;
import com.atlassw.tools.eclipse.checkstyle.config.GlobalCheckConfigurationWorkingSet;
import com.atlassw.tools.eclipse.checkstyle.config.ICheckConfiguration;
import com.atlassw.tools.eclipse.checkstyle.config.ICheckConfigurationWorkingSet;
import com.atlassw.tools.eclipse.checkstyle.config.gui.CheckConfigurationPropertiesDialog;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.LocalCheckConfigurationWorkingSet;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

public class ConfigtyoesHandle
{

    public void retrows(Exception e) throws CheckstylePluginException {
        CheckstylePluginException.rethrow(e);
    }
    
    public void commentedCode() {
        // we won't load the bundle then
        // disabled logging bug #1647602
        // CheckstyleLog.log(ioe);
    }
    
    public void checkstyleLogHandle(Exception e) {
        CheckstyleLog.log(e);
    }
    
    public void configtyoesHandle(CheckstylePluginException exception, 
            CheckConfigurationWorkingCopy workingCopy, Text mLocation, 
            boolean fileExists) 
        throws CheckstylePluginException {
        String location = mLocation.getText();

        if (StringUtils.trimToNull(location) != null && fileExists)
        {
            workingCopy.setLocation(mLocation.getText());
        }
        else
        {
            throw exception;
        }
    }
    
    public void getEditedWorkingCopyHandle(CheckstylePluginException exception, 
            CheckConfigurationWorkingCopy workingCopy, String location, 
            boolean fileExists) 
    throws CheckstylePluginException {
        if (StringUtils.trimToNull(location) != null && fileExists) {
            workingCopy.setLocation(location); //Diferença no atributo passado
        }
        else {
            throw exception;
        }
}

    public void checkstyleLogHandleAndThrowsIOException(CheckstylePluginException e) 
        throws IOException {
        CheckstyleLog.log(e);
        throw new IOException(e.getMessage());
    }
    
    public boolean checkstyleLogHandleAndReturnFalse(CheckstylePluginException e) {
        checkstyleLogHandle(e);
        return false;
    }
    
    public void setErroMessageHandle(CheckConfigurationPropertiesDialog dialog, 
            CheckstylePluginException exception){
        dialog.setErrorMessage(exception.getLocalizedMessage());
    }
    
   public void getEditedWorkingCopyHandle(CheckstylePluginException exception, 
           Text mLocation, CheckConfigurationPropertiesDialog mCheckConfigDialog, 
           CheckConfigurationWorkingCopy mWorkingCopy, boolean fileExists) 
   throws CheckstylePluginException{
       String location = mLocation.getText();

       if (StringUtils.trimToNull(location) == null)
       {
           throw exception;
       }

       ICheckConfigurationWorkingSet ws = mCheckConfigDialog.getCheckConfigurationWorkingSet();
       IPath tmp = new Path(location);
       boolean isFirstPartProject = ResourcesPlugin.getWorkspace().getRoot().getProject(
               tmp.segment(0)).exists();

       if (ws instanceof LocalCheckConfigurationWorkingSet && !isFirstPartProject)
       {
           location = ((LocalCheckConfigurationWorkingSet) ws).getProject().getFullPath()
                   .append(location).toString();
           mLocation.setText(location);
       }
       else if (ws instanceof GlobalCheckConfigurationWorkingSet && !isFirstPartProject)
       {
           throw new CheckstylePluginException(NLS.bind(Messages.ProjectConfigurationEditor_msgNoProjectInWorkspace,
                   tmp.segment(0)));
       }

       if (fileExists)
       {
           mWorkingCopy.setLocation(mLocation.getText());
       }
       else
       {
           throw exception;
       }
   }
   
   public void erroDialog(Shell shell, CheckstylePluginException e, boolean b) {
       CheckstyleLog.errorDialog(shell, e, b);
   }

   public void commentedCode2()
   {
       // ignore
   }

   public void retrows(Exception e, String message) throws CheckstylePluginException
   {
       CheckstylePluginException.rethrow(e, message);
   }
    
   public void commentedCode3()
   {
       // we won't load the bundle then
       // disabled logging bug #1647602
       // CheckstyleLog.log(ioe);
   }

    public void checkstyleLogHandle(IOException e, String bind)
    {
        CheckstyleLog.log(e, bind);
    }
    
    public void commentedCode4()
    {
        // ignore this since there simply might be no properties file
    }

}
