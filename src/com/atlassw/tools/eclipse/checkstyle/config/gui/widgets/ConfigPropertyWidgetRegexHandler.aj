package com.atlassw.tools.eclipse.checkstyle.config.gui.widgets;
import java.util.regex.PatternSyntaxException;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Text;

public aspect ConfigPropertyWidgetRegexHandler{
    
    pointcut internalGetTextHandler() : execution(* ConfigPropertyWidgetRegex.internalTestRegex(..));
    
    void around(Text mTextWidget,Color mRedColor): internalGetTextHandler() && args(mTextWidget,mRedColor){
        try {
            proceed(mTextWidget,mRedColor);
        }catch (PatternSyntaxException e){
            mTextWidget.setBackground(mRedColor);
        }
    }
}
