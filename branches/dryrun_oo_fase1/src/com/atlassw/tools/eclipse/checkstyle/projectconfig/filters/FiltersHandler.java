
package com.atlassw.tools.eclipse.checkstyle.projectconfig.filters;

import com.atlassw.tools.eclipse.checkstyle.exception.ExceptionHandler;
import com.atlassw.tools.eclipse.checkstyle.exception.GeneralException;

@ExceptionHandler
public class FiltersHandler extends GeneralException
{

    public void commentedCode()
    {
    // this should never happen because we call
    // #isAccessible before invoking #members
    }
}
