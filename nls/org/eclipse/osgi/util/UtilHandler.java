package org.eclipse.osgi.util;

import com.atlassw.tools.eclipse.checkstyle.exception.ExceptionHandler;
import com.atlassw.tools.eclipse.checkstyle.exception.GeneralException;

@ExceptionHandler
public class UtilHandler extends GeneralException
{
    public void throwIllegarArgumentException() throws IllegalArgumentException{
        throw new IllegalArgumentException();
    }
}
