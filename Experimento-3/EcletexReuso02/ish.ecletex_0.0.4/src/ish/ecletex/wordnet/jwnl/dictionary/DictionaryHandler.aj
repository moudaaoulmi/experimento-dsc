package ish.ecletex.wordnet.jwnl.dictionary;

import ish.ecletex.wordnet.jwnl.JWNLException;
import ish.ecletex.wordnet.jwnl.data.Exc;
import ish.ecletex.wordnet.jwnl.data.IndexWord;
import ish.ecletex.wordnet.jwnl.data.POS;
import ish.ecletex.wordnet.jwnl.data.Synset;
import ish.ecletex.wordnet.jwnl.dictionary.database.DatabaseManager;
import ish.ecletex.wordnet.jwnl.dictionary.database.Query;
import ish.ecletex.wordnet.jwnl.dictionary.file.DictionaryCatalogSet;
import ish.ecletex.wordnet.jwnl.dictionary.file.DictionaryFile;
import ish.ecletex.wordnet.jwnl.dictionary.file.DictionaryFileType;
import ish.ecletex.wordnet.jwnl.util.factory.Param;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.SoftException;

public privileged aspect DictionaryHandler {

	String[] msg = { "DICTIONARY_EXCEPTION_006", "DICTIONARY_EXCEPTION_004" };

	Map queryMap = new HashMap();
	Map wordQueryMap = new HashMap();
	Map pointerQueryMap = new HashMap();
	Map verbFrameQueryMap = new HashMap();

	pointcut internalGetExceptionHandler2(): execution(private Exc FileBackedDictionary.internalGetException(POS , String , Exc , long));

	pointcut getRandomIndexWordHandler(): execution(public IndexWord FileBackedDictionary.getRandomIndexWord(POS));

	pointcut internalGetIndexWordHandler(): execution(private IndexWord DatabaseBackedDictionary.internalGetIndexWord(POS, String,
			IndexWord, Query) );

	pointcut getIndexWordSynsetsQueryHandler() : call(* DatabaseManager.getIndexWordSynsetsQuery(POS , String)) && withincode(private IndexWord DatabaseBackedDictionary.internalGetIndexWord(POS, String,
			IndexWord, Query));

	pointcut internalGetRandomIndexWordHandler(): execution(private String DatabaseBackedDictionary.internalGetRandomIndexWord(Query, String));

	pointcut internalGetSynsetAtHandler(): execution(private Synset DatabaseBackedDictionary.internalGetSynsetAt(POS, long , Synset , Query , Query , Query ,	Query ));

	pointcut getSynsetQueryHandler() : call(* DatabaseManager.getSynsetQuery(POS , long)) && withincode(private Synset DatabaseBackedDictionary.internalGetSynsetAt(POS, long , Synset , Query , Query , Query , Query));

	pointcut getSynsetWordQueryHandler() : call(* DatabaseManager.getSynsetWordQuery(POS , long)) && withincode(private Synset DatabaseBackedDictionary.internalGetSynsetAt(POS, long , Synset , Query , Query , Query , Query));

	pointcut getPointerQueryHandler() : call(* DatabaseManager.getPointerQuery(POS , long)) && withincode(private Synset DatabaseBackedDictionary.internalGetSynsetAt(POS, long , Synset , Query , Query , Query , Query));

	pointcut getVerbFrameQueryHandler() : call(* DatabaseManager.getVerbFrameQuery(POS , long)) && withincode(private Synset DatabaseBackedDictionary.internalGetSynsetAt(POS, long , Synset , Query , Query , Query , Query));

	pointcut internalGetExceptionHandler(): execution(private Exc DatabaseBackedDictionary.internalGetException(POS , String , Exc , Query));

	pointcut getExceptionQueryHandler() : call(* DatabaseManager.getExceptionQuery(POS , String)) && withincode(private Exc DatabaseBackedDictionary.internalGetException(POS , String , Exc , Query));

	pointcut internalHasNextHandler(): execution(private void DatabaseBackedDictionary.DatabaseElementIterator.internalHasNext());

	pointcut internalNextHandler(): execution(private Object DatabaseBackedDictionary.DatabaseElementIterator.internalNext());

	pointcut internalGetIndexWordHandler2(): execution(private IndexWord FileBackedDictionary.internalGetIndexWord(String, POS , IndexWord));

	pointcut getSynsetIteratorHandler(): execution(private Object FileBackedDictionary.internal(POS , long , String));

	pointcut internalGetSynsetHandler(): execution(private Synset FileBackedDictionary.internalGetSynset(long , POS ,  String ,	POSKey, Synset));	

	pointcut internalNextLineHandlerSoft(): execution(protected final void FileBackedDictionary.FileLookaheadIterator.nextLine());

	pointcut getNextOffsetHandler(): execution(protected long FileBackedDictionary.FileLookaheadIterator.getNextOffset(long) );

	pointcut getNextOffsetHandler2(): execution(protected long FileBackedDictionary.SubstringIndexFileLookaheadIterator.getNextOffset(long));

	pointcut internalInstallHandler(): execution(private Class MapBackedDictionary.internalInstall(Param , Class ));

	pointcut internalMapBackedDictionaryHandler(): execution(private void MapBackedDictionary.internalMapBackedDictionary(DictionaryCatalogSet));

	pointcut loadDictFileHandler(): execution(private Map MapBackedDictionary.loadDictFile(DictionaryFile));

	declare soft: SQLException :  internalGetIndexWordHandler() || internalGetRandomIndexWordHandler() || internalGetSynsetAtHandler() || internalGetExceptionHandler() || internalHasNextHandler();
	declare soft: Exception : internalNextHandler()|| internalInstallHandler() || internalMapBackedDictionaryHandler() || loadDictFileHandler();
	declare soft: IOException : internalGetIndexWordHandler2() || getRandomIndexWordHandler() ||internalGetSynsetHandler()||internalGetExceptionHandler2()|| getNextOffsetHandler() || getNextOffsetHandler2();
	declare soft: JWNLException : getSynsetIteratorHandler();

	Map around(DictionaryFile file) throws JWNLException  : loadDictFileHandler()&& args(file) {
		try {
			return proceed(file);
		} catch (Exception ex) {
			throw new JWNLException("DICTIONARY_EXCEPTION_020", file.getFile(),
					ex);
		}
	}

	void around() throws JWNLException : internalMapBackedDictionaryHandler() {
		try {
			proceed();
		} catch (Exception ex) {
			throw new JWNLException("DICTIONARY_EXCEPTION_019", ex);
		}
	}

	Class around(Param param) throws JWNLException  : internalInstallHandler() && args(param,*)	{
		try {
			return proceed(param);
		} catch (Exception ex) {
			throw new JWNLException("DICTIONARY_EXCEPTION_003", param
					.getValue(), ex);
		}
	}

	long around(FileBackedDictionary.SubstringIndexFileLookaheadIterator file)
			throws JWNLException : ( getNextOffsetHandler2() || (getNextOffsetHandler() && !getNextOffsetHandler2() ) ) && this(file) {
		try {
			return proceed(file);
		} catch (IOException ex) {
			throw new JWNLException("DICTIONARY_EXCEPTION_008", new Object[] {
					file._pos, file._fileType }, ex);
		}
	}

	void around(): internalNextLineHandlerSoft(){
		try {
			proceed();
		} catch (SoftException e) {
			// return
		}
	}
	

	Object around() throws JWNLException  : internalGetExceptionHandler2() || getRandomIndexWordHandler() {
		try {
			return proceed();
		} catch (IOException ex) {
			throw new JWNLException(this.msg[thisEnclosingJoinPointStaticPart
					.getId()], ex);
		}
	}

	Synset around(long offset) throws JWNLException  : internalGetSynsetHandler() && args(offset, ..) {
		try {
			return proceed(offset);
		} catch (IOException e) {
			throw new JWNLException("DICTIONARY_EXCEPTION_005",
					new Long(offset), e);
		}
	}

	Object around(): getSynsetIteratorHandler(){
		try {
			return proceed();
		} catch (JWNLException ex) {
			throw new RuntimeException(ex);
		}
	}

	IndexWord around(String lemma) throws JWNLException :internalGetIndexWordHandler2() && args(lemma,..) {
		try {
			return proceed(lemma);
		} catch (IOException e) {
			throw new JWNLException("DICTIONARY_EXCEPTION_004", lemma, e);
		}
	}

	Object around(): internalNextHandler() {
		try {
			return proceed();
		} catch (Exception e) {
			return null;
		}
	}

	void around(DatabaseBackedDictionary.DatabaseElementIterator ite): internalHasNextHandler() && this(ite) {
		try {
			proceed(ite);
		} catch (SQLException e) {
			ite._hasNext = false;
		}
	}

	Object around() throws JWNLException  : internalGetExceptionHandler() || internalGetIndexWordHandler() {
		try {
			return proceed();
		} catch (SQLException e) {
			throw new JWNLException("DICTIONARY_EXCEPTION_023", e);
		} finally {
			Query query = (Query) this.queryMap.get(Thread.currentThread()
					.getName());
			query.close();
		}
	}

	Query around(): getExceptionQueryHandler() || getIndexWordSynsetsQueryHandler()|| getSynsetQueryHandler()  {
		Query query = proceed();
		this.queryMap.put(Thread.currentThread().getName(), query);
		return query;
	}

	Synset around() throws JWNLException : internalGetSynsetAtHandler() {
		try {
			return proceed();
		} catch (SQLException e) {
			throw new JWNLException("DICTIONARY_EXCEPTION_023", e);
		} finally {
			Query query = (Query) this.queryMap.get(Thread.currentThread()
					.getName());
			Query wordQuery = (Query) this.wordQueryMap.get(Thread
					.currentThread().getName());
			Query pointerQuery = (Query) this.pointerQueryMap.get(Thread
					.currentThread().getName());
			Query verbFrameQuery = (Query) this.verbFrameQueryMap.get(Thread
					.currentThread().getName());

			query.close();
			wordQuery.close();
			pointerQuery.close();
			verbFrameQuery.close();
		}
	}

	Query around(): getSynsetWordQueryHandler(){
		Query query = proceed();
		this.wordQueryMap.put(Thread.currentThread().getName(), query);
		return query;
	}

	Query around(): getPointerQueryHandler(){
		Query query = proceed();
		this.pointerQueryMap.put(Thread.currentThread().getName(), query);
		return query;
	}

	Query around(): getVerbFrameQueryHandler(){
		Query query = proceed();
		this.verbFrameQueryMap.put(Thread.currentThread().getName(), query);
		return query;
	}

	String around(Query query) throws JWNLException : internalGetRandomIndexWordHandler() && args(query ,*) {
		try {
			return proceed(query);
		} catch (SQLException ex) {
			throw new JWNLException("DICTIONARY_EXCEPTION_023", ex);
		} finally {
			query.close();
		}
	}

}
