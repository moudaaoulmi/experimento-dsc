package org.jhotdraw.ccconcerns.commands;

//import java.util.EventObject;

import org.jhotdraw.ccconcerns.commands.undo.*;

import org.jhotdraw.framework.DrawingEditor;
import org.jhotdraw.framework.FigureSelectionListener;
import org.jhotdraw.framework.DrawingView;
import org.jhotdraw.standard.AbstractCommand;
import org.jhotdraw.standard.AlignCommand;
import org.jhotdraw.standard.BringToFrontCommand;
import org.jhotdraw.standard.ChangeAttributeCommand;
import org.jhotdraw.standard.CutCommand;
import org.jhotdraw.standard.DeleteCommand;
import org.jhotdraw.standard.DuplicateCommand;
import org.jhotdraw.standard.PasteCommand;
import org.jhotdraw.figures.GroupCommand;
import org.jhotdraw.figures.InsertImageCommand;
import org.jhotdraw.standard.SelectAllCommand;
import org.jhotdraw.standard.SendToBackCommand;
import org.jhotdraw.figures.UngroupCommand;
import org.jhotdraw.util.Command;
//import org.jhotdraw.util.CommandListener;
import org.jhotdraw.util.Undoable;
import org.jhotdraw.util.UndoableAdapter;

/**
 * 
 * Refactoring of the (org.jhotdraw.util.)UndoableCommand redirector (RL instance).
 * 
 * Commands (that seem to be) always wrapped in an UndoableCommand (in the main application DrawApp):
 * - AlignCommand
 * - BringToFrontCommand
 * - ChangeAttributeCommand (note: in the DrawApplet application is not wrapped)
 * - CutCommand
 * - DeleteCommand (note: in the DrawApplet application is not wrapped)
 * - DuplicateCommand (note: in the DrawApplet application is not wrapped)
 * - PasteCommand
 * - GroupCommand (note: in the DrawApplet application is not wrapped)
 * - InsertImageCommand
 * - SelectAllCommand
 * - SendToBackCommand
 * - UngroupCommand
 * 
 * ( ... ~ commands that can be undone.)
 * 
 *  
 * @author Marius Marin
 * 
 */
