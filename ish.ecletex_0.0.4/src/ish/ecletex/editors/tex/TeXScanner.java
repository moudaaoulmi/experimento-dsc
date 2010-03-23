package ish.ecletex.editors.tex;

import ish.ecletex.preferences.TeXPreferencePage;
import ish.ecletex.preferences.TexColorsPreferencePage;

import org.eclipse.jface.text.rules.*;
import org.eclipse.jface.text.*;

public class TeXScanner extends RuleBasedScanner {

	public TeXScanner(ColorManager manager,TeXEditor editor) {
		IToken numberToken =
			new Token(
				new TextAttribute(
						manager.getColor(TexColorsPreferencePage.TEXT_COLOR)));
		IToken commandToken =
			new Token(
				new TextAttribute(
						manager.getColor(TexColorsPreferencePage.COMMAND_COLOR)));
		
		IToken bibtexToken =
			new Token(
				new TextAttribute(
						manager.getColor(TexColorsPreferencePage.BIBTEX_ENTRY_COLOR)));
		
		IRule[] rules = new IRule[4];
		rules[0] = new WordRule(new TeXWords(), commandToken);
		rules[1] = new WordRule(new BibTeXWords(),bibtexToken);
		//rules[2] = new WordRule(new NumberWord(), numberToken);
		rules[2] = new NumberRule(numberToken); // Stephan 27.5.2004

		rules[3] = new SpellingRule(manager,editor,this);
		setRules(rules);
		setDefaultReturnToken(
				new Token(
					new TextAttribute(
							manager.getColor(TexColorsPreferencePage.TEXT_COLOR))));
	}
}
