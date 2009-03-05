package org.jhotdraw.contrib.html;

public class HtmlHandler {
	public void contentProducerRegistryRead(){
		// the class does not exist in this application
		// cannot do much about it so ignore it, the entities of
		// this class will get their toString() value instead
	}
	
	public void disposableResourceManagerFactoryInitManager(){
		// we set it so we shouldn't get here
	}
	
	public void eTSLADisposalStrategyStopDisposing1(){
		// ignore
	}
	
	public void eTSLADisposalStrategyStopDisposing2(boolean disposingActive){
		disposingActive = false;
	}
	
	public void eTSLADisposalStrategyDisposalThreadRun(){
		// just exit
	}
	public void hTMLTextAreaFigureSubstituteEntityKeywords(){
		// invalid marker, ignore
	}
	
	public String resourceContentProducerGetContent(Exception ex){
		ex.printStackTrace();
		return ex.toString();
		
	}
	
}
