package ish.ecletex.wordnet.jwnl.dictionary.file_manager;

import ish.ecletex.wordnet.jwnl.JWNLException;
import ish.ecletex.wordnet.jwnl.JWNLRuntimeException;
import java.io.IOException;
import java.util.Map;

public privileged aspect File_ManagerHandler {

	pointcut internalCreateHandler(): execution(private Object FileManagerImpl.internalCreate(Class, String ));

	pointcut internalCreateHandler2(): execution(private Class FileManagerImpl.internalCreate(Map , Class ));

	declare soft: IOException: internalCreateHandler() ;
	
	declare soft: ClassNotFoundException:  internalCreateHandler2() ;

	Object around(Class fileClass) throws JWNLException: internalCreateHandler() && args(fileClass,*) {
		try {
			return proceed(fileClass);
		} catch (IOException ex) {
			throw new JWNLException("DICTIONARY_EXCEPTION_016", fileClass, ex);
		}
	}

	Class around() throws JWNLRuntimeException : internalCreateHandler2() {
		try {
			return proceed();
		} catch (ClassNotFoundException ex) {
			throw new JWNLRuntimeException("DICTIONARY_EXCEPTION_002", ex);
		}
	}

}
