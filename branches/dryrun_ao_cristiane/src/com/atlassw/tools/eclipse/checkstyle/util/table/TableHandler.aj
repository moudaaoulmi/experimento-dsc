package com.atlassw.tools.eclipse.checkstyle.util.table;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Table;

public privileged aspect TableHandler
{
    // ---------------------------
    // Pointcut's
    // ---------------------------
    pointcut EnhancedTableViewer_restoreStateHandler(): 
        call(* IDialogSettings.getInt(String)) &&
        withincode(* EnhancedTableViewer.restoreState(..));

  //Extraiu pq não consegui diferenciar os joinpoints
    pointcut EnhancedTableViewer_internalRestoreStateHandler(): 
        execution(* EnhancedTableViewer.internalRestoreState(..)) ;

    pointcut EnhancedTableViewer_internalRestoreState1Handler(): 
        execution(* EnhancedTableViewer.internalRestoreState1(..)) ;

    pointcut EnhancedTableViewer_restoreState1Handler():
        call(* Table.select(int)) &&
        withincode(* EnhancedTableViewer.restoreState(..));

    // ---------------------------
    // Advice's
    // ---------------------------
    int around() : EnhancedTableViewer_restoreStateHandler() {
        int result = 0;
        try
        {
            result = proceed();
        }
        catch (NumberFormatException e)
        {
            EnhancedTableViewer obj = (EnhancedTableViewer) thisJoinPoint.getThis();
            obj.mSortedColumnIndex = 0;
        }
        return result;
    }

    void around() : EnhancedTableViewer_internalRestoreStateHandler() {
        try
        {
            proceed();
        }
        catch (NumberFormatException e)
        {
            EnhancedTableViewer obj = (EnhancedTableViewer) thisJoinPoint.getThis();
            obj.mSortedColumnIndex = EnhancedTableViewer.DIRECTION_FORWARD;
        }
    }

    boolean around(IDialogSettings settings, TableLayout layout, boolean allColumnsHaveStoredData,
            TableColumn[] columns, int i) : 
                EnhancedTableViewer_internalRestoreState1Handler() &&
                args (settings, layout, allColumnsHaveStoredData, columns, i){
        try
        {
            proceed(settings, layout, allColumnsHaveStoredData, columns, i);
        }
        catch (NumberFormatException e)
        {
            // probably a new column
            allColumnsHaveStoredData = false;
        }
        return allColumnsHaveStoredData;
    }

    void around() : EnhancedTableViewer_restoreState1Handler() {
        try
        {
            proceed();
        }
        catch (NumberFormatException e)
        {
            // NOOP
        }
    }
}
