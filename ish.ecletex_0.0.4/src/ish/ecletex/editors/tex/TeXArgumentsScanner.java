/*
 * Created on Apr 27, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex.editors.tex;

import ish.ecletex.preferences.TeXPreferencePage;
import ish.ecletex.preferences.TexColorsPreferencePage;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;

/**
 * @author ish
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TeXArgumentsScanner extends RuleBasedScanner {
	public TeXArgumentsScanner(ColorManager manager,TeXEditor editor) {
		IToken numberToken =
			new Token(
				new TextAttribute(
						manager.getColor(TexColorsPreferencePage.ARGUMENT_COLOR)));
		IToken commandToken =
			new Token(
				new TextAttribute(
						manager.getColor(TexColorsPreferencePage.COMMAND_COLOR)));
		
		IRule[] rules = new IRule[3];
		rules[0] = new WordRule(new TeXWords(), commandToken);
		rules[1] = new WordRule(new NumberWord(), numberToken);
		rules[2] = new SpellingRule(manager,editor,this);
		setRules(rules);
		setDefaultReturnToken(
				new Token(
					new TextAttribute(
							manager.getColor(TexColorsPreferencePage.ARGUMENT_COLOR))));
	}
}
