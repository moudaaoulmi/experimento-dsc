package ish.ecletex.editors.tex;

import org.eclipse.jface.text.rules.*;

public class TeXPartitionScanner extends RuleBasedPartitionScanner {
	public static final String TEX_MATH = "mathParitition"; //$NON-NLS-1$
	public static final String TEX_CURLY_BRACKETS = "curlyBracketPartition";
	public static final String TEX_SQUARE_BRACKETS = "squareBracketPartition";
	public static final String TEX_COMMENTS = "commentsPartition";
	
	
	/**
	 * Creates the partitioner and sets up the appropriate rules.
	 */
	public TeXPartitionScanner() {
		super();

		IToken math = new Token(TEX_MATH);
		IToken curly_bracket = new Token(TEX_CURLY_BRACKETS);
		IToken square_bracket= new Token(TEX_SQUARE_BRACKETS);
		IToken comments = new Token(TEX_COMMENTS);

		IPredicateRule[] rules = new IPredicateRule[9]; //Stephan 27.05.2005
		
		rules[0] = new SingleLineRule("\\%"," ", Token.UNDEFINED); //no comment when using "\%" in LaTeX 
		rules[1] = new EndOfLineRule("%", comments); 
		rules[2] = new SingleLineRule("\\\\[","]", Token.UNDEFINED);  //no math when using "\\[]" line breaks
		rules[3] = new MultiLineRule("\\[","\\]", math); 
		rules[4] = new MultiLineRule("\\begin{equation}","\\end{equation}", math); 
		rules[5] = new MultiLineRule("\\begin{equation*}","\\end{equation*}", math); 
		rules[6] = new MultiLineRule("$","$", math); 
		rules[7] = new BracketRule('{','}', curly_bracket);
		rules[8] = new BracketRule('[',']', square_bracket);
		
		/*rules[0] = new MultiLineRule("\\[","\\]",math);
		rules[1] = new MultiLineRule("\\begin{equation}","\\end{equation}", math); // Stephan Dlugosz 19.11.2003
		rules[2] = new MultiLineRule("\\begin{equation*}","\\end{equation*}", math); // Stephan Dlugosz 19.11.2003
		rules[3] = new SingleLineRule("$","$", math); // Stephan Dlugosz 19.11.2003
		rules[4] = new BracketRule('{','}', curly_bracket);
		rules[5] = new BracketRule('[',']', square_bracket);
		rules[6] = new EndOfLineRule("%", comments);*/
		setPredicateRules(rules);
	}

}
