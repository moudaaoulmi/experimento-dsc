package com.atlassw.tools.eclipse.checkstyle.util.table;

import com.atlassw.tools.eclipse.checkstyle.exception.GeneralException;


public class TableHandler extends GeneralException {
    
    public void restoreState2Handler(int mSortDirection, int DIRECTION_FORWARD){
        mSortDirection = DIRECTION_FORWARD;
    }
    
    public void restoreState3Handler(boolean allColumnsHaveStoredData ){
        // probably a new column
        allColumnsHaveStoredData = false;
    }
        
}
