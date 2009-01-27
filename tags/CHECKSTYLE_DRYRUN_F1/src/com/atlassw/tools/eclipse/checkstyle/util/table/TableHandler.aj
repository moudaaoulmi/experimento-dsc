/*
 * 26th, November, 2008 
 */
package com.atlassw.tools.eclipse.checkstyle.util.table;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.widgets.TableColumn;

/**
 * @author juliana
 * 
 * Foi colocado Privileged no aspecto para que ele conseguisse acessar
 * um atributo privado sem ter que modificar a classe criando algum metodo
 * GET ou SET
 *
 */
public privileged aspect TableHandler {
   
    pointcut internalRestoreStateHandler(): execution(* EnhancedTableViewer.internalRestoreState(..));
    pointcut internalRestoreState2Handler(): execution(* EnhancedTableViewer.internalRestoreState2(..)) ;
    pointcut internalRestoreState3Handler(): execution(* EnhancedTableViewer.internalRestoreState3(..)) ;
    pointcut internalRestoreState4Handler(): execution(* EnhancedTableViewer.internalRestoreState4(..)) ;
    
    void around() : internalRestoreStateHandler() {
        try{
             proceed();
        }catch (NumberFormatException e)
        {
            EnhancedTableViewer obj = (EnhancedTableViewer) thisJoinPoint.getThis();
            obj.mSortedColumnIndex = 0;
        }
    }
    
    void around() : internalRestoreState2Handler() {
        try{
             proceed();
        }catch (NumberFormatException e)
        {
            EnhancedTableViewer obj = (EnhancedTableViewer) thisJoinPoint.getThis();
            obj.mSortedColumnIndex = EnhancedTableViewer.DIRECTION_FORWARD;
        }
    }
    
    boolean around(IDialogSettings settings, TableLayout layout,
            boolean allColumnsHaveStoredData, TableColumn[] columns, int i) : internalRestoreState3Handler() &&
            args (settings, layout, allColumnsHaveStoredData, columns, i){
        try{
            proceed(settings, layout, allColumnsHaveStoredData, columns, i);
        }
        catch (NumberFormatException e)
        {
            // probably a new column
            allColumnsHaveStoredData = false;
        }
        return allColumnsHaveStoredData;
    }
    
    void around() : internalRestoreState4Handler() {
        try{
             proceed();
        }catch (NumberFormatException e)
        {
         // NOOP
        }
    }
}
