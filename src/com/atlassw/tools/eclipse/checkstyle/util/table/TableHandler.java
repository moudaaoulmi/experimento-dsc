package com.atlassw.tools.eclipse.checkstyle.util.table;

/**
 * 
 * @author julianasaraiva
 *
 */

public class TableHandler {
    
    //catch (NumberFormatException e)
    public void restoreStateHandler(int mSortedColumnIndex){
        mSortedColumnIndex = 0;
    }
    
  //catch (NumberFormatException e)
    public void restoreState2Handler(int mSortDirection, int DIRECTION_FORWARD){
        mSortDirection = DIRECTION_FORWARD;
    }
    
    //catch (NumberFormatException e)
    public void restoreState3Handler(boolean allColumnsHaveStoredData ){
        // probably a new column
        allColumnsHaveStoredData = false;
    }
        
}//TableHandler{}
