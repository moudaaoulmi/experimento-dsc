package ish.ecletex.wordnet.jwnl.princeton.data;


import ish.ecletex.wordnet.jwnl.data.Synset;
import ish.ecletex.wordnet.jwnl.data.Verb;

import java.util.BitSet;

/**
 * Wrapper for a verb that allows the VerbFrame flags to be set after the Verb
 * is created.
 */
class MutableVerb extends Verb {
	public MutableVerb(Synset synset, int index, String lemma) {
		super(synset, index, lemma, new BitSet());
	}

	public void setVerbFrameFlag(int fnum) {
		getVerbFrameFlags().set(fnum);
	}
}
