package lancs.mobilemedia.optional.pim;
import java.lang.Exception;
import javax.microedition.pim.PIMException;

public aspect OptionalPimHanlder {
	
	pointcut internalPimInstHandler() : execution(void PimController.internalPimInst());
	pointcut runHandler() : execution(void PimControllerThread.run());
	pointcut pimSeedHandler() : execution(public PimSeed.new());
	pointcut internalSeedHandler() : execution(void PimSeed.internalSeed());
	
	declare soft : Exception : internalPimInstHandler() || runHandler() || pimSeedHandler();
	declare soft : PIMException : internalSeedHandler(); 

	void around(): internalPimInstHandler(){
		try{
			proceed();
		}catch(Exception ex){
		//TO DO
		}
	}
	
	void around(): runHandler(){
		try{
			proceed();
		}catch(Exception e){
			//TO DO
		}
	}
	
	
	void around(): internalSeedHandler(){
		try{
			proceed();
		}catch(PIMException ex){
			//Contact List is not supported
		}
	}
	
	void around(): pimSeedHandler(){
		try{
			proceed();
		}catch(Exception e){
			//TO DO
		}
	}
}
