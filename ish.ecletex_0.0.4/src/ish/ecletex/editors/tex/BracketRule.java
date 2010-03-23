/*
 * Created on Apr 27, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex.editors.tex;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * @author ish
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class BracketRule implements IPredicateRule {
	
	public BracketRule(char startBracket,char endBracket,IToken returnToken){
		this.fStartChar = startBracket;
		this.fEndChar = endBracket;
		this.fToken = returnToken;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IPredicateRule#getSuccessToken()
	 */
	public IToken getSuccessToken() {
		// TODO Auto-generated method stub
		return fToken;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IPredicateRule#evaluate(org.eclipse.jface.text.rules.ICharacterScanner, boolean)
	 */
	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		if(resume){
			if (endBracketDetected(scanner)){
				return fToken;
			}else{
				return Token.UNDEFINED;
			}
				
		}else{
			return evaluate(scanner);
		}
		
		

	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IRule#evaluate(org.eclipse.jface.text.rules.ICharacterScanner)
	 */
	
	private char fStartChar;
	private char fEndChar;
	IToken fToken;
	

	
	public IToken evaluate(ICharacterScanner scanner) {
		
		int c= scanner.read();
		if(c == fStartChar){
			if (endBracketDetected(scanner))
				return fToken;
		}
		scanner.unread();
		return Token.UNDEFINED;
	}
	
	private void unread(int count,ICharacterScanner scanner){
		for(int i=0;i<count;i++)
			scanner.unread();
	}
	
	private boolean endBracketDetected(ICharacterScanner scanner){
		int c;
		int pairs = 0;
		int read = 0;
		while ((c= scanner.read()) != ICharacterScanner.EOF) {
			read++;
			if(c == fEndChar){
				if(pairs == 0){
					return true;
				}else{
					pairs--;
				}
			}else if(c == fStartChar){
				pairs++;
			}
		}
		unread(read,scanner);
		return false;
		
	}
}
