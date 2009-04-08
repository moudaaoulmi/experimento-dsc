package org.jhotdraw.contrib.html;


@ExceptionHandler
public privileged aspect ContribHtmlHandler {
	
	// pointuts
	
	pointcut ETSLADisposalStrategy_stopDisposingPartOne(): execution(private void ETSLADisposalStrategy.stopDisposingPartOne(..)); 
	pointcut ContentProducerRegistry_readPartOneHandler(): execution(private void ContentProducerRegistry.readPartOne(..) );
	pointcut DisposalThread_internalRunHandler(): execution (private void DisposalThread.internalRun(..));
	pointcut DisposalThread_internalWhileHandler(): execution (private void DisposalThread.internalWhile(..));
	pointcut DisposableResourceManagerFactory_initManagerPartOneHandler(): execution(private static void DisposableResourceManagerFactory.initManagerPartOne());
	pointcut HTMLTextAreaFigure_internalSubstituteEntityKeywordsPartOne(): execution(private int HTMLTextAreaFigure.internalSubstituteEntityKeywordsPartOne(..));	
	pointcut ResourceContentProducer_getContentHandler(): execution(public Object ResourceContentProducer.getContent(..));
	pointcut URLContentProducer_getContent(): execution(public Object URLContentProducer.getContent(..));
		 
	// intertypes
	
	declare soft: InterruptedException: ETSLADisposalStrategy_stopDisposingPartOne();
	declare soft: ClassNotFoundException: ContentProducerRegistry_readPartOneHandler();
	declare soft: ResourceManagerNotSetException: DisposableResourceManagerFactory_initManagerPartOneHandler();
    declare soft: HTMLTextAreaFigure.InvalidAttributeMarker: HTMLTextAreaFigure_internalSubstituteEntityKeywordsPartOne(); 
    declare soft: Exception : ResourceContentProducer_getContentHandler()||URLContentProducer_getContent()|| DisposalThread_internalWhileHandler();
	 
	
    // advices
    
	 void around(): ETSLADisposalStrategy_stopDisposingPartOne() {
		 ETSLADisposalStrategy objeto = (ETSLADisposalStrategy) thisJoinPoint.getThis();
			try {
				proceed();
			}
			catch (InterruptedException ex) {
				// ignore
			}
			finally {
				
				objeto.disposingActive = false;
			}
		} 
	
	 
	 
	 void around(): ContentProducerRegistry_readPartOneHandler(){
			try {
			    proceed();
			}
			catch (ClassNotFoundException ex) {
				// the class does not exist in this application
				// cannot do much about it so ignore it, the entities of
				// this class will get their toString() value instead
			}
		}
		
	
	 
	 void around(): DisposableResourceManagerFactory_initManagerPartOneHandler(){
			try {
				proceed();
			}
			catch (ResourceManagerNotSetException ex) {
				// we set it so we shouldn't get here
			}
	}
	 
	
	 int around(String template, StringBuffer finalText, int startPos,
				int chunkEnd): HTMLTextAreaFigure_internalSubstituteEntityKeywordsPartOne() && args(template, finalText, startPos,
						chunkEnd){
			try {
				return proceed(template, finalText, startPos,chunkEnd);
			}
			catch (HTMLTextAreaFigure.InvalidAttributeMarker ex) {
				// invalid marker, ignore
			}
			return chunkEnd;
	} 
	
	 
	 
	Object around() : ResourceContentProducer_getContentHandler()||URLContentProducer_getContent() {
		Object obj = null;
		try{
			obj =  proceed();
		}catch (Exception ex) {
			ex.printStackTrace();
			return ex.toString();
		} 
		return obj;
		
	}	
	
	// Tratamento do while
	
	void around(): DisposalThread_internalRunHandler(){
		try{
			proceed();
		}catch (BreakException ex) {
		   // nada
	    }
	}
	
	
	void around() : DisposalThread_internalWhileHandler(){
		try{
			proceed();
		}catch (Exception ex) {
		// just exit
		 //break;
			throw new BreakException();
	    }
	}
	

	
}
