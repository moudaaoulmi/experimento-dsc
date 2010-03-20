package lancs.mobilemedia.alternative.musicvideo;

import lancs.mobilemedia.alternative.musicvideo.MultiMediaAccessor;
import lancs.mobilemedia.core.ui.datamodel.MediaData;
import lancs.mobilemedia.lib.exceptions.InvalidMediaDataException;
import lancs.mobilemedia.alternative.musicvideo.MultiMediaUtil;
import br.upe.dsc.reusable.exception.PrintStackTraceAbstractExceptionHandler;
import lancs.mobilemedia.lib.exceptions.MediaNotFoundException;

public privileged aspect AlternativeMusicVideoHandler extends PrintStackTraceAbstractExceptionHandler {
	
	public pointcut printStackTraceException(): execution(void MultiMediaAccessor.internalResetRecordStore(MediaData));
	pointcut getBytesFromMediaInfoHandler(): execution(String MultiMediaUtil.getBytesFromMediaInfo(MediaData));
	
	declare soft: Exception: (getBytesFromMediaInfoHandler()) || 
							 (printStackTraceException());

	
	String around() throws InvalidMediaDataException : getBytesFromMediaInfoHandler() {
		try {
			return proceed();
		} catch (Exception e) {
			throw new InvalidMediaDataException("The provided data are not valid");
		}
	}  
}
