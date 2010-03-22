/*
 * $Id: TexCompletionProposal.java,v 1.6 2006/05/03 18:23:13 oskarojala Exp $
 *
 * Copyright (c) 2006 by the TeXlipse team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package net.sourceforge.texlipse.editor;

import net.sourceforge.texlipse.TexlipsePlugin;
import net.sourceforge.texlipse.model.TexCommandEntry;
import net.sourceforge.texlipse.properties.TexlipseProperties;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.link.LinkedModeModel;
import org.eclipse.jface.text.link.LinkedModeUI;
import org.eclipse.jface.text.link.LinkedPosition;
import org.eclipse.jface.text.link.LinkedPositionGroup;
import org.eclipse.jface.text.link.LinkedModeUI.ExitFlags;
import org.eclipse.jface.text.link.LinkedModeUI.IExitPolicy;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.texteditor.link.EditorLinkedModeUI;

/**
 * This class is a wrapper class to create an ICompletionProposal from a
 * TexCommandEntry. It also creates "smart" braces.
 * 
 * @author Boris von Loesch
 * @author Oskar Ojala
 * 
 */
public class TexCompletionProposal implements ICompletionProposal {
	private TexCommandEntry fentry;
	private int fReplacementOffset;
	private int fReplacementLength;
	private ISourceViewer fviewer;

	/**
	 * Constructs a new completion proposal for a (La)TeX command
	 * 
	 * @param entry
	 *            The command entry
	 * @param replacementOffset
	 *            Offset of where it is to be replaced
	 * @param replacementLength
	 *            The length of the replacement
	 * @param viewer
	 *            The current viewer
	 */
	public TexCompletionProposal(TexCommandEntry entry, int replacementOffset,
			int replacementLength, ISourceViewer viewer) {
		fentry = entry;
		fReplacementLength = replacementLength;
		fReplacementOffset = replacementOffset;
		fviewer = viewer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.contentassist.ICompletionProposal#apply(org.eclipse
	 * .jface.text.IDocument)
	 */
	public void apply(IDocument document) {
		if (fentry.arguments > 0) {
			StringBuffer displayKey = new StringBuffer(fentry.key);
			for (int j = 0; j < fentry.arguments; j++)
				displayKey.append("{}");
			document.replace(fReplacementOffset, fReplacementLength, displayKey
					.toString());
			if (TexlipsePlugin.getDefault().getPreferenceStore().getBoolean(
					TexlipseProperties.SMART_PARENS)) {
				LinkedModeModel model = new LinkedModeModel();
				for (int j = 0; j < fentry.arguments; j++) {
					int newOffset = fReplacementOffset + fentry.key.length()
							+ j * 2 + 1;
					LinkedPositionGroup group = new LinkedPositionGroup();
					group.addPosition(new LinkedPosition(document, newOffset,
							0, LinkedPositionGroup.NO_STOP));
					model.addGroup(group);
				}
				model.forceInstall();
				LinkedModeUI ui = new EditorLinkedModeUI(model, fviewer);
				ui.setSimpleMode(false);
				ui.setExitPolicy(new ExitPolicy('}', fviewer));
				ui.setExitPosition(fviewer, fReplacementOffset
						+ displayKey.length(), 0, Integer.MAX_VALUE);
				ui.setCyclingMode(LinkedModeUI.CYCLE_NEVER);
				ui.enter();
			}
		} else {
			document
					.replace(fReplacementOffset, fReplacementLength, fentry.key);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.contentassist.ICompletionProposal#getSelection
	 * (org.eclipse.jface.text.IDocument)
	 */
	public Point getSelection(IDocument document) {
		if (fentry.arguments > 0)
			return new Point(fReplacementOffset + fentry.key.length() + 1, 0);
		return new Point(fReplacementOffset + fentry.key.length(), 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.jface.text.contentassist.ICompletionProposal#
	 * getAdditionalProposalInfo()
	 */
	public String getAdditionalProposalInfo() {
		return (fentry.info.length() > TexCompletionProcessor.assistLineLength ? TexCompletionProcessor
				.wrapString(fentry.info,
						TexCompletionProcessor.assistLineLength)
				: fentry.info);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.contentassist.ICompletionProposal#getDisplayString
	 * ()
	 */
	public String getDisplayString() {
		return fentry.key;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposal#getImage()
	 */
	public Image getImage() {
		return fentry.getImage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.jface.text.contentassist.ICompletionProposal#
	 * getContextInformation()
	 */
	public IContextInformation getContextInformation() {
		return null;
	}

	protected static class ExitPolicy implements IExitPolicy {

		final char fExitCharacter;
		ISourceViewer fviewer;

		public ExitPolicy(char exitCharacter, ISourceViewer viewer) {
			fExitCharacter = exitCharacter;
			fviewer = viewer;
		}

		/*
		 * @see
		 * org.eclipse.jdt.internal.ui.text.link.LinkedPositionUI.ExitPolicy
		 * #doExit(org.eclipse.jdt.internal.ui.text.link.LinkedPositionManager,
		 * org.eclipse.swt.events.VerifyEvent, int, int)
		 */
		public ExitFlags doExit(LinkedModeModel environment, VerifyEvent event,
				int offset, int length) {
			if (event.character == fExitCharacter) {
				internalDoExit(event, offset);
			} else if (event.character == '{') {
				internalDoExit2(event, offset);
			}
			return null;
		}

		private void internalDoExit2(VerifyEvent event, int offset) {
			if (fviewer.getDocument().getChar(offset - 1) == '{')
				event.doit = false;
		}

		private void internalDoExit(VerifyEvent event, int offset) {
			if (fviewer.getDocument().getChar(offset) == fExitCharacter) {
				event.doit = false;
			}
			ExitFlags aux = internalDoExit(offset);
			if (aux == null)
				return;
			fviewer.setSelectedRange(offset + 1, 0);
		}

		private ExitFlags internalDoExit(int offset) {
			if (fviewer.getDocument().getChar(offset + 1) == '{') {
				fviewer.setSelectedRange(offset + 2, 0);
			} else {
				fviewer.setSelectedRange(offset + 1, 0);
			}
			return null;
		}
	}

}