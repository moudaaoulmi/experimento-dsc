package com.atlassw.tools.eclipse.checkstyle.config.gui.widgets;

import java.util.MissingResourceException;

/**
 * @author Romulo
 */
public privileged aspect ConfigPropertyWidgetMultiCheckHandler{

    pointcut internalGetTextHandler() : execution(* ConfigPropertyWidgetMultiCheck.TokenLabelProvider.internalGetText(..));

    String around(Object element): internalGetTextHandler() && args(element){
        String translation;
        try {
            return proceed(element);
        } catch (MissingResourceException e) {
            translation = "" + element; //$NON-NLS-1$
        }
        return translation;
    }
}
