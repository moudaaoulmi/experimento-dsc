package ish.ecletex.editors.tex;

import ish.ecletex.preferences.TeXPreferencePage;
import ish.ecletex.preferences.TexColorsPreferencePage;

import org.eclipse.jface.text.rules.*;
import org.eclipse.jface.text.*;

public class TeXMathScanner extends RuleBasedScanner {

	public TeXMathScanner(ColorManager manager) {
		IToken mathcommandToken =
			new Token(
				new TextAttribute(
						manager.getColor(TexColorsPreferencePage.COMMAND_COLOR), manager.getColor(TexColorsPreferencePage.MATH_COLOR),0));
		IToken mathcommentToken =
			new Token(
				new TextAttribute(
						manager.getColor(TexColorsPreferencePage.COMMENT_COLOR), manager.getColor(TexColorsPreferencePage.MATH_COLOR),0));
		IToken mathelseToken =
			new Token(
				new TextAttribute(
						manager.getColor(TexColorsPreferencePage.TEXT_COLOR), manager.getColor(TexColorsPreferencePage.MATH_COLOR),0));
			
		IRule[] rules = new IRule[3];
		rules[0] = new EndOfLineRule("%", mathcommentToken);
		rules[1] = new WordRule(new TeXWords(), mathcommandToken);
		rules[2] = new WordRule(new TeXElse(), mathelseToken);
		
		setRules(rules);
		
		setDefaultReturnToken(
				new Token(
					new TextAttribute(
							manager.getColor(TexColorsPreferencePage.MATH_COLOR))));

	}
}
