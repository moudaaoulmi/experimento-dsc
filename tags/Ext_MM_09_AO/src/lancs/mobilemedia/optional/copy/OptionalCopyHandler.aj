package lancs.mobilemedia.optional.copy;

import lancs.mobilemedia.core.ui.datamodel.MediaData;
import lancs.mobilemedia.core.ui.datamodel.MediaAccessor;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;

import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

public aspect OptionalCopyHandler {
	
	pointcut addMediaDataHandler(): execution(void MediaAccessor.addMediaData(MediaData, String));
	
	declare soft: RecordStoreException: addMediaDataHandler();
	
	void around(MediaAccessor mediaAccessor) throws PersistenceMechanismException: addMediaDataHandler() && this(mediaAccessor) {
		try {
			proceed(mediaAccessor);
		} catch (RecordStoreException e) {
			throw new PersistenceMechanismException();
		}finally{
			try {
				mediaAccessor.mediaRS.closeRecordStore();
				mediaAccessor.mediaInfoRS.closeRecordStore();
			} catch (RecordStoreNotOpenException e) {
				e.printStackTrace();
			} catch (RecordStoreException e) {
				e.printStackTrace();
			}
		}
	}

}
