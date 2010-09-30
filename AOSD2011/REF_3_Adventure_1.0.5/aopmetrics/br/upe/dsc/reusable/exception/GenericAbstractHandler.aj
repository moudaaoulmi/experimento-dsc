package br.upe.dsc.reusable.exception;

@ExceptionHandler
public abstract privileged aspect  GenericAbstractHandler {

	public abstract pointcut checkedExceptionLog();

	/*
	 * Handleres que tratam exceções subclasses de Exception mas: - não as que
	 * herdam de RuntimeException. - no método Java não tem throws para uma
	 * exceção checada. Ou seja, toda exceção checada é capturada e tratada ali
	 * mesmo. - utiliza de objeto de retorno, padrão null, ou valor padrão para
	 * tipo primitivo ou void - Os catch são unicos para cada try. ou catch
	 * duplicado com mensagens semelhantes OBS: Atenção para um mesmo try com
	 * vários catchs com o log, mas mensagens diferentes
	 */
	Object around(): checkedExceptionLog()	{
		Object result = null;
		try {
			result = proceed();
		} catch (RuntimeException re) {
			throw re;
		} catch (Exception e) {
//			this.getMessageText(thisEnclosingJoinPointStaticPart.getId(), e);
			this.getMessageText(0, e);
		}
		return result;
	}

	public abstract void getMessageText(int pointcutId, Exception e);

}
