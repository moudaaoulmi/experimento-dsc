package lancs.mobilemedia.optional.pim;

import java.lang.Exception;
import br.upe.dsc.reusable.exception.EmptyBlockAbstractExceptionHandling;

public aspect OptionalPimHandler extends EmptyBlockAbstractExceptionHandling {
	
	public pointcut emptyBlockException(): (execution(public PimSeed.new())) || 
										   (execution(void PimControllerThread.run())) || 
										   (execution(void PimController.internalPimInst())) || 
										   (execution(void PimSeed.internalSeed()));
	
	declare soft : Exception : emptyBlockException();
}