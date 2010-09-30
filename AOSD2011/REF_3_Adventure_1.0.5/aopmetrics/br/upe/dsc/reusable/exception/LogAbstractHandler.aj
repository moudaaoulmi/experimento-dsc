package br.upe.dsc.reusable.exception;

@ExceptionHandler
public abstract privileged aspect LogAbstractHandler {
	
	public abstract pointcut checkedExceptionLog();
	
	public abstract pointcut exceptionLog();
	
	/*
	 Handleres que tratam exce��es subclasses de Exception mas:
	 - n�o as que herdam de RuntimeException.
	 - no m�todo Java n�o tem throws para uma exce��o checada. Ou seja, toda exce��o checada � capturada e tratada ali mesmo.
	 - utiliza de objeto de retorno, padr�o null, ou valor padr�o para tipo primitivo ou void
	 - Os catch s�o unicos para cada try. ou catch duplicado com mensagens semelhantes
	 OBS: Aten��o para um mesmo try com v�rios catchs com o log, mas mensagens diferentes
	*/
	Object around(): checkedExceptionLog()
	{
		Object result = null;
		try {
			result = proceed();
		} catch (RuntimeException re){
			throw re;
		} catch (Exception e) {
//			String logText = getMessageText(thisEnclosingJoinPointStaticPart.getId());
			String logText = getMessageText(0);
			getLogObject().logGeneral(logText, e);
		}
		return result;
	}
	
	//Handleres que tratam todos os tipos de exce��es Exception
	Object around(): exceptionLog() {
		Object result = null;
		try {
			result = proceed();
		} catch (Exception e) {
//			String logText = getMessageText(thisEnclosingJoinPointStaticPart.getId());
			String logText = getMessageText(0);
			getLogObject().logGeneral(logText, e);
		}
		return result;
	}
	
	public abstract String getMessageText(int pointcutId);
	
	public abstract ILogObject getLogObject();
}
