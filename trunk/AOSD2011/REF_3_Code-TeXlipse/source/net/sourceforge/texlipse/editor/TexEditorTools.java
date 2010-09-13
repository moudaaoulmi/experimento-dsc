/*
 * $Id: TexEditorTools.java,v 1.5 2007/11/13 14:06:04 borisvl Exp $
 *
 * Copyright (c) 2004-2005 by the TeXlapse Team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package net.sourceforge.texlipse.editor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.aspectj.lang.SoftException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextUtilities;

/**
 * Offers general tools for different TexEditor features. Tools are used mainly
 * to implement the word wrap and the indentation methods.
 * 
 * @author Laura Takkinen
 * @author Antti Pirinen
 * @author Oskar Ojala
 */
public class TexEditorTools extends DefaultIndentLineAutoEditStrategy {

	/**
	 * Matches some simple LaTeX -commands
	 */
	private static final Pattern simpleCommandPattern = Pattern
			.compile("\\\\([a-zA-Z]+)\\s*\\{(.*?)\\}\\s*");

	public TexEditorTools() {
	}

	/**
	 * Calculates the number of tabs ('\t') in given text.
	 * 
	 * @param text
	 *            Text where tabs are calculated.
	 * @return number of found tabs.
	 */
	public int numberOfTabs(String text) {
		int count = 0;
		char[] array = text.toCharArray();
		if (array.length == 0) {
			return count;
		}
		for (int i = 0; i < array.length; i++) {
			if (array[i] == '\t') {
				count++;
			}
		}
		return count;
	}

	/**
	 * Calculates the number of spaces (' ') in given text.
	 * 
	 * @param text
	 *            Text where spaces are calculated.
	 * @return number of found spaces.
	 */
	public int numberOfSpaces(String text) {
		int count = 0;
		char[] array = text.toCharArray();
		if (array.length == 0) {
			return count;
		}
		for (int i = 0; i < array.length; i++) {
			if (array[i] == ' ') {
				count++;
			} else {
				break;
			}
		}
		return count;
	}

	/**
	 * Returns the indentation of the given string. If the indentation contains
	 * tabular characters they are converted to the spaces.
	 * 
	 * @param text
	 *            source where to find the indentation
	 * @param tabWidth
	 *            how many spaces one tabular character is
	 * @return the indentation of the line
	 */
	public String getIndentation(String text, int tabWidth) {
		String indentation = "";
		char[] array = text.toCharArray();

		if (array.length == 0) {
			return indentation;
		}

		if (array[0] == ' ' || array[0] == '\t') {
			int tabs = numberOfTabs(text);
			int spaces = numberOfSpaces(text) - tabs + (tabs * tabWidth);
			if (spaces > 0) {
				for (int i = 0; i < spaces; i++) {
					indentation += " ";
				}
			}
		}
		return indentation;
	}

	/**
	 * Returs indentation string without tabs. Method calculates number of tabs
	 * of text and converts them to spaces.
	 * 
	 * @param document
	 *            Document that contains the text.
	 * @param line
	 *            Line for whitch the indentation is calculated.
	 * @param text
	 *            Text that marks the beginning of the text in given line.
	 * @param tabWidth
	 *            Number of spaces in tab.
	 * @return Indentation String.
	 */
	public String getIndentation(IDocument document, int line, String text,
			int tabWidth) {
		String indentation = "";
		if (line == -1 || line >= document.getNumberOfLines()) {
			return indentation;
		}
		indentation = internalGetIndentation(document, line, text, tabWidth,
				indentation);
		return indentation;
	}

	private String internalGetIndentation(IDocument document, int line,
			String text, int tabWidth, String indentation) {
		String lineText = document.get(document.getLineOffset(line), document
				.getLineLength(line));
		int beginIndex = lineText.indexOf(text);
		int tabs = numberOfTabs(lineText.substring(0, beginIndex));
		int spaces = beginIndex - tabs + (tabs * tabWidth);
		if (spaces > 0) {
			for (int i = 0; i < spaces; i++) {
				indentation += " ";
			}
		}
		return indentation;
	}

	/**
	 * Calculates indentation of a line where given command exists.
	 * 
	 * @param document
	 *            Document that includes the line.
	 * @param command
	 *            Command that determines the document line.
	 * @return Indentation String.
	 */
	public String getIndentation(IDocument document, DocumentCommand command) {
		String indentation = "";
		int line = -1;
		line = document.getLineOfOffset(command.offset);
		if (line > -1) {
			int start = document.getLineOffset(line);
			int end = start + document.getLineLength(line) - 1;
			int whiteend = findEndOfWhiteSpace(document, start, end);
			if (whiteend > start)
				indentation = document.get(start, whiteend - start);
		}
		return indentation;
	}

