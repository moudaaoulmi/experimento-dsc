package org.jhotdraw.ccconcerns.figures.persistence;

import org.jhotdraw.standard.CompositeFigure;
import org.jhotdraw.framework.Figure;
import org.jhotdraw.framework.FigureEnumeration;
import org.jhotdraw.util.*;

import java.io.IOException;

/**
 * Implements the "persistence" concern for composite figures.
 * The aspect accesses fields of the class into which introduces
 * the persistence concern (e.g., fFigures - protected list of the 
 * component figures of a composite), therefore needs to be given 
 * privileged access.
 * 
 * @author Marius M.
 *
 */
public privileged aspect PersistentCompositeFigure {
	
	
//	public void write(StorableOutput dw) {
//		super.write(dw);
//		dw.writeInt(figureCount());
//		FigureEnumeration fe = figures();
//		while (fe.hasNextFigure()) {
//			dw.writeStorable(fe.nextFigure());
//		}
//	}
	
	
	public void CompositeFigure.write(StorableOutput dw) {
		super.write(dw);
		int cnt = figureCount();
		dw.writeInt(cnt);
		FigureEnumeration fe = figures();
		int i = 0;
		while (fe.hasNextFigure()) {
			i++;
			Figure f = fe.nextFigure();
			if( f == null){
				throw new RuntimeException();
			}
			dw.writeStorable(f);
		}
		if(cnt != i){
			throw new RuntimeException();
		}
	}

//	public void read(StorableInput dr) throws IOException {
//		super.read(dr);
//		int size = dr.readInt();
//		fFigures = CollectionsFactory.current().createList(size);
//		for (int i=0; i<size; i++) {
//			add((Figure)dr.readStorable());
//		}
//		init(displayBox());
//	}
	
	public void CompositeFigure.read(StorableInput dr) /*@AJHD refactored throws IOException*/ { 
		super.read(dr);
		int size = dr.readInt();
		fFigures = CollectionsFactory.current().createList(size);
	
		for (int i=0; i<size; i++) {
			Storable s = dr.readStorable();
			if( s == null){
				throw new RuntimeException("reading null storable");
			} 
			add((Figure)s);
		}
		init(displayBox());
	}


}
