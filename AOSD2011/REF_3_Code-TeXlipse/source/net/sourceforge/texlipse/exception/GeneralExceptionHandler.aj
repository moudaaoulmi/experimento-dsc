package net.sourceforge.texlipse.exception;

import net.sourceforge.texlipse.TexlipsePlugin;
import net.sourceforge.texlipse.actions.TexSelections;
import net.sourceforge.texlipse.bibparser.lexer.Lexer;
import net.sourceforge.texlipse.bibparser.parser.Parser;

public privileged aspect GeneralExceptionHandler {

	//0
	pointcut getLineHandler(): execution(public String TexSelections.getLine(int));

	//1
	pointcut getCompleteLinesHandler(): execution(public String TexSelections.getCompleteLines());

	//2
	pointcut lexerHandler(): staticinitialization(Lexer);

	//3
	pointcut parserHandler(): staticinitialization(Parser);
	
	
	declare soft: Exception: getLineHandler()  
		|| getCompleteLinesHandler() 
		|| lexerHandler() 
		|| parserHandler() ;
	
	private String getMessageText(int pointcutIndex){
		String textResult = "";
		switch(pointcutIndex){
		
		case 0:
			textResult = "TexSelections.getLine: ";
			break;
		case 1:
			textResult = "TexSelections.getCompleteLines: ";
			break;
		case 2:
			textResult = "The file \"lexer.dat\" is either missing or corrupted.";
			break;
		case 3:
			textResult = "The file \"parser.dat\" is either missing or corrupted.";
			break;
		}
		return textResult;
	}
	
	//XXX rethrow
	void around(): lexerHandler()||parserHandler(){
		try {
			proceed();
		} catch (Exception e) {
			String logText = getMessageText(thisEnclosingJoinPointStaticPart.getId());
			throw new RuntimeException(logText);
		}
	}
	
	String around(): getLineHandler()||getCompleteLinesHandler() {
		try {
			return proceed();
		} catch (Exception e) {
			String logText = getMessageText(thisEnclosingJoinPointStaticPart.getId());
			TexlipsePlugin.log(logText, e);
		}
		return "";
	}
}
