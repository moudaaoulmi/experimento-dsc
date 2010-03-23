/*
 * Created on 05-May-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

package ish.ecletex.editors.tex.spelling;

import ish.ecletex.ecletexPlugin;
import ish.ecletex.editors.tex.TeXEditor;
import ish.ecletex.editors.tex.spelling.engine.SpellDictionary;
import ish.ecletex.editors.tex.spelling.engine.SpellDictionaryHashMap;
import ish.ecletex.editors.tex.spelling.engine.Word;
import ish.ecletex.editors.tex.spelling.event.SpellChecker;
import ish.ecletex.properties.ecletexProjectProperties;
import ish.ecletex.properties.texFileProperties;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;

/**
 * @author ish
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class GlobalDictionary {
	private static HashMap SpellCheckers;
	private static HashMap UserDictionaries;
	private static Object lockObject = new Object();

	public static boolean isCorrect(String word, String language) {
		// checkInit();

		SpellChecker checker = getSpellChecker(language);

		if (checker == null) {
			return true;
		} else {
			return checker.isCorrect(word);
		}
	}

	public static void addWord(String word, TeXEditor editor) {
		SpellDictionary dict = getUserDictionary(editor);
		dict.addWord(word);
		System.out.println("Added: " + word + " to user dictionary.");
	}

	public static boolean isCorrect(String word, TeXEditor editor) {
		// checkInit();

		SpellChecker checker = getSpellChecker(editor);

		if (checker == null) {
			return true;
		} else {
			return checker.isCorrect(word);
		}
	}

	public static String[] getSuggestions(String word, String language) {
		// checkInit();

		SpellChecker checker = getSpellChecker(language);

		if (checker == null) {
			return new String[0];
		} else {
			List l = checker.checkSpelling(word);
			String[] suggestions = new String[l.size()];
			Word[] words = new Word[l.size()];
			l.toArray(words);
			for (int i = 0; i < words.length; i++)
				suggestions[i] = words[i].getWord();
			return suggestions;
		}
	}

	public static String[] getSuggestions(String word, TeXEditor editor) {
		// checkInit();

		SpellChecker checker = getSpellChecker(editor);

		if (checker == null) {
			return new String[0];
		} else {
			List l = checker.checkSpelling(word);
			String[] suggestions = new String[l.size()];
			Word[] words = new Word[l.size()];
			l.toArray(words);
			for (int i = 0; i < words.length; i++)
				suggestions[i] = words[i].getWord();
			return suggestions;
		}
	}

	public static void LoadLanguage(IFile file) {

		if (file == null)
			return;

		if (SpellCheckers == null)
			SpellCheckers = new HashMap();
		if (UserDictionaries == null)
			UserDictionaries = new HashMap();

		String language = "";
		language = internalLoadLanguage(file, language);

		if (SpellCheckers.containsKey(language))
			return;

		System.out.println("Loading dictionary for: " + language);
		String dictionaryPath = "dictionary/" + language + "/all.words";
		// TODO : userdictionaryPath needed?
		// String userdictionaryPath = "dictionary/" + language +
		// "/users.words";
		SpellDictionary dictionary = null;
		SpellDictionary userdictionary = null;
		dictionary = internalLoadLanguage(language, dictionaryPath, dictionary);
		userdictionary = internalLoadLanguage(language, userdictionary);
		internalLoadLanguage(language, dictionary, userdictionary);
	}

	private static void internalLoadLanguage(String language,
			SpellDictionary dictionary, SpellDictionary userdictionary) {
		SpellChecker newChecker = new SpellChecker(dictionary);
		if (userdictionary != null) {
			newChecker.setUserDictionary(userdictionary);
			UserDictionaries.put(language, userdictionary);
		}
		SpellCheckers.put(language, newChecker);
	}

	private static SpellDictionary internalLoadLanguage(String language,
			SpellDictionary userdictionary) {
		IPath stateLocation = ecletexPlugin.getDefault().getStateLocation();
		IPath userdicpath = stateLocation.append(language + "/users.words");
		File dictfile = new File(userdicpath.toString());
		System.out.println("Loading User Dictionary: " + dictfile.toString());
		if (!dictfile.exists()) {
			dictfile.getParentFile().mkdirs();
			dictfile.createNewFile();
		}
		userdictionary = new SpellDictionaryHashMap(dictfile);

		return userdictionary;
	}

	private static SpellDictionary internalLoadLanguage(String language,
			String dictionaryPath, SpellDictionary dictionary) {
		dictionary = new SpellDictionaryHashMap(
				new InputStreamReader(ecletexPlugin.getDefault().openStream(
						new Path(dictionaryPath))));

		return dictionary;
	}

	private static String internalLoadLanguage(IFile file, String language) {

		language = file.getPersistentProperty(new QualifiedName("ish.ecletex",
				texFileProperties.DICTIONARY_PROPERTY));
		if (language == null)
			language = texFileProperties.DEFAULT_DICTIONARY;

		if (language.equals(""))
			language = texFileProperties.DEFAULT_DICTIONARY;

		return language;
	}

	public static void LoadLanguage(String language) {

		if (SpellCheckers == null)
			SpellCheckers = new HashMap();

		if (UserDictionaries == null)
			UserDictionaries = new HashMap();

		if (SpellCheckers.containsKey(language))
			return;

		System.out.println("Loading dictionary for: " + language);
		String dictionaryPath = "dictionary/" + language + "/all.words";
		// TODO : userdictionaryPath needed?
		// String userdictionaryPath = "dictionary/" + language +
		// "/users.words";
		SpellDictionary dictionary = null;
		SpellDictionary userdictionary = null;
		dictionary = internalLoadLanguage1(dictionaryPath, dictionary);
		userdictionary = internalLoadLanguage(language, userdictionary);
		internalLoadLanguage(language, dictionary, userdictionary);
	}

	private static SpellDictionary internalLoadLanguage1(String dictionaryPath,
			SpellDictionary dictionary) {
		dictionary = new SpellDictionaryHashMap(
				new InputStreamReader(ecletexPlugin.getDefault().openStream(
						new Path(dictionaryPath))));

		return dictionary;
	}

	private static SpellChecker getSpellChecker(TeXEditor editor) {
		// IProject project = ProjectUtils.getCurrentProject();
		IFile file = editor.getFile();
		if (file == null)
			return null;
		if (SpellCheckers == null) {
			LoadLanguage(file);
		}

		String language = "";
		language = internalLoadLanguage(file, language);
		if (!SpellCheckers.containsKey(language)) {
			LoadLanguage(file);
		}

		return (SpellChecker) SpellCheckers.get(language);
	}

	private static SpellDictionary getUserDictionary(TeXEditor editor) {
		// IProject project = ProjectUtils.getCurrentProject();
		IFile file = editor.getFile();
		if (file == null)
			return null;
		if (SpellCheckers == null) {
			LoadLanguage(file);
		}

		String language = "";
		language = internalLoadLanguage(file, language);
		if (!UserDictionaries.containsKey(language)) {
			LoadLanguage(file);
		}
		return (SpellDictionary) UserDictionaries.get(language);
	}

	private static SpellChecker getSpellChecker(String language) {
		if (SpellCheckers == null) {
			LoadLanguage(language);
		}
		if (!SpellCheckers.containsKey(language)) {
			LoadLanguage(language);
		}

		return (SpellChecker) SpellCheckers.get(language);
	}
}