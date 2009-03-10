package org.jhotdraw.ccconcerns.figures.persistence;

import org.jhotdraw.figures.AttributeFigure;
import org.jhotdraw.figures.FigureAttributes;
import org.jhotdraw.util.StorableOutput;
import org.jhotdraw.util.StorableInput;

import java.io.IOException;

/**
 * Adds the persistence concern to AttributeFigure.
 * 
 * The aspect is declared privileged as it needs access to the 
 * 'fAttributes' private member of the AttributeFigure class.
 * (The member does not exclusively belong to the persistence concern,
 * so it does not belong to this aspect.)
 * 
 * @author Marius M.
 */
public privileged aspect PersistentAttributeFigure {

    /**
     * Write an attribute figure to a storable output.
     */
	public void AttributeFigure.write(StorableOutput dw) {
		super.write(dw);
		if (fAttributes == null) {
			dw.writeString("no_attributes");
		}
		else {
			dw.writeString("attributes");
			fAttributes.write(dw);
		}
	}

    /**
     * Read an attribute figure from a storable input.
     */
	public void AttributeFigure.read(StorableInput dr) /*@AJHD refactored throws IOException*/ { 
		super.read(dr);
		String s = dr.readString();
		if (s.toLowerCase().equals("attributes")) {
			fAttributes = new FigureAttributes();
			fAttributes.read(dr);
		}
	}
//	public void write(StorableOutput dw) {
//		super.write(dw);
//		if (fAttributes == null) {
//			dw.writeString("no_attributes");
//		}
//		else {
//			dw.writeString("attributes");
//			fAttributes.write(dw);
//		}
//	}
//
//	/**
//	 * Reads the Figure from a StorableInput.
//	 */
//	public void read(StorableInput dr) throws IOException {
//		super.read(dr);
//		String s = dr.readString();
//		if (s.toLowerCase().equals("attributes")) {
//			fAttributes = new FigureAttributes();
//			fAttributes.read(dr);
//		}
//	}
	
	
	
}
