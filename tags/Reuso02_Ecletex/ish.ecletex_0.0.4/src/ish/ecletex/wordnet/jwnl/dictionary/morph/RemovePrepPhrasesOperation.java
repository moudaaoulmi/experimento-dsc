package ish.ecletex.wordnet.jwnl.dictionary.morph;


import ish.ecletex.wordnet.jwnl.JWNLException;
import ish.ecletex.wordnet.jwnl.data.POS;

import java.util.Map;

/** yet to be implemented */
public class RemovePrepPhrasesOperation implements Operation {
	public Object create(Map params) throws JWNLException {
		return new RemovePrepPhrasesOperation();
	}

	public boolean execute(POS pos, String lemma, BaseFormSet baseForm) {
		return false;
	}
}