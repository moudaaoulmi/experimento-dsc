/*
 * 25th, November, 2008
 */
package com.atlassw.tools.eclipse.checkstyle.util;

import java.io.IOException;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

/**
 * @author juliana
 *
 */
public aspect CustomLibrariesClassLoaderHandler {
    
    declare soft : IOException : buildHandler();
    
    pointcut buildHandler(): execution(* CustomLibrariesClassLoader.get(..)) ;
    
    ClassLoader around() throws CheckstylePluginException : buildHandler() {
        ClassLoader c = null;
        try{
            c = proceed();
        }catch (IOException e)
        {
            CheckstylePluginException.rethrow(e);
        }
        return c;
    }//around()

}//CustomLibrariesClassLoaderHandler{}
