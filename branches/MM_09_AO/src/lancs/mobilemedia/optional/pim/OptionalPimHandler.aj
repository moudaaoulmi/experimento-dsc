package lancs.mobilemedia.optional.pim;

import java.lang.Exception;

import br.upe.dsc.reusable.exception.EmptyBlockAbstractExceptionHandling;

public aspect OptionalPimHandler extends EmptyBlockAbstractExceptionHandling {
	
	pointcut internalPimInstHandler() : execution(void PimController.internalPimInst());
	pointcut runHandler() : execution(void PimControllerThread.run());
	pointcut pimSeedHandler() : execution(public PimSeed.new());
	pointcut internalSeedHandler() : execution(void PimSeed.internalSeed());
	public pointcut emptyBlockException(): pimSeedHandler() || runHandler() || internalPimInstHandler() || internalSeedHandler();
	
	declare soft : Exception : emptyBlockException();
	//declare soft : PIMException : internalSeedHandler(); 
	
//	void around(): pimSeedHandler() || runHandler() || internalPimInstHandler() || internalSeedHandler(){
//		try{
//			proceed();
//		} catch(RuntimeException e) {
//			throw e;
//		} catch(Exception e){
//			//TO DO
//		}
//	}
	
//	void around(): internalSeedHandler(){
//		try{
//			proceed();
//		}catch(PIMException ex){
//			//Contact List is not supported
//		}
//	}
}
