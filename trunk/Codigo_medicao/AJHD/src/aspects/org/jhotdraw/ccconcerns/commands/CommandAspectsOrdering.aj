package org.jhotdraw.ccconcerns.commands;

import org.jhotdraw.ccconcerns.commands.undo.CommandUndo;

/**
 * Takes care of the order of the aspects for cases like where more
 * advices are attached to the same joinpoint.
 * 
 * 
 * @author marin
 */
public aspect CommandAspectsOrdering {

	//precedence for the two after advices attached to the UndoableCommand constructor
	//!!! This order actually means that the *after* advice in ObservableUndoableCommand will 
	//excute second; that is, this advice is more of an "after" advice than the other one.
	declare precedence: UndoableCommand, ObservableUndoableCommand, CommandObserver;

	//- the precondition check (view not null) executes before init of undo and
	//affected figures;
	//- the notification of views is the "last after advice"  
	declare precedence: CommandContracts, *CommandUndo;

}
