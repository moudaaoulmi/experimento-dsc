/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC       initial implementation 
 *     AMC  03.27.2003  changed to allow access to NullIdeManager
 * 						as a singleton - needed for verifying
 * 						compiler warning and error messages.
 * ******************************************************************/

package org.tigris.aopmetrics.ajdt;

import java.io.IOException;
import java.util.List;

import org.aspectj.ajde.Ajde;
import org.aspectj.ajde.BuildProgressMonitor;
import org.aspectj.ajde.EditorAdapter;
import org.aspectj.ajde.ui.AbstractIcon;
import org.aspectj.ajde.ui.IStructureViewNode;
import org.aspectj.ajde.ui.IdeUIAdapter;
import org.aspectj.ajde.ui.StructureViewNodeFactory;
import org.aspectj.asm.IProgramElement;
import org.aspectj.asm.IRelationship;
import org.aspectj.bridge.ISourceLocation;

/**
 * @author Mik Kersten
 */
public class NullIdeManager {
	private static NullIdeManager ideManager = null;

	public static NullIdeManager getIdeManager() {
		if (null == ideManager) {
			ideManager = new NullIdeManager();
		}
		return ideManager;
	}

	public void init() {
		Ajde.init(new NullIdeEditorAdapter(), new NullIdeTaskListManager(),
				new NullIdeProgressMonitor(), new NullIdeProperties(),
				new NullIdeBuildOptions(),
				new NullIdeStructureViewNodeFactory(), new NullIdeUIAdapter(),
				new NullIdeErrorHandler());
	}

	/** ***** IDE STUBS ***** **/

	class NullIdeEditorAdapter implements EditorAdapter {
		public void showSourceLine(String filePath, int lineNumber,
				boolean highlight) {
		}

		public void showSourceLine(ISourceLocation sourceLocation,
				boolean highlight) {
		}

		public void showSourceLine(int lineNumber, boolean highlight) {
		}

		public String getCurrFile() {
			return null;
		}

		public void saveContents() throws IOException {
		}

		public void pasteToCaretPos(String text) {
		}

		public void showSourcelineAnnotation(String filePath, int lineNumber,
				List items) {
		}

	}

	class NullIdeUIAdapter implements IdeUIAdapter {
		public void displayStatusInformation(String message) {
			System.out.println(message);
		}

		public void resetGUI() {
		}
	}

	class NullIdeStructureViewNodeFactory extends StructureViewNodeFactory {
		public NullIdeStructureViewNodeFactory() {
			super(null);
		}

		protected IStructureViewNode createLink(IProgramElement arg0,
				AbstractIcon arg1) {
			return null;
		}

		protected IStructureViewNode createRelationship(IRelationship arg0,
				AbstractIcon arg1) {
			return null;
		}

		protected IStructureViewNode createDeclaration(IProgramElement arg0,
				AbstractIcon arg1, List arg2) {
			return null;
		}

		public IStructureViewNode createNode(IProgramElement arg0, List arg1) {
			return null;
		}

		public IStructureViewNode createNode(IProgramElement arg0) {
			return null;
		}
	};

	class NullIdeProgressMonitor implements BuildProgressMonitor {
		public void start(String configFile) {
		}

		public void setProgressText(String text) {
		}

		public void setProgressBarVal(int newVal) {
		}

		public void incrementProgressBarVal() {
		}

		public void setProgressBarMax(int maxVal) {
		}

		public int getProgressBarMax() {
			return 0;
		}

		public void finish() {
		}
	}
}
