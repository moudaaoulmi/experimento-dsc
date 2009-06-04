package com.atlassw.tools.eclipse.checkstyle.config.gui.widgets;

import java.util.regex.PatternSyntaxException;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Text;

import com.atlassw.tools.eclipse.checkstyle.exception.ExceptionHandler;
import com.atlassw.tools.eclipse.checkstyle.exception.GeneralException;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

@ExceptionHandler
public class WidgetsHandler extends GeneralException
{
    
    public String configPropertyWidgetsMultiCheck_getTextHandler(String translation, Object element){
        return translation = "" + element;
    }
    
    public void configPropertyWidgetsRegex_validateHandler(PatternSyntaxException e)
            throws CheckstylePluginException{
        CheckstylePluginException.rethrow(e, e.getLocalizedMessage());
    }

    public void configPropertyWidgetsRegex_testRegexHandler(Text mTextWidget, Color mRedColor){
        mTextWidget.setBackground(mRedColor);
    }
}
