/**
 * 
 */
package com.atlassw.tools.eclipse.checkstyle.properties;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Control;

import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.Messages;
import com.atlassw.tools.eclipse.checkstyle.config.ICheckConfiguration;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.FileSet;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

/**
 * @author julianasaraiva
 *
 */
public aspect PropertiesHandler
{
    
    declare soft : CoreException : setElementHandler() || internalRunHandler();
    declare soft : IllegalAccessException : openFilterEditorHandler();
    declare soft : InstantiationException : openFilterEditorHandler();
    declare soft : CheckstylePluginException :addFileSetHandler() || editFileSetHandler() || internalsetMatchPatternHandler() ||
                                            widgetSelectedHandler() || widgetSelectedHandler2() || internalFileMatchPatternHandler() || 
                                            setElementHandler() || createContentsHandler() ||
                                            isValidHandler() || performOkHandler() ||createFileSetsAreaHandler();

    
    pointcut internalFileMatchPatternHandler(): execution(* FileMatchPatternEditDialog.internalFileMatchPattern(..));
    pointcut setElementHandler(): execution(* CheckstylePropertyPage.setElement(..));
    pointcut createContentsHandler(): execution(* CheckstylePropertyPage.createContents(..));
    pointcut isValidHandler(): execution(* CheckstylePropertyPage.isValid(..));
    pointcut performOkHandler(): execution(* CheckstylePropertyPage.performOk(..));
    pointcut createFileSetsAreaHandler(): call(* CheckstylePropertyPage.createFileSetsArea(..));
    pointcut openFilterEditorHandler(): execution(* CheckstylePropertyPage.PageController.openFilterEditor(..));
    pointcut addFileSetHandler(): execution(* ComplexFileSetsEditor.addFileSet(..));
    pointcut editFileSetHandler(): execution(* ComplexFileSetsEditor.editFileSet(..));
    pointcut internalsetMatchPatternHandler(): execution(* FileMatchPatternEditDialog.internalsetMatchPattern(..));
    pointcut internalRunHandler(): call(* FileSetEditDialog.getFiles(..)) &&
    withincode(* FileSetEditDialog.internalRun(..));
    pointcut widgetSelectedHandler(): execution(* FileSetEditDialog.Controller.widgetSelected(..));
    pointcut widgetSelectedHandler2(): execution (* SimpleFileSetsEditor.Controller.widgetSelected(..));
    
    void around() : internalFileMatchPatternHandler() {
        FileMatchPatternEditDialog obj = (FileMatchPatternEditDialog) thisJoinPoint.getThis();
            try{
                proceed();
            }catch (CheckstylePluginException e)
            {
                obj.setErrorMessage(e.getLocalizedMessage());
            }
    }//around()
    
    void around() : setElementHandler() {
        CheckstylePropertyPage object = (CheckstylePropertyPage) thisJoinPoint.getThis();
        try{
            proceed();
        } catch (CoreException e)
        {
            CheckstyleLog
            .errorDialog(object.getShell(), ErrorMessages.errorOpeningPropertiesPage, e, true);
        }
        catch (CheckstylePluginException e)
        {
            CheckstyleLog
            .errorDialog(object.getShell(), ErrorMessages.errorOpeningPropertiesPage, e, true);
        }
    }//around()
    
    
    Control around() : createContentsHandler() {
        CheckstylePropertyPage object = (CheckstylePropertyPage) thisJoinPoint.getThis();
        Control c = null;
        try{
            c = proceed();
        } 
        catch (CheckstylePluginException e)
        {
            CheckstyleLog
            .errorDialog(object.getShell(), ErrorMessages.errorOpeningPropertiesPage, e, true);
        }
        return c;

    }//around()
    
    
    Control around() : createFileSetsAreaHandler()  {
        CheckstylePropertyPage object = (CheckstylePropertyPage) thisJoinPoint.getThis();
        Control c = null;
        try{
            c = proceed();
        } catch (CheckstylePluginException e)
        {
            CheckstyleLog.errorDialog(object.getShell(), ErrorMessages.errorChangingFilesetEditor,
                    e, true);
        }
        return c;
    }//around()
    
    boolean around() : performOkHandler() {
        CheckstylePropertyPage object = (CheckstylePropertyPage) thisJoinPoint.getThis();
        boolean c = false;
        try{
            c = proceed();
        } 
        catch (CheckstylePluginException e)
        {
            CheckstyleLog.errorDialog(object.getShell(), e, true);
        }
        return c;
    }//around()
    
    
    boolean around() : isValidHandler() {
        CheckstylePropertyPage object = (CheckstylePropertyPage) thisJoinPoint.getThis();

        // check if all check configurations resolve
        List fileSets = object.getMProjectConfig().getFileSets();
        Iterator it = fileSets.iterator();

        FileSet fileset = (FileSet) it.next();
        ICheckConfiguration checkConfig = fileset.getCheckConfig();

        boolean c = false;
        try{
            c = proceed();
        } 
        catch (CheckstylePluginException e)
        {

            CheckstyleLog.warningDialog(object.getShell(), NLS.bind(
                    ErrorMessages.errorCannotResolveCheckLocation, checkConfig
                    .getLocation(), checkConfig.getName()), e);
            return false;
        }
        return c;
    }//around()
    
    void around() : openFilterEditorHandler() {
        CheckstylePropertyPage object = (CheckstylePropertyPage) thisJoinPoint.getThis();
        try{
            proceed();
        } catch (IllegalAccessException ex)
        {
            CheckstyleLog.errorDialog(object.getShell(), ex, true);
        }
        catch (InstantiationException ex)
        {
            CheckstyleLog.errorDialog(object.getShell(), ex, true);
        }
    }//around()

 
    void around() : addFileSetHandler() || editFileSetHandler() {
        ComplexFileSetsEditor obj = (ComplexFileSetsEditor) thisJoinPoint.getThis();
        try{
            proceed();
        }catch (CheckstylePluginException e)
        {
            CheckstyleLog.errorDialog(obj.getMComposite().getShell(), NLS.bind(
                    ErrorMessages.errorFailedAddFileset, e.getMessage()), e, true);
        }
    }//around()
    

    void around() : internalsetMatchPatternHandler() {
        FileMatchPatternEditDialog obj = (FileMatchPatternEditDialog) thisJoinPoint.getThis();
        try{
            proceed();
        }catch (CheckstylePluginException e)
        {
            obj.setErrorMessage(e.getLocalizedMessage());
        }
    }//around()
    
 
    List around() : internalRunHandler() {
        List c = null;
        try{
            c = proceed();
        }  catch (CoreException e)
        {
            CheckstyleLog.log(e);
        }
        return c;
    }//around()
    
    void around() : widgetSelectedHandler() {
        FileSetEditDialog object = (FileSetEditDialog) thisJoinPoint.getThis();
        IProject project = (IProject) object.getMPropertyPage().getElement();
        ICheckConfiguration config = object.getMFileSet().getCheckConfig();

        try{
            proceed();
        } catch (CheckstylePluginException ex)
        {
            CheckstyleLog.warningDialog(object.getMPropertyPage().getShell(), Messages.bind(
                    Messages.CheckstylePreferencePage_msgProjectRelativeConfigNoFound,
                    project, config.getLocation()), ex);
        }
    }//around()
    
    
    void around() : widgetSelectedHandler2() {
        SimpleFileSetsEditor object = (SimpleFileSetsEditor) thisJoinPoint.getThis();

        ICheckConfiguration config = object.getMDefaultFileSet().getCheckConfig();
        IProject project = (IProject) object.getMPropertyPage().getElement();

        try{
            proceed();
        } catch (CheckstylePluginException ex)
        {
            CheckstyleLog.warningDialog(object.getMPropertyPage().getShell(), Messages.bind(
                    Messages.CheckstylePreferencePage_msgProjectRelativeConfigNoFound,
                    project, config.getLocation()), ex);
        }
    }//around()
    
}//PropertiesHandler
