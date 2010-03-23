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
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;

/**
 * @author ish
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TeXCommentScanner extends RuleBasedScanner{
	
	public TeXCommentScanner(ColorManager manager,TeXEditor editor) {
		
		IRule[] rules = new IRule[1];
		rules[0] = new SpellingRule(manager,editor,this);
		setRules(rules);
		
		setDefaultReturnToken(
				new Token(
					new TextAttribute(
							manager.getColor(TexColorsPreferencePage.COMMENT_COLOR))));
	}
}
