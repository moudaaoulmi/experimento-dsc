/*
 * Created on Mar 29, 2003
 * 
 * @author henkel@cs.colorado.edu
 *  
 */
package ish.ecletex.editors.bibtex.expansions;

import ish.ecletex.editors.bibtex.dom.BibtexEntry;
import ish.ecletex.editors.bibtex.dom.BibtexFile;
import ish.ecletex.editors.bibtex.dom.BibtexString;

import java.util.Iterator;

/**
 * This expander will convert author/editor field values into BibtexPersonList
 * objects.
 * 
 * @author henkel
 */
public class PersonListExpander extends AbstractExpander implements Expander {

	/**
	 * Equivalent to PersonListExpander(expandAuthors,expandEditors,true).
	 * 
	 * @param expandAuthors
	 * @param expandEditors
	 */

	public PersonListExpander(boolean expandAuthors, boolean expandEditors) {
		this(expandAuthors, expandEditors, true);
	}

	/**
	 * @param expandAuthors
	 * @param expandEditors
	 * @param throwAllExpansionExceptions
	 *            Setting this to true means that all exceptions will be thrown
	 *            immediately. Otherwise, the expander will skip over things it
	 *            can't expand and you can use getExceptions to retrieve the
	 *            exceptions later
	 */
	public PersonListExpander(boolean expandAuthors, boolean expandEditors,
			boolean throwAllExpansionExceptions) {
		super(throwAllExpansionExceptions);
		this.expandAuthors = expandAuthors;
		this.expandEditors = expandEditors;
	}

	private boolean expandAuthors, expandEditors;

	/**
	 * This method will expand all author and editor fields (if configured in
	 * the constructor) into BibtexPersonList values. Before you call this
	 * method, please make sure you have used the MacroReferenceExpander.
	 * 
	 * If you use the flag throwAllExpansionExceptions set to false, you can
	 * retrieve all the exceptions using getExceptions()
	 * 
	 * @param file
	 */
	public void expand(BibtexFile file) throws ExpansionException {
		for (Iterator entryIt = file.getEntries().iterator(); entryIt.hasNext();) {
			Object next = entryIt.next();
			if (!(next instanceof BibtexEntry))
				continue;
			BibtexEntry entry = (BibtexEntry) next;
			if (expandAuthors && entry.getFieldValue("author") != null) {
				internalExpand(entry, "author");
			}
			if (expandEditors && entry.getFieldValue("editor") != null) {
				internalExpand(entry, "editor");
			}
		}
		finishExpansion();
	}

	private void internalExpand(BibtexEntry entry, String str)
			throws ExpansionException {
		entry.setField(str, BibtexPersonListParser.parse((BibtexString) entry
				.getFieldValue(str)));
	}

}
