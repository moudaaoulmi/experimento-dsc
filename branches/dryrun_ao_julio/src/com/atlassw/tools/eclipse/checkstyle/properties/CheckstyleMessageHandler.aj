/*
 * 25th, November, 2008 
 */
package com.atlassw.tools.eclipse.checkstyle.properties;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Control;
import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.Messages;

import java.util.List;
import java.util.Iterator;
import com.atlassw.tools.eclipse.checkstyle.config.ICheckConfiguration;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.FileSet;
import org.eclipse.osgi.util.NLS;

/**
 * @author juliana
 *
 */
public privileged aspect CheckstyleMessageHandler  {

    declare soft : CheckstylePluginException : setElementHandler() || createContentsHandler() ||
    isValidHandler() || performOkHandler() ||createFileSetsAreaHandler();
    declare soft : CoreException : setElementHandler();
    declare soft : IllegalAccessException : openFilterEditorHandler();
    declare soft : InstantiationException : openFilterEditorHandler();

    pointcut setElementHandler(): execution(* CheckstylePropertyPage.setElement(..));

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


    pointcut createContentsHandler(): execution(* CheckstylePropertyPage.createContents(..));

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


    pointcut isValidHandler(): execution(* CheckstylePropertyPage.isValid(..));

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


    pointcut performOkHandler(): execution(* CheckstylePropertyPage.performOk(..));

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

    pointcut createFileSetsAreaHandler(): call(* CheckstylePropertyPage.createFileSetsArea(..));

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


    pointcut openFilterEditorHandler(): execution(* CheckstylePropertyPage.PageController.openFilterEditor(..));

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


    declare soft : CheckstylePluginException :addFileSetHandler() || editFileSetHandler();

    pointcut addFileSetHandler(): execution(* ComplexFileSetsEditor.addFileSet(..));

    void around() : addFileSetHandler() {
        ComplexFileSetsEditor obj = (ComplexFileSetsEditor) thisJoinPoint.getThis();
        try{
            proceed();
        }catch (CheckstylePluginException e)
        {
            CheckstyleLog.errorDialog(obj.getMComposite().getShell(), NLS.bind(
                    ErrorMessages.errorFailedAddFileset, e.getMessage()), e, true);
        }
    }//around()

    pointcut editFileSetHandler(): execution(* ComplexFileSetsEditor.editFileSet(..));

    void around() : editFileSetHandler() {
        ComplexFileSetsEditor obj = (ComplexFileSetsEditor) thisJoinPoint.getThis();
        try{
            proceed();
        }catch (CheckstylePluginException e)
        {
            CheckstyleLog.errorDialog(obj.getMComposite().getShell(), NLS.bind(
                    ErrorMessages.errorFailedEditFileset, e.getMessage()), e, true);
        }
    }//around()

    declare soft : CheckstylePluginException : internalsetMatchPatternHandler();
    
    pointcut internalsetMatchPatternHandler(): execution(* FileMatchPatternEditDialog.internalsetMatchPattern(..));

    void around() : internalsetMatchPatternHandler() {
        FileMatchPatternEditDialog obj = (FileMatchPatternEditDialog) thisJoinPoint.getThis();
        try{
            proceed();
        }catch (CheckstylePluginException e)
        {
            obj.setErrorMessage(e.getLocalizedMessage());
        }
    }//around()


    declare soft : CoreException : internalRunHandler();
    declare soft : CheckstylePluginException : widgetSelectedHandler();

    pointcut internalRunHandler(): call(* FileSetEditDialog.getFiles(..)) &&
    withincode(* FileSetEditDialog.internalRun(..));

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


    pointcut widgetSelectedHandler(): execution(* FileSetEditDialog.Controller.widgetSelected(..));

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

    declare soft : CheckstylePluginException : widgetSelectedHandler2();

    pointcut widgetSelectedHandler2(): execution (* SimpleFileSetsEditor.Controller.widgetSelected(..));

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


}//CheckstylePropertyPageHandler{}
