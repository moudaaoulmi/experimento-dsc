
package com.atlassw.tools.eclipse.checkstyle.duplicates;

import com.atlassw.tools.eclipse.checkstyle.exception.GeneralException;

/**
 * @author juliana
 */
public class DuplicatesHandler extends GeneralException
{

    public Object[] getChildrenHandler()
    {
        return new Object[0];
    }

    public boolean selectHandler()
    {
        return false;
    }
}