	/**
	 * Gets substring of the given text by removing the given prexif from the
	 * string. Removes also white spaces from the substring. For example: if
	 * text is "\begin  {itemize}" and prefix is "\begin" return value is
	 * "{itemize}".
	 * 
	 * @param text
	 *            Text from where the substring is created.
	 * @param prefix
	 *            Prefix that is removed from the text.
	 * @return Substring of the text.
	 * @throws IndexOutOfBoundsException
	 */
	public String getEndLine(String text, String prefix)
			throws IndexOutOfBoundsException {

		String endOfLine = text.substring(prefix.length());
		return endOfLine.trim();
	}

	/**
	 * Gets environment string from the given text.
	 * 
	 * @param text
	 *            Text where the string is searched.
	 * @return Environment string (itemize, table...). If nothing is found,
	 *         empty string is returned.
	 * @throws IndexOutOfBoundsException
	 */
	public String getEnvironment(String text) throws IndexOutOfBoundsException {
		int begin = text.indexOf('{');
		int end = text.indexOf('}');

		// "{" has to be to the first character of the text
		if (begin == 0 && end > begin) {
			return text.substring(begin + 1, end);
		}
		return "";
	}

	/**
	 * Checks if the target text ends with a new line.
	 * 
	 * @param document
	 *            Document that includes the text.
	 * @param text
	 *            Text that is investigated.
	 * @return <code>true</code> if the text ends with new line otherwise
	 *         <code>false</code>.
	 */
	public boolean endsWithNewline(IDocument document, String text) {
		String[] newlines = document.getLegalLineDelimiters();
		boolean ends = false;
		for (int i = 0; i < newlines.length; i++) {
			String delimiter = newlines[i];
			if (text.indexOf(delimiter) != -1)
				ends = true;
		}
		return ends;
	}

	/**
	 * Checks if the given character is legal line delimiter
	 * 
	 * @param d
	 *            IDocument.
	 * @param c
	 *            DocumentCommand.
	 * @return <code>true</code>, if the character is legal line delimiter,
	 *         <code>false</code> otherwise
	 */
	public boolean isLineDelimiter(IDocument d, DocumentCommand c) {
		return endsWithNewline(d, c.text);
	}

	/**
	 * Finds matching \begin{environment} expression to the given
	 * \end{environment} line.
	 * 
	 * @param document
	 *            Document that contains line.
	 * @param line
	 *            End line for which the matching beging line is searched.
	 * @param environment
	 *            String that defines begin-end environment type (itemize,
	 *            enumerate...)
	 * @return Returs line number of matching begin equation, if match does not
	 *         found -1 is returned.
	 * @throws BadLocationException
	 */
	public int findMatchingBeginEquation(IDocument document, int line,
			String environment) throws BadLocationException {
		int startLine = line - 1;
		int startOffset = document.getLineOffset(startLine);
		int lineLength = document.getLineLength(startLine);
		String lineText = document.get(startOffset, lineLength).trim();
		boolean noMatch = true;
		int beginCounter = 0;
		int endCounter = 1; // one end has been detected earlier

		while (noMatch) {
			if (lineText.startsWith("\\begin")) {
				String end = getEndLine(lineText, "\\begin");
				if (getEnvironment(end).equals(environment)) {
					beginCounter++;

					if (beginCounter == endCounter) {
						return startLine;
					}
				}
			} else if (lineText.startsWith("\\end")) {
				String end = getEndLine(lineText, "\\end");
				if (getEnvironment(end).equals(environment)) {
					endCounter++;
				}
			}
			if (startLine > 0) {
				startLine--;
				startOffset = document.getLineOffset(startLine);
				lineLength = document.getLineLength(startLine);
				lineText = document.get(startOffset, lineLength).trim();
			} else {
				noMatch = false;
			}
		}
		return -1;
	}

	/**
	 * Returns the longest legal line delimiter.
	 * 
	 * @param document
	 *            IDocument
	 * @param command
	 *            DocumentCommand
	 * @return the longest legal line delimiter
	 */
	public String getLineDelimiter(IDocument document, DocumentCommand command) {
		String delimiter = "\n";
		delimiter = internalGetLineDelimiter(document, delimiter);
		return delimiter == null ? "\n" : delimiter;
	}

	private String internalGetLineDelimiter(IDocument document, String delimiter) {
		delimiter = document.getLineDelimiter(0);
		return delimiter;
	}

