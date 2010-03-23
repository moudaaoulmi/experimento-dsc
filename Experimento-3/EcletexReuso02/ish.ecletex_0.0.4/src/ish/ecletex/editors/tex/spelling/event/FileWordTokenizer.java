package ish.ecletex.editors.tex.spelling.event;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class tokenizes a input file.
 * 
 * <p>
 * Any takers to do this efficiently?? Doesnt need to replace any words to start
 * with. I need this to get an idea of how quick the spell checker is.
 * </p>
 */
public class FileWordTokenizer extends AbstractWordTokenizer {

	// ~ Instance/static variables
	// ...............................................

	// private File inFile;

	// ~ Constructors
	// ............................................................

	/**
	 * Creates a new FileWordTokenizer object.
	 * 
	 * @param inputFile
	 */
	public FileWordTokenizer(File inputFile) {
		super(stringValue(inputFile));
	}

	public FileWordTokenizer(File inputFile, WordFinder finder) {
		super(finder);
		finder.setText(stringValue(inputFile));
	}

	// ~ Methods
	// .................................................................

	/**
	 * 
	 * 
	 * @params
	 * @throws WordNotFoundException
	 *             current word not yet set.
	 */
	public void replaceWord(String s) {
	}

	private static String stringValue(File inFile) {
		StringBuffer out = new StringBuffer("");
		internalStringValue(inFile, out);
		return out.toString();
	}

	private static void internalStringValue(File inFile, StringBuffer out) {
		BufferedReader in = new BufferedReader(new FileReader(inFile));
		char[] c = new char[100];
		int count;
		while ((count = in.read(c, 0, c.length)) != -1) {
			out.append(c, 0, count);
		}
		in.close();
	}
}