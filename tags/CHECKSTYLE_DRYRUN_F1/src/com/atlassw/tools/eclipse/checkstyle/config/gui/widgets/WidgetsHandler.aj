/**
 * 
 */
package com.atlassw.tools.eclipse.checkstyle.config.gui.widgets;

import java.util.MissingResourceException;
import java.util.regex.PatternSyntaxException;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Text;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

/**
 * @author julianasaraiva
 *
 */
public aspect WidgetsHandler
{

    pointcut validateIntegerHandler() : execution(* ConfigPropertyWidgetInteger.validate(..));
    pointcut validateRegexHandler() : execution(* ConfigPropertyWidgetRegex.validate(..));
    pointcut internalGetTextHandler() : execution(* ConfigPropertyWidgetMultiCheck.TokenLabelProvider.internalGetText(..));
    pointcut internalGetText_2Handler() : execution(* ConfigPropertyWidgetRegex.internalTestRegex(..));
    
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
    
    
    void around(Text mTextWidget,Color mRedColor): internalGetText_2Handler() && args(mTextWidget,mRedColor){
        try {
            proceed(mTextWidget,mRedColor);
        }catch (PatternSyntaxException e){
            mTextWidget.setBackground(mRedColor);
        }
    }
    
}//WidgetsHandler
