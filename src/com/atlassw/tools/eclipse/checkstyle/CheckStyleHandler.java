package com.atlassw.tools.eclipse.checkstyle;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;


/**
 * @author juliana
 *
 */

public class CheckStyleHandler {
    
    //catch (Exception ioe)
    public void startHandler(Exception e)
    {
        CheckstyleLog.log(e);
    }
    

}//CheckStyleHandler{}
