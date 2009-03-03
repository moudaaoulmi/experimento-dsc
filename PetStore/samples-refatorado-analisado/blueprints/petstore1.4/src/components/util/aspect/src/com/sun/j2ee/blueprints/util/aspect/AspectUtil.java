package com.sun.j2ee.blueprints.util.aspect;

import org.aspectj.lang.JoinPoint;

public class AspectUtil {
	
	/**
	 * Verifica se o joinPoint se referencia ao metodo.  
	 * 
	 * @param joinPoint representa o JointPoint associado ao aspecto em tempo de execução
	 * @param metodo nome do método que está associado ao pointcut. O formato do nome deve ser o seguinte: modificador retorno NomeDaClasse.metodo(tipoParametro1,tipoParametro2) 
	 * @return boolean true se o joinPoint a ser tratado for o metodo recebido como argumento
	 */
	public static boolean verifyJointPoint(JoinPoint joinPoint, String metodo){		
		return joinPoint.toLongString().contains(metodo);
	}	
}
