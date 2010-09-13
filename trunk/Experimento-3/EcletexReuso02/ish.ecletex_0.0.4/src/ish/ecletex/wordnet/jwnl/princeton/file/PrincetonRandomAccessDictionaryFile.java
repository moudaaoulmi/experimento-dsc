/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package ish.ecletex.wordnet.jwnl.princeton.file;

import ish.ecletex.wordnet.jwnl.JWNLRuntimeException;
import ish.ecletex.wordnet.jwnl.data.POS;
import ish.ecletex.wordnet.jwnl.dictionary.file.DictionaryFile;
import ish.ecletex.wordnet.jwnl.dictionary.file.DictionaryFileType;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * A <code>RandomAccessDictionaryFile</code> that accesses files named with
 * Princeton's dictionary file naming convention.
 */
public class PrincetonRandomAccessDictionaryFile extends
		AbstractPrincetonRandomAccessDictionaryFile {
	/** Read-only file permission. */
	public static final String READ_ONLY = "r";
	/** Read-write file permission. */
	public static final String READ_WRITE = "rw";

	/** The random-access file. */
	private RandomAccessFile _file = null;
	/** The file permissions to use when opening a file. */
	protected String _permissions;

	public DictionaryFile newInstance(String path, POS pos,
			DictionaryFileType fileType) {
		return new PrincetonRandomAccessDictionaryFile(path, pos, fileType);
	}

	public PrincetonRandomAccessDictionaryFile() {
	}

	public PrincetonRandomAccessDictionaryFile(String path, POS pos,
			DictionaryFileType fileType) {
		this(path, pos, fileType, READ_ONLY);
	}

	public PrincetonRandomAccessDictionaryFile(String path, POS pos,
			DictionaryFileType fileType, String permissions) {
		super(path, pos, fileType);
		_permissions = permissions;
	}

	public String readLine() throws IOException {
		if (isOpen()) {
			return _file.readLine();
		} else {
			throw new JWNLRuntimeException("PRINCETON_EXCEPTION_001");
		}
	}

	public void seek(long pos) throws IOException {
		_file.seek(pos);
	}

	public long getFilePointer() throws IOException {
		return _file.getFilePointer();
	}

	public boolean isOpen() {
		return _file != null;
	}

	public void close() {
		_file.close();
	}

	protected void openFile(File path) throws IOException {
		_file = new RandomAccessFile(path, _permissions);
	}

	public long length() throws IOException {
		return _file.length();
	}

	public int read() throws IOException {
		return _file.read();
	}
}