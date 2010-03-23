package ish.ecletex.wordnet.jwnl.dictionary;

import ish.ecletex.wordnet.jwnl.JWNLException;
import ish.ecletex.wordnet.jwnl.data.DatabaseDictionaryElementFactory;
import ish.ecletex.wordnet.jwnl.data.DictionaryElement;
import ish.ecletex.wordnet.jwnl.data.Exc;
import ish.ecletex.wordnet.jwnl.data.IndexWord;
import ish.ecletex.wordnet.jwnl.data.POS;
import ish.ecletex.wordnet.jwnl.data.Synset;
import ish.ecletex.wordnet.jwnl.dictionary.database.DatabaseManager;
import ish.ecletex.wordnet.jwnl.dictionary.database.Query;
import ish.ecletex.wordnet.jwnl.util.factory.Param;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class DatabaseBackedDictionary extends AbstractCachingDictionary {
	/**
	 * <code>MorphologicalProcessor</code> class install parameter. The value
	 * should be the class of <code>MorphologicalProcessor</code> to use.
	 */
	public static final String MORPH = "morphological_processor";
	public static final String DICTIONARY_ELEMENT_FACTORY = "dictionary_element_factory";
	public static final String DATABASE_MANAGER = "database_manager";

	private DatabaseDictionaryElementFactory _elementFactory;
	private DatabaseManager _dbManager;

	public DatabaseBackedDictionary() {
	}

	private DatabaseBackedDictionary(MorphologicalProcessor morph,
			DatabaseDictionaryElementFactory elementFactory,
			DatabaseManager dbManager) {
		super(morph);
		_elementFactory = elementFactory;
		_dbManager = dbManager;
	}

	public void install(Map params) throws JWNLException {
		Param param = (Param) params.get(MORPH);
		MorphologicalProcessor morph = (param == null) ? null
				: (MorphologicalProcessor) param.create();

		param = (Param) params.get(DICTIONARY_ELEMENT_FACTORY);
		DatabaseDictionaryElementFactory factory = (param == null) ? null
				: (DatabaseDictionaryElementFactory) param.create();

		param = (Param) params.get(DATABASE_MANAGER);
		DatabaseManager manager = (param == null) ? null
				: (DatabaseManager) param.create();

		setDictionary(new DatabaseBackedDictionary(morph, factory, manager));
	}

	public IndexWord getIndexWord(POS pos, String lemma) throws JWNLException {
		lemma = prepareQueryString(lemma);
		IndexWord word = null;
		if (lemma.length() > 0) {
			if (isCachingEnabled()) {
				word = getCachedIndexWord(new POSKey(pos, lemma));
			}
			if (word == null) {
				Query query = null;
				word = internalGetIndexWord(pos, lemma, word, query);
			}
		}
		return word;
	}

	private IndexWord internalGetIndexWord(POS pos, String lemma,
			IndexWord word, Query query) throws JWNLException {
		query = _dbManager.getIndexWordSynsetsQuery(pos, lemma);
		word = _elementFactory.createIndexWord(pos, lemma, query.execute());
		if (word != null && isCachingEnabled()) {
			cacheIndexWord(new POSKey(pos, lemma), word);
		}
		return word;
	}

	public Iterator getIndexWordIterator(POS pos) throws JWNLException {
		Query query = _dbManager.getIndexWordLemmasQuery(pos);
		return new IndexWordIterator(pos, query);
	}

	public Iterator getIndexWordIterator(POS pos, String substring)
			throws JWNLException {
		Query query = _dbManager.getIndexWordLemmasQuery(pos, substring);
		return new IndexWordIterator(pos, query);
	}

	public IndexWord getRandomIndexWord(POS pos) throws JWNLException {
		Query query = _dbManager.getRandomIndexWordQuery(pos);
		String lemma = null;

		lemma = internalGetRandomIndexWord(query, lemma);

		return getIndexWord(pos, lemma);
	}

	private String internalGetRandomIndexWord(Query query, String lemma)
			throws JWNLException {
		query.execute();
		query.getResults().next();
		lemma = query.getResults().getString(1);
		return lemma;
	}

	public Synset getSynsetAt(POS pos, long offset) throws JWNLException {
		Synset synset = null;
		if (isCachingEnabled()) {
			synset = getCachedSynset(new POSKey(pos, offset));
		}
		if (synset == null) {
			Query query = null;
			Query wordQuery = null;
			Query pointerQuery = null;
			Query verbFrameQuery = null;
			synset = internalGetSynsetAt(pos, offset, synset, query, wordQuery,
					pointerQuery, verbFrameQuery);
		}
		return synset;
	}

	private Synset internalGetSynsetAt(POS pos, long offset, Synset synset,
			Query query, Query wordQuery, Query pointerQuery,
			Query verbFrameQuery) throws JWNLException {
		query = _dbManager.getSynsetQuery(pos, offset);
		wordQuery = _dbManager.getSynsetWordQuery(pos, offset);
		pointerQuery = _dbManager.getPointerQuery(pos, offset);
		verbFrameQuery = _dbManager.getVerbFrameQuery(pos, offset);
		synset = _elementFactory.createSynset(pos, offset, query.execute(),
				wordQuery.execute(), pointerQuery.execute(), verbFrameQuery
						.execute());
		if (synset != null && isCachingEnabled()) {
			cacheSynset(new POSKey(pos, offset), synset);
		}
		return synset;
	}

	public Iterator getSynsetIterator(POS pos) throws JWNLException {
		Query query = _dbManager.getSynsetsQuery(pos);
		return new SynsetIterator(pos, query);
	}

	public Exc getException(POS pos, String derivation) throws JWNLException {
		Exc exc = null;
		if (isCachingEnabled()) {
			exc = getCachedException(new POSKey(pos, derivation));
		}
		if (exc == null) {
			Query query = null;
			exc = internalGetException(pos, derivation, exc, query);
		}
		return exc;
	}

	private Exc internalGetException(POS pos, String derivation, Exc exc,
			Query query) throws JWNLException {
		query = _dbManager.getExceptionQuery(pos, derivation);
		exc = _elementFactory.createExc(pos, derivation, query.execute());
		if (exc != null && isCachingEnabled()) {
			cacheException(new POSKey(pos, derivation), exc);
		}
		return exc;
	}

	public Iterator getExceptionIterator(POS pos) throws JWNLException {
		Query query = _dbManager.getExceptionsQuery(pos);
		return new ExceptionIterator(pos, query);
	}

	public void close() {
	}

	private abstract class DatabaseElementIterator implements Iterator {
		private POS _pos;
		private Query _lemmas;
		private boolean _advanced = false;
		private boolean _hasNext = false;

		protected DatabaseElementIterator(POS pos, Query query) {
			_pos = pos;
			_lemmas = query;
		}

		public boolean hasNext() {
			if (!_advanced) {
				_advanced = true;
				internalHasNext();
			}
			if (!_hasNext) {
				_lemmas.close();
			}
			return _hasNext;
		}

		private void internalHasNext() {
			_hasNext = getResults().next();
		}

		public Object next() {
			if (hasNext()) {
				_advanced = false;
				return internalNext();
			}
			throw new NoSuchElementException();
		}

		private Object internalNext() {
			return createElement();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		protected abstract DictionaryElement createElement() throws Exception;

		protected POS getPOS() {
			return _pos;
		}

		protected ResultSet getResults() throws SQLException {
			if (!_lemmas.isExecuted()) {
				_lemmas.execute();
			}
			return _lemmas.getResults();
		}

		protected void finalize() throws Throwable {
			_lemmas.close();
		}
	}

	private class IndexWordIterator extends DatabaseElementIterator {
		public IndexWordIterator(POS pos, Query query) {
			super(pos, query);
		}

		protected DictionaryElement createElement() throws Exception {
			String lemma = getResults().getString(1);
			return getIndexWord(getPOS(), lemma);
		}
	}

	private class SynsetIterator extends DatabaseElementIterator {
		public SynsetIterator(POS pos, Query query) {
			super(pos, query);
		}

		protected DictionaryElement createElement() throws Exception {
			long offset = getResults().getLong(1);
			return getSynsetAt(getPOS(), offset);
		}
	}

	private class ExceptionIterator extends DatabaseElementIterator {
		public ExceptionIterator(POS pos, Query query) {
			super(pos, query);
		}

		protected DictionaryElement createElement() throws Exception {
			String derivation = getResults().getString(1);
			return getException(getPOS(), derivation);
		}
	}
}