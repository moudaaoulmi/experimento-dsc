package ish.ecletex.wordnet.jwnl.dictionary.morph;

import ish.ecletex.wordnet.jwnl.JWNLException;
import ish.ecletex.wordnet.jwnl.data.POS;
import ish.ecletex.wordnet.jwnl.util.factory.Createable;

public interface Operation extends Createable {
	/**
	 * Execute the operation.
	 * @param pos
	 * @param lemma
	 * @param baseForms BaseFormSet to which all discovered base forms should
	 *        be added.
	 * @return true if at least one base form was discovered by the operation and
	 *         added to <var>baseForms</var>.
	 * @throws JWNLException
	 */
	boolean execute(POS pos, String lemma, BaseFormSet baseForms) throws JWNLException;
}