package org.jhotdraw.contrib.html;


public privileged aspect ContribHtmlExceptionHandler {
	
	// pointuts
	
	pointcut ETSLADisposalStrategy_stopDisposingHandler(): execution(public void ETSLADisposalStrategy.stopDisposing(..));
	pointcut ETSLADisposalStrategy_internalRunHandler(): execution(private void DisposalThread.internalRun(..));
	pointcut ContentProducerRegistry_internalReadHandler(): execution(private void ContentProducerRegistry.internalRead(..) );
	pointcut DisposalThread_internalRunHandler(): execution (private void DisposalThread.internalRun(..));
	pointcut DisposalThread_internalWhileHandler(): execution (private void DisposalThread.internalWhile(..));
	pointcut DisposableResourceManagerFactory_initManagerPartOneHandler(): execution(protected static void DisposableResourceManagerFactory.initManager());
	pointcut HTMLTextAreaFigure_internalSubstituteEntityKeywordsPartOne(): execution(private int HTMLTextAreaFigure.internalSubstituteEntityKeywordsPartOne(..));	
	pointcut ResourceContentProducer_getContentHandler(): execution(public Object ResourceContentProducer.getContent(..));
	pointcut URLContentProducer_getContent(): execution(public Object URLContentProducer.getContent(..));
		 
	// intertypes
	
	declare soft: InterruptedException: ETSLADisposalStrategy_stopDisposingHandler() || ETSLADisposalStrategy_internalRunHandler();
	declare soft: ClassNotFoundException: ContentProducerRegistry_internalReadHandler();
	declare soft: ResourceManagerNotSetException: DisposableResourceManagerFactory_initManagerPartOneHandler();
    declare soft: HTMLTextAreaFigure.InvalidAttributeMarker: HTMLTextAreaFigure_internalSubstituteEntityKeywordsPartOne(); 
    declare soft: Exception : ResourceContentProducer_getContentHandler() || 
    							URLContentProducer_getContent()|| DisposalThread_internalWhileHandler();
	 
	
    // advices
    
    void around(): ETSLADisposalStrategy_stopDisposingHandler() {
    	try {
    		proceed();
    	}
    	catch (InterruptedException ex) {
    		// ignore
    	}
    	finally {
    		ETSLADisposalStrategy objeto = (ETSLADisposalStrategy) thisJoinPoint.getThis();
    		objeto.disposingActive = false;
    	}
    } 
    
    void around():ETSLADisposalStrategy_internalRunHandler(){
    	try {
			proceed();
		}
		catch (Exception ex) {
			//break;
		}
    }
	
	 
	 
	 void around(): ContentProducerRegistry_internalReadHandler(){
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
	 
	
	 int around(String template,
				int endPos, StringBuffer finalText, int startPos, int chunkEnd): HTMLTextAreaFigure_internalSubstituteEntityKeywordsPartOne() 
				&& args(template, endPos, finalText, startPos, chunkEnd){
			try {
				return proceed(template, endPos, finalText, startPos, chunkEnd);
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
	
}
