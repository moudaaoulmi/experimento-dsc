package ish.ecletex.wordnet.jwnl.util.factory;

import ish.ecletex.wordnet.jwnl.JWNLException;

public privileged aspect FactoryHandler {

	pointcut createHandler(): execution(public Object AbstractValueParam.create());

	pointcut installHandler(): execution(public void Element.install());

	declare soft: Exception: createHandler() || installHandler();

	void around(Element ele) throws JWNLException  : installHandler() && this(ele){
		try {
			proceed(ele);
		} catch (Exception ex) {
			throw new JWNLException("UTILS_EXCEPTION_005", ele._className, ex);
		}
	}

	Object around(AbstractValueParam abs) throws JWNLException : createHandler() && this(abs) {
		try {
			return proceed(abs);
		} catch (Exception ex) {
			throw new JWNLException("JWNL_EXCEPTION_004", abs.getValue(), ex);
		}
	}

}
