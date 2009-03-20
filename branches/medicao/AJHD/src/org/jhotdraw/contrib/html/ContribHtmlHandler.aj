package org.jhotdraw.contrib.html;

import java.lang.*;


public privileged aspect ContribHtmlHandler {
	
	/** declare soft: InterruptedException: joinHandler();*/
	declare soft: ClassNotFoundException: readPartOneHandler();
	declare soft: ResourceManagerNotSetException: initManagerPartOneHandler();
    /**  declare soft: InvalidAttributeMarker: substituteEntityKeywordsPartOneHandler(); */
    declare soft: Exception : getContentHandler();
	
	
	
	/** Rever refatoracao
	pointcut joinHandler(): call(* DisposalThread.join(..)) &&
			withincode(* ETSLADisposalStrategy.stopDisposingPartOne(..));*/
	
	pointcut readPartOneHandler(): execution(* ContentProducerRegistry.readPartOne(..) );
	pointcut initManagerPartOneHandler():call (*  DisposableResourceManager.startDisposing(..)) &&
    									 withincode(* DisposableResourceManagerFactory.initManagerPartOne());
	/**pointcut substituteEntityKeywordsPartOneHandler(): execution(* HTMLTextAreaFigure.substituteEntityKeywordsPartOne(..));*/
	
	pointcut  getContentHandler():execution(* ResourceContentProducer.getContent(..)) ||
								  execution(* URLContentProducer.getContent(..));
	 
	
	 
	/**
	 * cen�rio em que eu n�o consigo modificar o valor da vari�vel OBJ porque � privada da classe 
	 * ETSLADisposalStrategy, e n�o tem m�todo setVari�vel() pra eu alterar o seu valor
	 *
	 void around(): joinHandler() {
			ETSLADisposalStrategy obj = (ETSLADisposalStrategy) thisJoinPoint.getThis();
			try {
				proceed();
			}
			catch (InterruptedException ex) {
				// ignore
			}
			finally {
				obj.disposingActive = false;
			}
		}
	*/
	 
	 
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
	  *
	 void around(): substituteEntityKeywordsPartOneHandler(){
			try {
				proceed();
			}
			catch (InvalidAttributeMarker ex) {
				// invalid marker, ignore
			}
	} 
	*/
	 
	 
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
	int around(String template, StringBuffer finalText, int startPos, int chunkEnd): HTMLTextAreaFigure_substitute()
	&& args(template,finalText, startPos, chunkEnd){ 
		try{
			return proceed(template,finalText, startPos, chunkEnd);
		}catch (Exception ex) {
			// invalid marker, ignore
		}
		return chunkEnd;
	}
	*/

}
