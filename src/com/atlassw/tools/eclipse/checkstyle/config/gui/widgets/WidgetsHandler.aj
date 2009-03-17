/**
 * 
 */
package com.atlassw.tools.eclipse.checkstyle.config.gui.widgets;

import java.util.MissingResourceException;
import java.util.regex.PatternSyntaxException;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Text;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

/**
 * @author julianasaraiva
 *
 */
public aspect WidgetsHandler
{

    declare soft: Exception: validateIntegerHandler() || validateRegexHandler();
    
    pointcut validateIntegerHandler() : execution(* ConfigPropertyWidgetInteger.validate(..));
    pointcut validateRegexHandler() : execution(* ConfigPropertyWidgetRegex.validate(..));
    pointcut internalGetTextHandler() : execution(* ConfigPropertyWidgetMultiCheck.TokenLabelProvider.internalGetText(..));
    pointcut testRegexHandler() : execution(* ConfigPropertyWidgetRegex.testRegex(..));
    
    void around() throws CheckstylePluginException: 
        validateIntegerHandler() || validateRegexHandler() {    
        try{
            proceed();
        }catch (Exception e){
            CheckstylePluginException.rethrow(e, e.getLocalizedMessage());
        }        
    }
    
    
    String around(Object element): internalGetTextHandler() && args(element){
        String translation;
        try {
            return proceed(element);
        } catch (MissingResourceException e) {
            translation = "" + element; //$NON-NLS-1$
        }
        return translation;
    }
    
    
    void around(): testRegexHandler(){
        try {
            proceed();
        }catch (PatternSyntaxException e){
            Text mTextWidget = (Text) thisJoinPoint.getThis();
            Color mRedColor = (Color) thisJoinPoint.getThis();
            mTextWidget.setBackground(mRedColor);
        }
    }
    
}
