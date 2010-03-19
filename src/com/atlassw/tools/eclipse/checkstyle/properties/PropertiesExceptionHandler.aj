/**
 * 
 */

package com.atlassw.tools.eclipse.checkstyle.properties;

import org.eclipse.core.resources.IProject;
import java.util.regex.PatternSyntaxException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Composite;
import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.Messages;
import com.atlassw.tools.eclipse.checkstyle.config.ICheckConfiguration;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import org.eclipse.swt.events.SelectionEvent;
/**
 * @author Cristiane Queiroz
 */
public privileged aspect PropertiesExceptionHandler
{
    // ---------------------------
    // Declare soft's
    // ---------------------------
    declare soft : CoreException : CheckstylePropertyPage_setElementHandler();

    declare soft : IllegalAccessException : CheckstylePropertyPage_openFilterEditorHandler();

    declare soft : InstantiationException : CheckstylePropertyPage_openFilterEditorHandler();

    declare soft : CheckstylePluginException :  ComplexFileSetsEditor_addFileSetHandler() || 
                                                ComplexFileSetsEditor_editFileSetHandler() || 
                                                FileSetEditDialog_widgetSelectedHandler() || 
                                                SimpleFileSetsEditor_widgetSelectedHandler() ||
                                                FileMatchPatternEditDialog_okPressedHandler() || 
                                                CheckstylePropertyPage_setElementHandler() || 
                                                CheckstylePropertyPage_internalCreateContentsHandler() ||
                                                CheckstylePropertyPage_performOkHandler() ||
                                                CheckstylePropertyPage_createFileSetsAreaHandler();

    // ---------------------------
    // Pointcut's
    // ---------------------------
    pointcut FileMatchPatternEditDialog_okPressedHandler(): 
        execution(* FileMatchPatternEditDialog.okPressed(..) );

    pointcut CheckstylePropertyPage_setElementHandler(): 
        execution(* CheckstylePropertyPage.setElement(..));

    pointcut CheckstylePropertyPage_internalCreateContentsHandler(): 
        execution(* CheckstylePropertyPage.internalCreateContentes(..));
    
    pointcut CheckstylePropertyPage_createFileSetsAreaHandler(): 
        execution (* CheckstylePropertyPage.PageController.widgetSelected(..));

    pointcut CheckstylePropertyPage_performOkHandler(): 
        execution(* CheckstylePropertyPage.performOk(..));

    pointcut CheckstylePropertyPage_openFilterEditorHandler(): 
        execution(* CheckstylePropertyPage.PageController.openFilterEditor(..));

    pointcut ComplexFileSetsEditor_addFileSetHandler(): 
        execution(* ComplexFileSetsEditor.addFileSet(..));

    pointcut ComplexFileSetsEditor_editFileSetHandler():
        execution(* ComplexFileSetsEditor.editFileSet(..));

    pointcut FileSetEditDialog_widgetSelectedHandler(): 
        execution(* FileSetEditDialog.Controller.internalWidgetSelected(..));

    pointcut SimpleFileSetsEditor_widgetSelectedHandler(): 
        execution (* SimpleFileSetsEditor.Controller.internalWidgetSelected(..));

    // ---------------------------
    // Advice's
    // ---------------------------
    void around() : FileMatchPatternEditDialog_okPressedHandler() {
        try
        {
            proceed();
        }
        catch (PatternSyntaxException e)
        {
            FileMatchPatternEditDialog fMPED = (FileMatchPatternEditDialog) thisJoinPoint.getThis();
            fMPED.setErrorMessage(e.getLocalizedMessage());
            return;
        }
        catch (CheckstylePluginException e)
        {
            FileMatchPatternEditDialog fMPED = (FileMatchPatternEditDialog) thisJoinPoint.getThis();
            fMPED.setErrorMessage(e.getLocalizedMessage());
            return;
        }
    }

    void around() : CheckstylePropertyPage_setElementHandler() {
        try
        {
            proceed();
        }
        catch (CoreException e)
        {
            CheckstylePropertyPage cPG = (CheckstylePropertyPage) thisJoinPoint.getThis();
            CheckstyleLog.errorDialog(cPG.getShell(), ErrorMessages.errorOpeningPropertiesPage, e,
                    true);
        }
        catch (CheckstylePluginException e)
        {
            CheckstylePropertyPage cPG = (CheckstylePropertyPage) thisJoinPoint.getThis();
            CheckstyleLog.errorDialog(cPG.getShell(), ErrorMessages.errorOpeningPropertiesPage, e,
                    true);
        }
    }

    void around(Composite container) : 
            CheckstylePropertyPage_internalCreateContentsHandler() &&
            args(container){
        try
        {
            proceed(container);
        }
        catch (CheckstylePluginException e)
        {
            CheckstylePropertyPage cPG = (CheckstylePropertyPage) thisJoinPoint.getThis();
            CheckstyleLog.errorDialog(cPG.getShell(), ErrorMessages.errorOpeningPropertiesPage, e,
                    true);
        }
    }

    void around(SelectionEvent selectionEvent) : 
            CheckstylePropertyPage_createFileSetsAreaHandler() &&
            args(selectionEvent){
        try
        {
            proceed(selectionEvent);
        }
        catch (CheckstylePluginException e)
        {
            CheckstylePropertyPage cPG = (CheckstylePropertyPage) thisJoinPoint.getThis();
            CheckstyleLog.errorDialog(cPG.getShell(), ErrorMessages.errorChangingFilesetEditor, e,
                    true);
        }
    }

    boolean around() : CheckstylePropertyPage_performOkHandler() {
        boolean result = true;
        try
        {
            result = proceed();
        }
        catch (CheckstylePluginException e)
        {
            CheckstylePropertyPage cPG = (CheckstylePropertyPage) thisJoinPoint.getThis();
            CheckstyleLog.errorDialog(cPG.getShell(), e, true);
        }
        return result;
    }

    void around() : CheckstylePropertyPage_openFilterEditorHandler() {
        try
        {
            proceed();
        }
        catch (IllegalAccessException ex)
        {
            CheckstylePropertyPage cPG = (CheckstylePropertyPage) thisJoinPoint.getThis();
            CheckstyleLog.errorDialog(cPG.getShell(), ex, true);
        }
        catch (InstantiationException ex)
        {
            CheckstylePropertyPage cPG = (CheckstylePropertyPage) thisJoinPoint.getThis();
            CheckstyleLog.errorDialog(cPG.getShell(), ex, true);
        }
    }

    void around() : ComplexFileSetsEditor_addFileSetHandler() ||
                    ComplexFileSetsEditor_editFileSetHandler() {
        try
        {
            proceed();
        }
        catch (CheckstylePluginException e)
        {
            ComplexFileSetsEditor obj = (ComplexFileSetsEditor) thisJoinPoint.getThis();
            CheckstyleLog.errorDialog(obj.mComposite.getShell(), NLS.bind(
                    ErrorMessages.errorFailedAddFileset, e.getMessage()), e, true);
        }
    }


    void around(ICheckConfiguration config, IProject project) : 
            FileSetEditDialog_widgetSelectedHandler() &&
            args(config, project){
        try
        {
            proceed(config, project);
        }
        catch (CheckstylePluginException ex)
        {
            FileSetEditDialog fSED = (FileSetEditDialog) thisJoinPoint.getThis();
            CheckstyleLog.warningDialog(fSED.mPropertyPage.getShell(), Messages.bind(
                    Messages.CheckstylePreferencePage_msgProjectRelativeConfigNoFound, project,
                    config.getLocation()), ex);
        }
    }

    void around(ICheckConfiguration config, IProject project) : 
            SimpleFileSetsEditor_widgetSelectedHandler() &&
            args(config, project){
        try
        {
            proceed(config, project);
        }
        catch (CheckstylePluginException ex)
        {
            SimpleFileSetsEditor cFSE = (SimpleFileSetsEditor) thisJoinPoint.getThis();
            CheckstyleLog.warningDialog(cFSE.getMPropertyPage().getShell(), Messages.bind(
                    Messages.CheckstylePreferencePage_msgProjectRelativeConfigNoFound, project,
                    config.getLocation()), ex);
        }
    }

}
