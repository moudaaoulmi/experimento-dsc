package ish.ecletex.wordnet.jwnl.princeton.data;


import ish.ecletex.wordnet.jwnl.JWNLException;

import java.util.Map;

public class PrincetonWN17DatabaseDictionaryElementFactory extends AbstractPrincetonDatabaseDictionaryElementFactory {
	public Object create(Map params) throws JWNLException {
		return new PrincetonWN17DatabaseDictionaryElementFactory();
	}
}
