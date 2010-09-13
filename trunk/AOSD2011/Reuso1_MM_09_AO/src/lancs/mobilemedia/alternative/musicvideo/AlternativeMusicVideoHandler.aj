package lancs.mobilemedia.alternative.musicvideo;

import lancs.mobilemedia.alternative.musicvideo.MultiMediaAccessor;
import lancs.mobilemedia.lib.exceptions.MediaNotFoundException;
import lancs.mobilemedia.core.ui.datamodel.MediaData;
import lancs.mobilemedia.lib.exceptions.InvalidMediaDataException;
import lancs.mobilemedia.alternative.musicvideo.MultiMediaUtil;

public privileged aspect AlternativeMusicVideoHandler {
	
	pointcut internalResetRecordStoreHandler(): execution(void MultiMediaAccessor.internalResetRecordStore(MediaData));
	pointcut getBytesFromMediaInfoHandler(): execution(String MultiMediaUtil.getBytesFromMediaInfo(MediaData));
	
	declare soft: MediaNotFoundException: internalResetRecordStoreHandler();
	declare soft: Exception: getBytesFromMediaInfoHandler();

	void around(): internalResetRecordStoreHandler() {
		try {
			proceed();
		} catch (MediaNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	String around() throws InvalidMediaDataException : getBytesFromMediaInfoHandler() {
		try {
			return proceed();
		} catch (Exception e) {
			throw new InvalidMediaDataException("The provided data are not valid");
		}
	}
}
