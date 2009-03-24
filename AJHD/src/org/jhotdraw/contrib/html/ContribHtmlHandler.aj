package org.jhotdraw.contrib.html;

import org.aspectj.lang.SoftException;



public privileged aspect ContribHtmlHandler {
	
	/** declare soft: InterruptedException: joinHandler();*/
	declare soft: ClassNotFoundException: readPartOneHandler();
	declare soft: ResourceManagerNotSetException: initManagerPartOneHandler();
    declare soft: HTMLTextAreaFigure.InvalidAttributeMarker: substituteEntityKeywordsPartOneHandler(); 
    declare soft: Exception : getContentHandler(); /**|| runHandler();*/
	
	
	
	/** Rever refatoracao ver se nao ta conseguindo ver a classe DisposalThread que 
	 * � uma classe LOCAL 
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
	 * cen�rio em que eu n�o consigo modificar o valor da vari�vel OBJ porque � privada da classe 
	 * ETSLADisposalStrategy, e n�o tem m�todo setVari�vel() pra eu alterar o seu valor
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
	  * N�o tem como acessar essa classe da exce��o e com Exception a� n�o funciona
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
	 *  N�o deu para refatorar
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
