package org.jhotdraw.standard;

import java.awt.Component;
import java.awt.Graphics;
import java.io.IOException;

import javax.swing.JOptionPane;

public class StandardHandler {

	public void abstractFigureClone(Exception e){
		System.err.println("Class not found: " + e);
	}
	public void abstractFigureClone_2(IOException e){
		System.err.println(e.toString());
	}
	/*public Object abstractLocatorClone(){
		throw new InternalError();
	}*/
	public void selectAreaTrackerDrawXORRect(Graphics g){
		g.dispose(); // SF bugtracker id: #490663
	}
	public void handleMouseEventException(Throwable t, Component object) {
		JOptionPane.showMessageDialog(
			object,
            t.getClass().getName() + " - " + t.getMessage(),
			"Error",
			JOptionPane.ERROR_MESSAGE);
		t.printStackTrace();
    }
	
}
