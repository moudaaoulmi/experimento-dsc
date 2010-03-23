/*
 * Created on 16-Jul-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ish.ecletex;

import java.io.InputStream;
import java.util.HashMap;

import org.eclipse.core.runtime.Path;

import ish.ecletex.wordnet.jwnl.JWNL;
import ish.ecletex.wordnet.jwnl.JWNLException;
import ish.ecletex.wordnet.jwnl.data.IndexWord;
import ish.ecletex.wordnet.jwnl.data.POS;
import ish.ecletex.wordnet.jwnl.data.Synset;
import ish.ecletex.wordnet.jwnl.data.Word;
import ish.ecletex.wordnet.jwnl.dictionary.Dictionary;

/**
 * @author ish
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class WordNetProvider {

	private static void init() {
		if (!JWNL.isInitialized()) {
			internalInit();
		}
	}

	private static void internalInit() {
		System.out.println("Initalising WordNet.");
		// JWNL.initialize();

		InputStream is = ecletexPlugin.getDefault().openStream(
				new Path("wordnet/properties.xml"));
		JWNL.initialize(is);

		System.out.println("Done.");
	}

	public static String getWord(String word) {
		init();
		if (!JWNL.isInitialized()) {
			return "<p><b>" + word
					+ "</b></p><p>WordNet 2.0 data is not avaliable.</p>";
		}
		return internalGetWord(word);
	}

	private static String internalGetWord(String word) {
		// TODO : polysemies needed?
		// int[] polysemies = new int[4];
		Dictionary d = Dictionary.getInstance();

		IndexWord noun_form = d.getIndexWord(POS.NOUN, word);
		if (noun_form == null)
			noun_form = d.getMorphologicalProcessor().lookupBaseForm(POS.NOUN,
					word);
		IndexWord verb_form = d.getIndexWord(POS.VERB, word);
		if (verb_form == null)
			verb_form = d.getMorphologicalProcessor().lookupBaseForm(POS.VERB,
					word);
		IndexWord adj_form = d.getIndexWord(POS.ADJECTIVE, word);
		if (adj_form == null)
			adj_form = d.getMorphologicalProcessor().lookupBaseForm(
					POS.ADJECTIVE, word);
		IndexWord adv_form = d.getIndexWord(POS.ADVERB, word);
		if (adv_form == null)
			adv_form = d.getMorphologicalProcessor().lookupBaseForm(POS.ADVERB,
					word);

		if (noun_form == null && verb_form == null && adj_form == null
				&& adv_form == null) {
			return "<p><b>" + word + "</b></p><p>Not found.</p>";
		}

		String output = "<p><b>" + word + "</b></p>";
		if (noun_form != null) {
			if (noun_form.getSenseCount() > 0) {
				output += "<p><b>" + noun_form.getPOS().getLabel() + "</b></p>";
				for (int i = 1; i <= noun_form.getSenseCount(); i++) {
					Synset sense = noun_form.getSense(i);
					output += "<li bindent='10'>" + sense.getGloss() + "</li>";
					output += "<li style='text' value='' bindent='20'>";
					Word[] words = sense.getWords();
					HashMap usedWords = new HashMap();
					for (int j = 0; j < words.length - 1; j++) {
						if (!usedWords.containsKey(words[j].getLemma())) {
							output += words[j].getLemma() + ", ";
							usedWords.put(words[j].getLemma(), new Object());
						}
					}
					if (!usedWords.containsKey(words[words.length - 1]
							.getLemma())) {
						output += words[words.length - 1].getLemma() + "</li>";
					} else {
						output += "</li>";
					}

				}
			}

		}

		if (verb_form != null) {
			if (verb_form.getSenseCount() > 0) {
				output += "<p><b>" + verb_form.getPOS().getLabel() + "</b></p>";
				for (int i = 1; i <= verb_form.getSenseCount(); i++) {
					Synset sense = verb_form.getSense(i);
					output += "<li bindent='10'>" + sense.getGloss() + "</li>";
					output += "<li style='text' value='' bindent='20'>";
					Word[] words = sense.getWords();
					HashMap usedWords = new HashMap();
					for (int j = 0; j < words.length - 1; j++) {
						if (!usedWords.containsKey(words[j].getLemma())) {
							output += words[j].getLemma() + ", ";
							usedWords.put(words[j].getLemma(), new Object());
						}
					}
					if (!usedWords.containsKey(words[words.length - 1]
							.getLemma())) {
						output += words[words.length - 1].getLemma() + "</li>";
					} else {
						output += "</li>";
					}

				}
			}

		}

		if (adj_form != null) {
			if (adj_form.getSenseCount() > 0) {
				output += "<p><b>" + adj_form.getPOS().getLabel() + "</b></p>";
				for (int i = 1; i <= adj_form.getSenseCount(); i++) {
					Synset sense = adj_form.getSense(i);
					output += "<li bindent='10'>" + sense.getGloss() + "</li>";
					output += "<li style='text' value='' bindent='20'>";
					Word[] words = sense.getWords();
					HashMap usedWords = new HashMap();
					for (int j = 0; j < words.length - 1; j++) {
						if (!usedWords.containsKey(words[j].getLemma())) {
							output += words[j].getLemma() + ", ";
							usedWords.put(words[j].getLemma(), new Object());
						}
					}
					if (!usedWords.containsKey(words[words.length - 1]
							.getLemma())) {
						output += words[words.length - 1].getLemma() + "</li>";
					} else {
						output += "</li>";
					}

				}
			}

		}

		if (adv_form != null) {
			if (adv_form.getSenseCount() > 0) {
				output += "<p><b>" + adv_form.getPOS().getLabel() + "</b></p>";
				for (int i = 1; i <= adv_form.getSenseCount(); i++) {
					Synset sense = adv_form.getSense(i);
					output += "<li bindent='10'>" + sense.getGloss() + "</li>";
					output += "<li style='text' value='' bindent='20'>";
					Word[] words = sense.getWords();
					HashMap usedWords = new HashMap();
					for (int j = 0; j < words.length - 1; j++) {
						if (!usedWords.containsKey(words[j].getLemma())) {
							output += words[j].getLemma() + ", ";
							usedWords.put(words[j].getLemma(), new Object());
						}
					}
					if (!usedWords.containsKey(words[words.length - 1]
							.getLemma())) {
						output += words[words.length - 1].getLemma() + "</li>";
					} else {
						output += "</li>";
					}

				}
			}

		}

		return output;
	}

}
