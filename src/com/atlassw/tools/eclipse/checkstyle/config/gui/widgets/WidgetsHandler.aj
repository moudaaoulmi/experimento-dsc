package com.atlassw.tools.eclipse.checkstyle.config.gui.widgets;

import java.util.MissingResourceException;
import java.util.regex.PatternSyntaxException;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Text;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

public aspect WidgetsHandler
{
    // ---------------------------
    // Declare soft's
    // ---------------------------
    declare soft: Exception: ConfigPropertyWidgetInteger_validateIntegerHandler() || 
    ConfigPropertyWidgetRegex_validateRegexHandler();

    // ---------------------------
    // Pointcut's
    // ---------------------------
    pointcut ConfigPropertyWidgetInteger_validateIntegerHandler() : 
        execution(* ConfigPropertyWidgetInteger.validate(..));

    pointcut ConfigPropertyWidgetRegex_validateRegexHandler() : 
        execution(* ConfigPropertyWidgetRegex.validate(..));

    pointcut ConfigPropertyWidgetMultiCheck_internalGetTextHandler() : 
        execution(* ConfigPropertyWidgetMultiCheck.TokenLabelProvider.internalGetText(..));

    pointcut ConfigPropertyWidgetRegex_testRegexHandler() : 
        execution(* ConfigPropertyWidgetRegex.testRegex(..));

    // ---------------------------
    // Advice's
    // ---------------------------
    void around() throws CheckstylePluginException: 
            ConfigPropertyWidgetInteger_validateIntegerHandler() || 
            ConfigPropertyWidgetRegex_validateRegexHandler() {
        try
        {
            proceed();
        }
        catch (Exception e)
        {
            CheckstylePluginException.rethrow(e, e.getLocalizedMessage());
        }
    }

    String around(Object element): ConfigPropertyWidgetMultiCheck_internalGetTextHandler() 
            && args(element){
        String translation;
        try
        {
            return proceed(element);
        }
        catch (MissingResourceException e)
        {
            translation = "" + element; //$NON-NLS-1$
        }
        return translation;
    }

    void around(): ConfigPropertyWidgetRegex_testRegexHandler(){
        try
        {
            proceed();
        }
        catch (PatternSyntaxException e)
        {
            Text mTextWidget = (Text) thisJoinPoint.getThis();
            Color mRedColor = (Color) thisJoinPoint.getThis();
            mTextWidget.setBackground(mRedColor);
        }
    }

}
