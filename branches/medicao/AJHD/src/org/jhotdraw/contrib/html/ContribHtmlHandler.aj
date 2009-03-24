package org.jhotdraw.contrib.html;

import org.aspectj.lang.SoftException;



public privileged aspect ContribHtmlHandler {
	
	/** declare soft: InterruptedException: joinHandler();*/
	declare soft: ClassNotFoundException: readPartOneHandler();
	declare soft: ResourceManagerNotSetException: initManagerPartOneHandler();
    declare soft: HTMLTextAreaFigure.InvalidAttributeMarker: substituteEntityKeywordsPartOneHandler(); 
    declare soft: Exception : getContentHandler(); /**|| runHandler();*/
	
	
	
	/** Rever refatoracao ver se nao ta conseguindo ver a classe DisposalThread que 
	 * é uma classe LOCAL 
	pointcut joinHandler(): 
		call(* *.join(..)) && 
		withincode(* ETSLADisposalStrategy.stopDisposingPartOne(..)); */
	
	pointcut readPartOneHandler(): execution(* ContentProducerRegistry.readPartOne(..) );
	pointcut initManagerPartOneHandler():call (*  DisposableResourceManager.startDisposing(..)) &&
    									 withincode(* DisposableResourceManagerFactory.initManagerPartOne());
	pointcut substituteEntityKeywordsPartOneHandler(): execution(* HTMLTextAreaFigure.substituteEntityKeywordsPartOne(.., int));
	
	pointcut  getContentHandler():execution(* ResourceContentProducer.getContent(..)) ||
								  execution(* URLContentProducer.getContent(..));
	/** pointcut runHandler(): execution (* DisposalThread.run(..));*/
	 
	
	 
	/**
	 * cenário em que eu não consigo modificar o valor da variável OBJ porque é privada da classe 
	 * ETSLADisposalStrategy, e não tem método setVariável() pra eu alterar o seu valor
	 *
	 void around(): joinHandler() {
			// = (ETSLADisposalStrategy) thisJoinPoint.getThis();
			try {
				proceed();
			}
			catch (InterruptedException ex) {
				// ignore
			}
		} */
	
	 
	 
	 void around(): readPartOneHandler(){
			try {
			    proceed();
			}
			catch (ClassNotFoundException ex) {
				// the class does not exist in this application
				// cannot do much about it so ignore it, the entities of
				// this class will get their toString() value instead
			}
		}
		
	
	 
	 void around(): initManagerPartOneHandler(){
			try {
				proceed();
			}
			catch (ResourceManagerNotSetException ex) {
				// we set it so we shouldn't get here
			}
	}
	 
	 /**
	  * Não tem como acessar essa classe da exceção e com Exception aí não funciona
	  */
	 int around(int chunkEnd): substituteEntityKeywordsPartOneHandler() && args(chunkEnd){
			try {
				chunkEnd = proceed(chunkEnd);
			}
			catch (SoftException ex) {
				// invalid marker, ignore
			}
			return chunkEnd;
	} 
	
	 
	 
	Object around() : getContentHandler() {
		Object obj = null;
		try{
			obj =  proceed();
		}catch (Exception ex) {
			ex.printStackTrace();
			return ex.toString();
		} 
		return obj;
		
	}	
	
	/** 
	 *  Não deu para refatorar
	 * 
	void around() : runHandler(){
		try{
			proceed();
		}catch (Exception ex) {
		// just exit
		 break;
	    }
	}
	*/
	

	
}
