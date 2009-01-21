/*
 *  23th, November, 2008
 */

package com.atlassw.tools.eclipse.checkstyle.projectconfig.filters;

import java.lang.CloneNotSupportedException;
import java.lang.InternalError;

/**
 * @author juliana
 */
public aspect AbstractFilterHandler
{
    // ---------------------------
    // Declare soft's
    // ---------------------------
    declare soft : CloneNotSupportedException : cloneHandler();

    // ---------------------------
    // PointCut's
    // ---------------------------
    pointcut cloneHandler(): execution(* AbstractFilter.clone(..));

    // ---------------------------
    // Advices's
    // ---------------------------
    Object around() : cloneHandler() {
        try
        {
            return proceed();
        }
        catch (CloneNotSupportedException e)
        {
            throw new InternalError(); // should never happen
        }
    }

}
