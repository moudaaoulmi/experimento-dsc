/*
 * $Id: TexCorrectIndentationAction.java,v 1.1 2005/03/16 12:17:43 kimmok Exp $
 *
 * Copyright (c) 2004-2005 by the TeXlapse Team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package net.sourceforge.texlipse.actions;

import java.util.Arrays;

import net.sourceforge.texlipse.TexlipsePlugin;
import net.sourceforge.texlipse.editor.TexEditor;
import net.sourceforge.texlipse.editor.TexEditorTools;
import net.sourceforge.texlipse.properties.TexlipseProperties;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * @author Laura Takkinen
 * 
 *         Handles indentation quick fix when user selects text area and
 *         performs "Correct Indentation" action. (Correct Indentation button or
 *         Ctrl+I)
 */
public class TexCorrectIndentationAction implements IEditorActionDelegate {
	private IEditorPart targetEditor;
	private static TexSelections selection;
	private TexEditorTools tools;
	private String indentationString = "";
	private String[] indentationItems;
	private int tabWidth;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IEditorActionDelegate#setActiveEditor(org.eclipse.jface
	 * .action.IAction, org.eclipse.ui.IEditorPart)
	 */
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.targetEditor = targetEditor;
	}

	/**
	 * Returns the TexEditor.
	 */
	private TexEditor getTexEditor() {
		if (targetEditor instanceof TexEditor) {
			return (TexEditor) targetEditor;
		} else {
			throw new RuntimeException("Expecting text editor. Found:"
					+ targetEditor.getClass().getName());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		setIndetationPreferenceInfo(getTexEditor().getPreferences());

		// no environments to indent
		if (this.indentationItems.length == 0) {
			return;
		} else {
			// check if environments have change
			selection = new TexSelections(getTexEditor());
			if (selection != null) {
				this.tools = new TexEditorTools();
				internalRun();
			}
		}
	}

	private void internalRun() {
		indent();
	}

	/**
	 * Initializes indentation information from preferencepage
	 */
	private void setIndetationPreferenceInfo(
			IPreferenceStore editorPreferenceStore) {
		String indentationEnvironments = TexlipsePlugin
				.getPreference(TexlipseProperties.INDENTATION_ENVS);

		int indentationLevel = TexlipsePlugin.getDefault().getPreferenceStore()
				.getInt(TexlipseProperties.INDENTATION_LEVEL);
		this.tabWidth = editorPreferenceStore
				.getInt(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_TAB_WIDTH);

		this.indentationString = "";
		for (int i = 0; i < indentationLevel; i++) {
			this.indentationString += " ";
		}

		if (indentationEnvironments != null
				&& indentationEnvironments.indexOf(',') > 0) {
			String[] environments = indentationEnvironments.split(",");

			this.indentationItems = new String[environments.length];
			for (int i = 0; i < environments.length; i++) {
				this.indentationItems[i] = environments[i].trim();
			}
		}
		Arrays.sort(this.indentationItems);
	}

	/**
	 * Checks if selection needs indentation and corrects it when necessary.
	 * 
	 * @throws BadLocationException
	 */
	private void indent() throws BadLocationException {
		int index = 0;
		boolean fix = false;
		IDocument document = selection.getDocument();
		selection.selectCompleteLines();
		String delimiter = document.getLineDelimiter(selection
				.getStartLineIndex());
		String[] lines = null;

		lines = internalIdent(document, delimiter, lines);

		// fix lines on at the time
		String selectedLine = "";
		String originalIndentation = "";
		String correctIndentation = "";
		for (int line = selection.getStartLineIndex(); index < lines.length; line++) {
			selectedLine = document.get(document.getLineOffset(line), document
					.getLineLength(line));
			originalIndentation = this.tools.getIndentation(selectedLine,
					this.tabWidth);
			correctIndentation = "";

			// first line of end of document
			if (line == 0 || line == document.getLength() - 1) {

			} else if (line >= document.getLength()) {
				return;
			} else if (selectedLine.indexOf("\\end") != -1) { // selected line
				// is \end{...}
				// -line
				String endText = this.tools.getEndLine(selectedLine.trim(),
						"\\end");

				if (Arrays.binarySearch(this.indentationItems, this.tools
						.getEnvironment(endText)) >= 0) {
					int matchingBegin = this.tools.findMatchingBeginEquation(
							document, line, this.tools.getEnvironment(endText));

					if (matchingBegin < selection.getStartLineIndex()) {
						correctIndentation = this.tools.getIndentation(
								document, matchingBegin, "\\begin",
								this.tabWidth);
					} else {
						correctIndentation = this.tools.getIndentation(
								lines[matchingBegin
										- selection.getStartLineIndex()],
								this.tabWidth);
					}
				}
			} else { // otherwise indentation is determined by the previous line
				String lineText = "";

				// previous line is outside the selection
				if (index == 0) {
					lineText = document.get(document.getLineOffset(line - 1),
							document.getLineLength(line - 1));
				} else { // previous line is inside the selection
					lineText = lines[index - 1];
				}

				// previous line is \begin{...} -line
				if (lineText.indexOf("\\begin") != -1) {
					String endText = this.tools.getEndLine(lineText.trim(),
							"\\begin");
					if (Arrays.binarySearch(this.indentationItems, this.tools
							.getEnvironment(endText)) >= 0) {
						correctIndentation = this.tools.getIndentation(
								lineText, this.tabWidth)
								+ this.indentationString;
					}
				} else { // previous line is something else
					correctIndentation = this.tools.getIndentation(lineText,
							this.tabWidth);
				}
			}
			if (!correctIndentation.equals(originalIndentation)) {
				fix = true;
			}
			lines[index] = correctIndentation.concat(lines[index].trim());
			index++;
		}
		String newText = lines[0];
		for (int i = 1; i < lines.length; i++) {
			newText += delimiter;
			newText += lines[i];
		}
		if (fix) {
			document.replace(document.getLineOffset(selection
					.getStartLineIndex()), selection.getSelLength(), newText);
		}
	}

	private String[] internalIdent(IDocument document, String delimiter,
			String[] lines) {
		lines = document.get(
				document.getLineOffset(selection.getStartLineIndex()),
				selection.getSelLength()).split(delimiter);
		return lines;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action
	 * .IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof TextSelection) {
			action.setEnabled(true);
			return;
		}
		action.setEnabled(targetEditor instanceof ITextEditor);
	}
}