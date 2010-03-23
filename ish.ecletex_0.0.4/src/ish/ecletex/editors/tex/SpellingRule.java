/*
 * Created on 05-May-2004
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex.editors.tex;

import ish.ecletex.ecletexPlugin;
import ish.ecletex.editors.tex.spelling.GlobalDictionary;
import ish.ecletex.preferences.TeXPreferencePage;
import ish.ecletex.preferences.TexColorsPreferencePage;
import ish.ecletex.properties.texFileProperties;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;

/**
 * @author ish
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Generation - Code and Comments
 */
public class SpellingRule implements IRule {
	private IWordDetector words;

	private ColorManager colorManager;

	private String language;

	private RuleBasedScanner ParentScanner;

	private TeXEditor editor;

	private String languageStartTag = "%language:";

	private String languageEndTag = "%language:end";

	private boolean FoundLanguageTag = false;

	private boolean InAlternateLanguage = false;

	private String AlternateLanguage;

	public SpellingRule(ColorManager colorManager, TeXEditor editor,
			RuleBasedScanner parent) {
		words = new TextWords();
		this.colorManager = colorManager;
		this.editor = editor;
		this.ParentScanner = parent;
	}

	public String GetLanguage() {
		int position = ParentScanner.getTokenOffset();
		IDocument doc = editor.getDocumentProvider().getDocument(
				editor.getEditorInput());
		int current_position = position;

		String Text = doc.get(0, position);
		int end = Text.lastIndexOf(languageEndTag);
		int start = Text.lastIndexOf(languageStartTag);

		if (start == end)
			return "default";

		if (start > end) {
			String lang = "";
			for (int j = start + languageStartTag.length(); j < start
					+ languageStartTag.length() + 255; j++) {
				char c = doc.getChar(j);
				if (words.isWordPart(c)) {
					lang += c;
				} else {
					return lang;
				}
			}
		}
		return "default";

	}

	public IToken evaluate(ICharacterScanner scanner) {
		if (!ecletexPlugin.getDefault().getPreferenceStore().getBoolean(
				TeXPreferencePage.SPELL_CHECK)) {
			return Token.UNDEFINED;
		} else {
			char start = (char) scanner.read();
			if (words.isWordStart(start)) {
				String word = "" + start;
				char currentChar;
				while (words.isWordPart(currentChar = (char) scanner.read())) {
					word += currentChar;
				}
				scanner.unread();
				return internalEvaluate(word);

			} else {
				scanner.unread();
				return Token.UNDEFINED;
			}
		}
	}

	private IToken internalEvaluate(String word) {
		if (editor.getFile().getPersistentProperty(
				new QualifiedName("ish.ecletex",
						texFileProperties.ALTERNATE_SUPPORT)).equals(
				texFileProperties.CHECKED)) {
			String language = GetLanguage();

			if (language == "default") {
				if (GlobalDictionary.isCorrect(word, editor)) {
					// return new Token(new
					// TextAttribute(colorManager.getColor(TeXPreferencePage.TEXT_COLOR)));
					return Token.UNDEFINED;
				} else {
					return new Token(new TextAttribute(colorManager
							.getColor(TexColorsPreferencePage.INCORRECT_COLOR)));
				}
			} else {
				if (GlobalDictionary.isCorrect(word, language)) {
					// return new Token(new
					// TextAttribute(colorManager.getColor(TeXPreferencePage.TEXT_COLOR)));
					return Token.UNDEFINED;
				} else {
					return new Token(new TextAttribute(colorManager
							.getColor(TexColorsPreferencePage.INCORRECT_COLOR)));
				}
			}
		} else {
			if (GlobalDictionary.isCorrect(word, editor)) {
				// return new Token(new
				// TextAttribute(colorManager.getColor(TeXPreferencePage.TEXT_COLOR)));
				return Token.UNDEFINED;
			} else {
				return new Token(new TextAttribute(colorManager
						.getColor(TexColorsPreferencePage.INCORRECT_COLOR)));
			}

		}
	}
}