package org.eclipse.osgi.util;

import com.atlassw.tools.eclipse.checkstyle.exception.ExceptionHandler;

@ExceptionHandler
public class UtilHandler
{
    public void throwIllegarArgumentException() throws IllegalArgumentException{
        throw new IllegalArgumentException();
    }
}
