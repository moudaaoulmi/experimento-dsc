/*
 * 
 * A BibtexString is a literal string, such as "Donald E. Knuth"
 * 
 * Created on Mar 17, 2003
 *
 * @author henkel@cs.colorado.edu
 * 
 */
package ish.ecletex.editors.bibtex.dom;

import java.io.PrintWriter;

/**
 * A string - this class is used for numbers as well - if there's a number
 * wrapped in here, the toString() method will be smart enough to leave out the
 * braces, and thus print {1979} as 1979.
 * 
 * @author henkel
 */
public class BibtexString extends BibtexAbstractValue {

	// content does not include the quotes or curly braces around the string!
	private String content;

	/**
	 * content includes the quotes or curly braces around the string!
	 * 
	 * @param content
	 */
	protected BibtexString(BibtexFile file, String content) {
		super(file);
		this.content = content;
	}

	/**
	 * content includes the quotes or curly braces around the string!
	 * 
	 * @return String
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Sets the content.
	 * 
	 * @param content
	 *            The content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ish.ecletex.editors.bibtex.dom.BibtexNode#printBibtex(java.io.PrintWriter
	 * )
	 */
	public void printBibtex(PrintWriter writer) {
		// is this really a number?
		Integer.parseInt(content);
		writer.print(content);
	}

}
