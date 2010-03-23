package ish.ecletex.wordnet.jwnl.dictionary.morph;


import ish.ecletex.wordnet.jwnl.JWNLException;
import ish.ecletex.wordnet.jwnl.data.POS;
import ish.ecletex.wordnet.jwnl.dictionary.Dictionary;

import java.util.Map;

public class LookupIndexWordOperation implements Operation {
	public Object create(Map params) throws JWNLException {
		return new LookupIndexWordOperation();
	}

	public boolean execute(POS pos, String lemma, BaseFormSet baseForms) throws JWNLException {
		if (Dictionary.getInstance().getIndexWord(pos, lemma) != null) {
			baseForms.add(lemma);
			return true;
		}
		return false;
	}
}