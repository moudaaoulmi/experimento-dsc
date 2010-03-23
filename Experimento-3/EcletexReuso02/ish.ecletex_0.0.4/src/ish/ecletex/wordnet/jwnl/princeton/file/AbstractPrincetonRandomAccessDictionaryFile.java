package ish.ecletex.wordnet.jwnl.princeton.file;

import ish.ecletex.wordnet.jwnl.data.POS;
import ish.ecletex.wordnet.jwnl.dictionary.file.DictionaryFileType;
import ish.ecletex.wordnet.jwnl.dictionary.file.RandomAccessDictionaryFile;

public abstract class AbstractPrincetonRandomAccessDictionaryFile extends AbstractPrincetonDictionaryFile
																                                  implements RandomAccessDictionaryFile {
	/** Used for caching the previously accessed file offset. */
	private long _previousOffset;
	/** Used for caching the offset of the line following the line at
	 *  <code>previousOffset</code>. */
	private long _nextOffset;

	public AbstractPrincetonRandomAccessDictionaryFile() {}

	protected AbstractPrincetonRandomAccessDictionaryFile(String path, POS pos, DictionaryFileType fileType) {
		super(path, pos, fileType);
	}

	public void setNextLineOffset(long previousOffset, long nextOffset) {
		_previousOffset = previousOffset;
		_nextOffset = nextOffset;
	}

	public boolean isPreviousLineOffset(long offset) {
		return _previousOffset == offset;
	}

	public long getNextLineOffset() {
		return _nextOffset;
	}
}
