/*
 * 23th, November, 2008 
 */
package com.atlassw.tools.eclipse.checkstyle.properties;


import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

/**
 * @author juliana
 *
 */
public aspect FileMatchPatternEditDialogHandler {
    
    declare soft : CheckstylePluginException : internalFileMatchPatternHandler();
    
    pointcut internalFileMatchPatternHandler(): execution(* FileMatchPatternEditDialog.internalFileMatchPattern(..));
    
    void around() : internalFileMatchPatternHandler() {
        FileMatchPatternEditDialog obj = (FileMatchPatternEditDialog) thisJoinPoint.getThis();
            try{
                proceed();
            }catch (CheckstylePluginException e)
            {
                obj.setErrorMessage(e.getLocalizedMessage());
            }
    }//around()
    

}//FileMatchPatternEditDialogHandler{}
