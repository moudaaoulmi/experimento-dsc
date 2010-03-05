package lancs.mobilemedia.core.ui.datamodel;

import javax.microedition.rms.RecordStoreException;

//import lancs.mobilemedia.exception.ExceptionHandler;

//@ExceptionHandler
public aspect CoreUiDataModelHandler {
	
	pointcut internalRemoveRecordsHandler() : execution(void MediaAccessor.internalRemoveRecords(String, String, int));
	
	declare soft: RecordStoreException : internalRemoveRecordsHandler();
	
	void around(String storeName, String infoStoreName) :  internalRemoveRecordsHandler() && args(storeName, infoStoreName, *) { 
		try {
			proceed(storeName, infoStoreName);
		} catch (RecordStoreException e) {
			System.out.println("No record store named " + storeName + " to delete.");
			System.out.println("...or...No record store named " + infoStoreName + " to delete.");
			System.out.println("Ignoring Exception: " + e);
			// ignore any errors...
		}
	}

}
