package ish.ecletex.editors.tex.spelling;

import ish.ecletex.editors.tex.spelling.engine.SpellDictionary;
import ish.ecletex.properties.texFileProperties;
import java.io.IOException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

public privileged aspect SpellingHandler {
	
	String[] msg = {"User ","Main "};
	
	pointcut internalLoadLanguageHandler1(): execution(private SpellDictionary GlobalDictionary.internalLoadLanguage(String,SpellDictionary));

	pointcut internalLoadLanguageHandler2(): execution(private SpellDictionary GlobalDictionary.internalLoadLanguage(String,String, SpellDictionary));

	
	pointcut internalLoadLanguageHandler(): execution(private void GlobalDictionary.internalLoadLanguage(String,SpellDictionary, SpellDictionary));
	
	pointcut internalLoadLanguageHandler3(): execution(private String GlobalDictionary.internalLoadLanguage(IFile, String));

	pointcut internalLoadLanguage1Handler(): execution(private SpellDictionary GlobalDictionary.internalLoadLanguage1(String,SpellDictionary) );

	// Declare Soft

	declare soft: Exception :internalLoadLanguageHandler();
	declare soft: IOException :internalLoadLanguageHandler1() || internalLoadLanguageHandler2() || internalLoadLanguage1Handler();
	declare soft: CoreException :internalLoadLanguageHandler3();

	// Advices

	SpellDictionary around(SpellDictionary dictionary): internalLoadLanguage1Handler() && args(*,dictionary) {
		try {
			return proceed(dictionary);
		} catch (IOException ex) {

		}
		return dictionary;
	}

	void around(String language): internalLoadLanguageHandler() && args(language, ..) {
		try {
			proceed(language);
		} catch (Exception ex) {
			System.out.println("Failed To Load Dictionary for: " + language);
		}
	}

	SpellDictionary around(String language, SpellDictionary userdictionary): (internalLoadLanguageHandler1() || internalLoadLanguageHandler2()) && args(language,..,userdictionary) {
		try {
			return proceed(language, userdictionary);
		} catch (IOException ex) {
			System.out.println(this.msg[thisEnclosingJoinPointStaticPart.getId()] + language + " dictionary, load failed");
		}
		return userdictionary;
	}	

	String around(String language): internalLoadLanguageHandler3() && args(*,language) {
		try {
			return proceed(language);
		} catch (CoreException ex) {
			language = texFileProperties.DEFAULT_DICTIONARY;
		}
		return language;
	}

}