	public String getLineDelimiter(IDocument document) {
		return getLineDelimiter(document, null);
	}

	/**
	 * Checks the first character of command.text is ' ' or '\t'.
	 * 
	 * @param command
	 *            DocumentCommand that is checked.
	 * @return <code>true</code> if the first character is white space
	 *         <code>false</code> otherwise
	 */
	public boolean isWhiteSpace(DocumentCommand command) {
		char[] array = command.text.toCharArray();
		if (array[0] == ' ' || array[0] == '\t') {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * <pre>
	 * Returns the index of the start position of the command.offset.
	 *  0_1_2_3_4_5_6_7_8_
	 * |a|a|a|c|b|b|b| | | ...
	 *        &circ;
	 *        New character here -&gt; return 3
	 * </pre>
	 * 
	 * @param document
	 *            IDocument that contais the command.
	 * @param command
	 *            DocumentCommand for which the index is calculated.
	 * @param delim
	 *            determines if line delimiters are counted to the line length
	 * @return Index of the command.offset, -1 is returned if command is last
	 *         character of line.
	 */
	public int getIndexAtLine(IDocument document, DocumentCommand command,
			boolean delim) {
		int index = -1;
		index = internalGetIndexLine(document, command, delim, index);
		return index;
	}

	private int internalGetIndexLine(IDocument document,
			DocumentCommand command, boolean delim, int index) {
		int line = document.getLineOfOffset(command.offset);
		int lineLength;

		if (delim) {
			lineLength = getLineLength(document, command, true);
		} else {
			lineLength = getLineLength(document, command, false);
		}

		if ((document.getLineOffset(line) + lineLength) == command.offset) {
			// command.text is last character at the line
			index = -1;
		} else {
			index = command.offset - document.getLineOffset(line);
		}
		return index;
	}

	/**
	 * Returns a length of a line.
	 * 
	 * @param document
	 *            IDocument that contains the line.
	 * @param command
	 *            DocumentCommand that determines the line.
	 * @param delim
	 *            are line delimiters counted to the line length
	 * @return the line length
	 */
	public int getLineLength(IDocument document, DocumentCommand command,
			boolean delim) {
		return getLineLength(document, command, delim, 0);
	}

	/**
	 * Returns a length of a line.
	 * 
	 * @param document
	 *            IDocument that contains the line.
	 * @param command
	 *            DocumentCommand that determines the line.
	 * @param delim
	 *            are line delimiters counted to the line length
	 * @param target
	 *            -1 = previous line, 0 = current line, 1 = next line etc...
	 * @return the line length
	 */
	public int getLineLength(IDocument document, DocumentCommand command,
			boolean delim, int target) {
		int line = 0;

		int length = 0;
		length = internalGetLineLength(document, command, delim, target, line,
				length);
		return length;
	}

	private int internalGetLineLength(IDocument document,
			DocumentCommand command, boolean delim, int target, int line,
			int length) {
		line = document.getLineOfOffset(command.offset) + target;
		if (line < 0 || line >= document.getNumberOfLines()) {
			// line = document.getLineOfOffset(command.offset);
			// return 0;para possibilita a extracao...
			throw new SoftException(null);
		}

		length = document.getLineLength(line);
		if (length == 0) {
			// return 0; para possibilita a extracao...
			throw new SoftException(null);
		}
		if (!delim) {
			String txt = document.get(document.getLineOffset(line), document
					.getLineLength(line));
			String[] del = document.getLegalLineDelimiters();
			int cnt = TextUtilities.endsWith(del, txt);
			if (!delim && cnt > -1) {
				length = length - del[cnt].length();
			}
		}
		return length;
	}

	/**
	 * Returns the location of last white space character. The first character
	 * at a row is 0 and last is <code>lineText.length - 1</code> If
	 * <code>command.text</code> is a white space, it is NOT counted.
	 * 
	 * @param document
	 *            IDocument that contains the command.
	 * @param command
	 *            DocumentCommand that determines the row.
	 * @return the index of the last white space character, returns -1 if not
	 *         found.
	 */
	public int getLastWhiteSpacePosition(IDocument document,
			DocumentCommand command) {
		String txt = "";
		txt = getStringAt(document, command, false);
		return getLastWhiteSpacePosition(txt);
	}

	/**
	 * Returns the location of last white space character. The fisrt character
	 * at a row is 0 and last is <code>lineText.length - 1 </code>
	 * 
	 * @param text
	 *            to search
	 * @return index of last white space character, returns -1 if not found.
	 */
	public int getLastWhiteSpacePosition(String text) {
		int lastSpace = text.lastIndexOf(' ');
		int lastTab = text.lastIndexOf('\t');
		return (lastSpace > lastTab ? lastSpace : lastTab);
	}

	/**
	 * Returns a text String of the (line + <code>lineDif</code>).
	 * 
	 * @param document
	 *            IDocument that contains the line.
	 * @param command
	 *            DocumentCommand that determines the line.
	 * @param delim
	 *            are delimiters included
	 * @param lineDif
	 *            0 = current line, 1 = next line, -1 previous line etc...
	 * @return the text of the line.
	 */
	public String getStringAt(IDocument document, DocumentCommand command,
			boolean delim, int lineDif) {
		String line = "";
		int lineBegin = 0;
		int lineLength = 0;
		line = internalGetStringAt(document, command, delim, lineDif, 
				lineBegin, lineLength,line);
		return line;
	}

	private String internalGetStringAt(IDocument document,
			DocumentCommand command, boolean delim, int lineDif, 
			int lineBegin, int lineLength,String line) {
		if (delim) {
			lineLength = getLineLength(document, command, true, lineDif);
		} else {
			lineLength = getLineLength(document, command, false, lineDif);
		}
		if (lineLength > 0) {
			lineBegin = document.getLineOffset(document
					.getLineOfOffset(command.offset)
					+ lineDif);
			line = document.get(lineBegin, lineLength);
		}
		return line;
	}

	/**
	 * Returns a text String of the line.
	 * 
	 * @param d
	 *            IDocument that contains the line.
	 * @param c
	 *            DocumentCommand that determines the line.
	 * @param del
	 *            Are delimiters included?
	 * @return The text of the current line (lineDif = 0).
	 */
	public String getStringAt(IDocument d, DocumentCommand c, boolean del) {
		return getStringAt(d, c, del, 0);
	}

	/**
	 * Detects the position of the first white space character smaller than the
	 * limit. The first character at a row is 0 and last is lineText.length-1
	 * 
	 * @param text
	 *            to search
	 * @param limit
	 *            the detected white space must be before this
	 * @return index of last white space character, returns -1 if not found.
	 */
	public int getLastWSPosition(String text, int limit) {
		int index = -1;
		if (text.length() >= limit && limit > -1) {
			String temp = text.substring(0, limit); // TODO limit+1?
			int lastSpace = temp.lastIndexOf(' ');
			int lastTab = temp.lastIndexOf('\t');
			index = (lastSpace > lastTab ? lastSpace : lastTab);
		}
		return index;
	}

	/**
	 * Detects the position of the first white space character larger than the
	 * limit. The first character at a row is 0 and last is lineText.length-1
	 * 
	 * @param text
	 *            to search
	 * @param limit
	 *            the detected white space is the first white space after this
	 * @return index of first white space character, returns -1 if not found.
	 */
	public int getFirstWSPosition(String text, int limit) {
		int index = -1;
		if (text.length() > limit && limit > -1) {
			String temp = text.substring(limit + 1);
			int firstSpace = temp.indexOf(' ');
			int firstTab = temp.indexOf('\t');

			if (firstSpace == -1 && firstTab != -1) {
				index = firstTab + limit + 1;
			} else if (firstSpace != -1 && firstTab == -1) {
				index = firstSpace + limit + 1;
			} else if (firstSpace > -1 && firstTab > -1) {
				index = (firstSpace < firstTab ? firstSpace : firstTab) + limit
						+ 1;
			}
		}
		return index;
	}

	/**
	 * Trims the beginning of the given text.
	 * 
	 * @param text
	 *            String that will be trimmed.
	 * @return trimmed String.
	 */
	public String trimBegin(String text) {
		char[] array = text.toCharArray();
		int i = 0;
		for (; i < array.length; i++) {
			if (array[i] != ' ' && array[i] != '\t')
				break;
		}
		return text.substring(i);
	}

	/**
	 * Trims the end of the given text.
	 * 
	 * @param text
	 *            String that will be trimmed.
	 * @return trimmed String.
	 */
	public String trimEnd(String text) {
		char[] array = text.toCharArray();
		int i = array.length - 1;
		for (; i >= 0; i--) {
			if (array[i] != ' ' && array[i] != '\t')
				break;
		}
		return text.substring(0, i + 1);
	}

	/**
	 * Checks if the target line begins with latex-command word.
	 * 
	 * @param d
	 *            IDocument
	 * @param c
	 *            DocumentCommand
	 * @param line
	 *            -1 = previous line, 0 = current line, 1 = next line etc...
	 * @return <code>true</code> if the line contains the latex command word,
	 *         <code>false</code> otherwise
	 */
	public boolean isLineCommandLine(IDocument d, DocumentCommand c, int line) {
		String lineTxt = getStringAt(d, c, true, line);
		return isLineCommandLine(lineTxt);
	}

	/**
	 * Checks if the target text begins with a LaTeX command.
	 * 
	 * @param text
	 *            source string
	 * @return <code>true</code> if the line contains the latex command word,
	 *         <code>false</code> otherwise
	 */
	public boolean isLineCommandLine(String text) {
		String txt = text.trim();
		Matcher m = simpleCommandPattern.matcher(txt);
		if (m.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if the target line is a comment line
	 * 
	 * @param d
	 *            IDocument
	 * @param c
	 *            DocumentCommand
	 * @param line
	 *            -1 = previous line, 0 = current line, 1 = next line etc...
	 * @return <code>true</code> if the line is a comment line,
	 *         <code>false</code> otherwise
	 */
	public boolean isLineCommentLine(IDocument d, DocumentCommand c, int line) {
		String lineTxt = getStringAt(d, c, true, line);
		return isLineCommentLine(lineTxt);
	}

	/**
	 * Checks if the target txt is a comment line
	 * 
	 * @param text
	 *            source text
	 * @return <code>true</code> if line starts with %-character,
	 *         <code>false</code> otherwise
	 */
	public boolean isLineCommentLine(String text) {
		return text.trim().startsWith("%");
	}

	/**
	 * Checks is the line begins with \item key word
	 * 
	 * @param d
	 *            IDocument
	 * @param c
	 *            DocumentCommand
	 * @param line
	 *            -1 = previous line, 0 = current line, 1 = next line etc...
	 * @return <code>true</code> if the line contains the item key word,
	 *         <code>false</code> otherwise
	 * @return True if the line begins with \item, false otherwise
	 */
	public boolean isLineItemLine(IDocument d, DocumentCommand c, int line) {
		String lineTxt = getStringAt(d, c, true, line);
		return isLineItemLine(lineTxt);
	}

	/**
	 * Checks is the line begins with \item keyword
	 * 
	 * @param text
	 *            string to test
	 * @return <code>true</code> if the line contains the item key word,
	 *         <code>false</code> otherwise
	 */
	public boolean isLineItemLine(String text) {
		return text.trim().startsWith("\\item");
	}

	// Oskar's additions

	/**
	 * Returns the indentation of the given string taking into account if the
	 * string starts with a comment. The comment character is included in the
	 * output.
	 * 
	 * @param text
	 *            source where to find the indentation
	 * @return The indentation of the line including the comment
	 */
	public String getIndentationWithComment(String text) {
		StringBuffer indentation = new StringBuffer();
		char[] array = text.toCharArray();

		if (array.length == 0) {
			return indentation.toString();
		}

		int i = 0;
		while (i < array.length && (array[i] == ' ' || array[i] == '\t')) {
			indentation.append(array[i]);
			i++;
		}
		if (i < array.length && array[i] == '%') {
			indentation.append("% ");
		}

		return indentation.toString();
	}

	public String wrapWordString(String input, String indent, int width,
			String delim) {
		String[] words = input.split("\\s");
		if (input.length() == 0 || words.length == 0) {
			return "";
		}
		StringBuffer sbout = new StringBuffer(indent);
		sbout.append(words[0]);
		int currLength = indent.length() + words[0].length();
		for (int j = 1; j < words.length; j++) {
			if (words[j].length() + currLength <= width) {
				sbout.append(" ");
				sbout.append(words[j]);
				currLength += 1 + words[j].length();
			} else {
				sbout.append(delim);
				sbout.append(indent);
				sbout.append(words[j]);
				currLength = indent.length() + words[j].length();
			}
		}
		sbout.append(delim);
		return sbout.toString();
	}

	public String[] getEnvCommandArg(String text) {
		String txt = text.trim();
		Matcher m = simpleCommandPattern.matcher(txt);
		while (m.find()) {
			if ("begin".equals(m.group(1)) || "end".equals(m.group(1))) {
				return new String[] { m.group(1), m.group(2) };
			}
		}
		return new String[] { "", "" };
	}

	public String getNewlinesAtEnd(String text, String delim) {
		StringBuffer sb = new StringBuffer();
		for (int i = text.length() - delim.length(); i >= 0; i -= delim
				.length()) {
			if (text.regionMatches(i, delim, 0, delim.length())) {
				sb.append(delim);
			} else {
				break;
			}
		}
		return sb.toString();
	}

}
