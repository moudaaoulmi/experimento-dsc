package lancs.mobilemedia.exception;

import javax.microedition.rms.RecordStoreException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;

public abstract aspect CheckedRecordStoreException {
	
	public abstract pointcut checkedRecordStoreException();
	
	declare soft: RecordStoreException: checkedRecordStoreException();
	
	void around() throws PersistenceMechanismException : checkedRecordStoreException() {
		try {
			proceed();
		} catch (RecordStoreException e) {
			throw new PersistenceMechanismException();
		}
	}

}
