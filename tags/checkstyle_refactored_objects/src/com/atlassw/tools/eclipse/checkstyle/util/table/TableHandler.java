package com.atlassw.tools.eclipse.checkstyle.util.table;

import com.atlassw.tools.eclipse.checkstyle.exception.ExceptionHandler;
import com.atlassw.tools.eclipse.checkstyle.exception.GeneralException;

@ExceptionHandler
public class TableHandler extends GeneralException {
    
    public int restoreState2Handler(int mSortDirection, int DIRECTION_FORWARD){
        return mSortDirection = DIRECTION_FORWARD;
    }
       
}
