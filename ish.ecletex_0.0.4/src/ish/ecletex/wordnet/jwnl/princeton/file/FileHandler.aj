package ish.ecletex.wordnet.jwnl.princeton.file;

import java.io.IOException;

public privileged aspect FileHandler {

	pointcut closeHandler() : execution(public void PrincetonChannelDictionaryFile.close());

	pointcut closeHandler1() : execution(public void PrincetonObjectDictionaryFile.close());
	
	pointcut closeHandler2() : execution(public void PrincetonRandomAccessDictionaryFile.close());

	declare soft: IOException  : closeHandler();
	declare soft: Exception  : closeHandler1() || closeHandler2();
	
	void around(PrincetonRandomAccessDictionaryFile princ): closeHandler2() && this(princ){
		try {
			proceed(princ);
		} catch (Exception ex) {
		} finally {
			princ._file = null;
		}
	}

	void around(PrincetonObjectDictionaryFile princ): closeHandler1() && this(princ) {
		try {
			proceed(princ);
		} catch (Exception ex) {
		} finally {
			princ._in = null;
			princ._out = null;
			princ._file = null;
		}
	}

	void around(PrincetonChannelDictionaryFile princ): closeHandler() && this(princ){
		try {
			proceed(princ);
		} catch (IOException ex) {
		} finally {
			princ._channel = null;
		}
	}

}
