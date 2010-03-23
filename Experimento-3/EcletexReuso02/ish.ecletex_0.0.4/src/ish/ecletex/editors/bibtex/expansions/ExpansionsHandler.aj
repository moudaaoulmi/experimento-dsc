package ish.ecletex.editors.bibtex.expansions;

import ish.ecletex.editors.bibtex.dom.BibtexEntry;

public privileged aspect ExpansionsHandler {

	pointcut internalThrowExpansionExceptionHandler(): execution(private void AbstractExpander.internalThrowExpansionException(String));

	pointcut internalThrowExpansionExceptionHandler2(): execution(private void AbstractExpander.internalThrowExpansionException(Exception));

	pointcut mainHandler(): execution(public void BibtexPersonListParser.main(String[]));

	pointcut internalExpandHandler(): execution(private void PersonListExpander.internalExpand(BibtexEntry, String));

	declare soft: ExpansionException : internalThrowExpansionExceptionHandler() || internalThrowExpansionExceptionHandler2();
	declare soft: PersonListParserException : mainHandler() || internalExpandHandler();

	void around(AbstractExpander abs): (internalThrowExpansionExceptionHandler() || internalThrowExpansionExceptionHandler2() )&& this(abs) {
		try {
			proceed(abs);
		} catch (ExpansionException e) {
			abs.exceptions.add(e);
		}
	}	

	// Excecao nao é visivel fora do pacote
	void around(): mainHandler() {
		try {
			proceed();
		} catch (PersonListParserException e) {
			e.printStackTrace();
		}
	}

	void around(PersonListExpander person) throws ExpansionException :internalExpandHandler() && this(person) {
		try {
			proceed(person);
		} catch (PersonListParserException e) {
			person.throwExpansionException(e);
		}
	}

}
