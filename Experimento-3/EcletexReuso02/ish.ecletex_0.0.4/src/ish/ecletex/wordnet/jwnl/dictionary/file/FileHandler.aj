package ish.ecletex.wordnet.jwnl.dictionary.file;

import ish.ecletex.wordnet.jwnl.JWNLRuntimeException;

public privileged aspect FileHandler {

	pointcut dictionaryCatalogHandler(): execution(DictionaryCatalog.new(String , DictionaryFileType , Class ) );

	declare soft: Exception: dictionaryCatalogHandler();

	void around(DictionaryFileType fileType, Class dictionaryFileType): dictionaryCatalogHandler() && args(*,  fileType, dictionaryFileType) {
		try {
			proceed(fileType, dictionaryFileType);
		} catch (Exception ex) {
			throw new JWNLRuntimeException("DICTIONARY_EXCEPTION_0018",
					new Object[] { fileType, dictionaryFileType }, ex);
		}
	}

}