public aspect UndoableCommand /*perthis(this(Command))*/ {

	pointcut undoableCommands() :
		(
		(target(AlignCommand) && !within(AlignCommand) && !within(AlignCommandUndo))  
		|| (target(BringToFrontCommand) && !within(BringToFrontCommand) && !within(BringToFrontCommandUndo))	
		|| (target(ChangeAttributeCommand) && !within(ChangeAttributeCommand) && !within(ChangeAttributeCommandUndo))
		|| (target(CutCommand) && !within(CutCommand) && !within(CutCommandUndo))
		|| (target(DeleteCommand) && !within(DeleteCommand) && !within(DeleteCommandUndo))
		|| (target(DuplicateCommand) && !within(DuplicateCommand) && !within(DuplicateCommandUndo))
		|| (target(PasteCommand) && !within(PasteCommand) && !within(PasteCommandUndo))
		|| (target(GroupCommand) && !within(GroupCommand) && !within(GroupCommandUndo))
		|| (target(InsertImageCommand) && !within(InsertImageCommand) /*&& !within(InsertImageCommandUndo)*/)
		|| (target(SelectAllCommand) && !within(SelectAllCommand) /*&& !within(SelectAllCommandUndo)*/)
		|| (target(SendToBackCommand) && !within(SendToBackCommand) /*&& !within(SendToBackCommandUndo)*/)
		|| (target(UngroupCommand) && !within(UngroupCommand) /*&& !within(UngroupCommandUndo)*/)
		) && !within(UndoableCommand);

//	--- FigureSelectionListener ---
	
	//introduce it in the command so it is associated with the executing command
	private boolean AbstractCommand.hasSelectionChanged;
	
//	public void UndoableCommand.figureSelectionChanged(DrawingView view) {
//	    hasSelectionChanged = true;
//	}
		
	pointcut callCommandFigureSelectionChanged(/*AbstractCommand command,*/ DrawingView drawingView) :
		call(void FigureSelectionListener+.figureSelectionChanged(DrawingView)) && 
		undoableCommands(/*command*/) &&
		args(drawingView);
	
	void around(/*AbstractCommand command,*/ DrawingView drawingView) : callCommandFigureSelectionChanged(/*command,*/ drawingView) {

		AbstractCommand command = (AbstractCommand)thisJoinPoint.getTarget();
		
		command.hasSelectionChanged = true;
		proceed(/*command,*/ drawingView);
	}

//	-----------------------------

	
	pointcut callCommandExecute(/*AbstractCommand command*/) :
		call(void Command+.execute()) && undoableCommands(/*command*/);

	/**
	 * As now all commands extend AbstractCommand(which is also a FigureSelectionListener), I use
	 * AbstractCommand instead of Command in the advice/pointcut definitions.
	 */
	void around(/*AbstractCommand command*/) : callCommandExecute(/*command*/) {
	    	
		AbstractCommand command = (AbstractCommand)thisJoinPoint.getTarget();
	    	
		command.hasSelectionChanged = false;
		// listen for selection change events during executing the wrapped command
		command.view().addFigureSelectionListener(/*this*/command);
		
		proceed(/*command*/);

		Undoable undoableCommand = /*getWrappedCommand()*/command.getUndoActivity();
		
		if ((undoableCommand != null) && (undoableCommand.isUndoable())) {
			command.getDrawingEditor().getUndoManager().pushUndo(undoableCommand);
			command.getDrawingEditor().getUndoManager().clearRedos();
		}
		
		// initiate manual update of undo/redo menu states if it has not
		// been done automatically during executing the wrapped command
		if (!command.hasSelectionChanged || (command.getDrawingEditor().getUndoManager().getUndoSize() == 1)) {
			command.getDrawingEditor().figureSelectionChanged(command.view());
		}
		
		// remove because not all commands are listeners that have to be notified
		// all the time (bug-id 595461)
		command.view().removeFigureSelectionListener(/*this*/command);
	}
	
//	This doesn't work although I would have liked it ...
//  pointcut callCommandIsExecutable(AbstractCommand command) :
//  (
//  (call(boolean AlignCommand.isExecutable()) && !within(AlignCommand)) ||
//  (call(boolean BringToFrontCommand.isExecutable()) && !within(BringToFrontCommand)) ||
//	...
//  )
//  &&
//  target(command);

	
	pointcut callCommandIsExecutable(/*AbstractCommand command*/) :
		call(boolean Command+.isExecutable()) && undoableCommands(/*command*/);
    
    boolean around(/*AbstractCommand command*/) : callCommandIsExecutable(/*command*/) {
        return proceed(/*command*/);
    }

    
	pointcut callCommandName(/*AbstractCommand command*/) :
		call(String Command+.name()) && undoableCommands(/*command*/);
    
    String around(/*AbstractCommand command*/) : callCommandName(/*command*/) {
        return proceed(/*command*/);
    }


	pointcut callCommandGetDrawingEditor(/*AbstractCommand command*/) :
		call(DrawingEditor Command+.getDrawingEditor()) && undoableCommands(/*command*/);
    
    DrawingEditor around(/*AbstractCommand command*/) : callCommandGetDrawingEditor(/*command*/) {
        return proceed(/*command*/);
    }
        

	pointcut callCommandGetUndoActivity(/*AbstractCommand command*/) :
		call(Undoable Command+.getUndoActivity()) && undoableCommands(/*command*/);

    Undoable around(/*AbstractCommand command*/) : callCommandGetUndoActivity(/*command*/) {
    	AbstractCommand command = (AbstractCommand)thisJoinPoint.getTarget();
    	return new UndoableAdapter(command.getDrawingEditor().view());
    }
    
    
	pointcut callCommandSetUndoActivity(/*AbstractCommand command,*/ Undoable undoable) :
		call(void Command+.setUndoActivity(Undoable)) && undoableCommands(/*command*/) && args(undoable);

    void around(/*AbstractCommand command,*/ Undoable undoable) : callCommandSetUndoActivity(/*command,*/ undoable) {
        // do nothing: always return default UndoableAdapter
    }
    
    
//  ObservableUndoableCommand ----------------------------------------------------------------------------

//	pointcut callCommandAddCommandListener(/*AbstractCommand command,*/ CommandListener newCommandListener) :
//		call(void Command+.addCommandListener(CommandListener)) 
//		&& undoableCommands(/*command*/)
//		&& args(newCommandListener);
//	
//    void around(/*AbstractCommand command,*/ CommandListener newCommandListener) : callCommandAddCommandListener(/*command,*/ newCommandListener) {
//        proceed(/*command,*/ newCommandListener);
//    }
//
//
//	pointcut callCommandRemoveCommandListener(/*AbstractCommand command,*/ CommandListener oldCommandListener) :
//		call(void Command+.removeCommandListener(CommandListener)) 
//		&& undoableCommands(/*command*/)
//		&& args(oldCommandListener);
//	
//    void around(/*AbstractCommand command,*/ CommandListener newCommandListener) : callCommandRemoveCommandListener(/*command,*/ newCommandListener) {
//        proceed(/*command,*/ newCommandListener);
//    }
//
//	
//	pointcut callCommandSetEventDispatcher(/*AbstractCommand command,*/ CommandObserver.EventDispatcher newEventDispatcher) :
//		call(void AbstractCommand+.setEventDispatcher(CommandObserver.EventDispatcher)) 
//		&& undoableCommands(/*command*/)
//		&& args(newEventDispatcher);
//	
//    void around(/*AbstractCommand command,*/ CommandObserver.EventDispatcher newEventDispatcher) : callCommandSetEventDispatcher(/*command,*/ newEventDispatcher) {
//        proceed(/*command,*/ newEventDispatcher);
//    }
//	
//	
//	pointcut callCommandGetEventDispatcher(/*AbstractCommand command*/) :
//		call(CommandObserver.EventDispatcher AbstractCommand+.getEventDispatcher()) 
//		&& undoableCommands(/*command*/);
//	
//	CommandObserver.EventDispatcher around(/*AbstractCommand command*/) : callCommandGetEventDispatcher(/*command*/) {
//        return proceed(/*command*/);
//    }
//
//	
//	pointcut callCommandCreateEventDispatcher(/*AbstractCommand command*/) :
//		call(CommandObserver.EventDispatcher AbstractCommand+.createEventDispatcher()) && undoableCommands(/*command*/);
//
//	CommandObserver.EventDispatcher around(/*AbstractCommand command*/) : callCommandCreateEventDispatcher(/*command*/) {
//        return proceed(/*command*/);
//    }

	
//    CommandListener --------------------------------------------------------------------------
    
}
