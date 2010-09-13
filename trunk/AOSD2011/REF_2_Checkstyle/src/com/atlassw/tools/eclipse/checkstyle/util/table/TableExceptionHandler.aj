package com.atlassw.tools.eclipse.checkstyle.util.table;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Table;

public privileged aspect TableExceptionHandler
{
    // ---------------------------
    // Pointcut's
    // ---------------------------
    //Extraiu pq não consegui diferenciar os joinpoints e quando sem extrair
    //estava saindo afetando todos os getInt do metodo original.
    pointcut EnhancedTableViewer_restoreStateHandler(): 
        call(* IDialogSettings.getInt(String)) &&
        withincode(* EnhancedTableViewer.internalRestoreStateGetInt(..));

    //Extraiu pq não consegui diferenciar os joinpoints e quando sem extrair
    //estava saindo afetando todos os getInt do metodo original.
    pointcut EnhancedTableViewer_internalRestoreStateHandler(): 
        call(* IDialogSettings.getInt(String)) &&
        withincode(* EnhancedTableViewer.internalRestoreState(..));

    pointcut EnhancedTableViewer_internalRestoreState1Handler(): 
        execution(* EnhancedTableViewer.internalRestoreState1(..)) ;

    pointcut EnhancedTableViewer_restoreState1Handler():
        call(* Table.select(int)) &&
        withincode(* EnhancedTableViewer.restoreState(..));

    // ---------------------------
    // Advice's
    // ---------------------------
    int around() : EnhancedTableViewer_restoreStateHandler() {
        EnhancedTableViewer obj = (EnhancedTableViewer) thisJoinPoint.getThis();
        try
        {
            obj.mSortedColumnIndex = proceed();
        }
        catch (NumberFormatException e)
        {
            obj.mSortedColumnIndex = 0;
        }
        return obj.mSortedColumnIndex;
    }

    int around() : EnhancedTableViewer_internalRestoreStateHandler() {
        EnhancedTableViewer obj = (EnhancedTableViewer) thisJoinPoint.getThis();
        try
        {
            obj.mSortedColumnIndex = proceed();
        }
        catch (NumberFormatException e)
        {
            obj.mSortedColumnIndex = EnhancedTableViewer.DIRECTION_FORWARD;
        }
        return obj.mSortedColumnIndex;
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
