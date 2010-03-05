package lancs.mobilemedia.aspects.exceptionblocks;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

import lancs.mobilemedia.lib.exceptions.InvalidMediaDataException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;
import lancs.mobilemedia.lib.exceptions.UnavailablePhotoAlbumException;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.datamodel.MediaAccessor;
import lancs.mobilemedia.core.ui.datamodel.MediaData;
import lancs.mobilemedia.exception.CheckedRecordStoreException;

public aspect DataModelAspectEH extends CheckedRecordStoreException {
	
	public pointcut checkedRecordStoreException() : execution(public void MediaAccessor.addMediaData(String, String, String));
	
	//Method public void ImageMediaAccessor.addImageData(String, String, String) 1- block - Scenario 1
	//pointcut addMediaData(): execution(public void MediaAccessor.addMediaData(String, String, String));
	pointcut loadMediaDataFromRMS(): execution(public MediaData[] MediaAccessor.loadMediaDataFromRMS(String));
	pointcut updateMediaInfo(): execution(public boolean MediaAccessor.updateMediaInfo(MediaData, MediaData));
	pointcut updateMediaInfoAround(): call(public void RecordStore.closeRecordStore(..))&& (withincode(public void MediaAccessor.updateMediaRecord(MediaData, MediaData)));
	pointcut loadMediaBytesFromRMS(): execution(public byte[] MediaAccessor.loadMediaBytesFromRMS(String, int));
	pointcut deleteSingleMediaFromRMS(): execution(public boolean MediaAccessor.deleteSingleMediaFromRMS(String, String));
	pointcut createNewAlbum(): (call(public RecordStore RecordStore.openRecordStore(String, boolean)) || call(public void RecordStore.closeRecordStore(..)))&& (withincode(public void MediaAccessor.createNewAlbum(String)));
	pointcut deleteAlbum(): execution(public void MediaAccessor.deleteAlbum(String));
	pointcut getAlbumNames(): call(public void MediaAccessor.loadAlbums())&& (withincode(public String[] AlbumData.getAlbumNames()));
	pointcut getMedias(): execution(public MediaData[] AlbumData.getMedias(String));
	pointcut resetMediaData(): execution(public void AlbumData.resetMediaData());
	
	
	declare soft: RecordStoreException : loadMediaDataFromRMS() || updateMediaInfo() 
										|| loadMediaBytesFromRMS() || deleteSingleMediaFromRMS() || createNewAlbum() || deleteAlbum();
	declare soft:  InvalidMediaDataException : getAlbumNames() || getMedias() || resetMediaData();
	declare soft:  PersistenceMechanismException : getAlbumNames() || getMedias();
	
	
//	void around() throws PersistenceMechanismException: addMediaData(){
//		try {
//			proceed();
//		} catch (RecordStoreException e) {
//			throw new  PersistenceMechanismException();
//		}
//	}
	
	Object around() throws  PersistenceMechanismException: loadMediaDataFromRMS() || updateMediaInfo() || loadMediaBytesFromRMS()
																|| deleteSingleMediaFromRMS() || deleteAlbum(){
		try {
			return proceed();
		} catch(RecordStoreException e) {
			throw new PersistenceMechanismException(e);
		}
	}
	
	void around() : updateMediaInfoAround() {
		try {
			proceed();
		} catch(RecordStoreNotOpenException e) {
			//No problem if the RecordStore is not Open
			
			//catch (RecordStoreException e) {
			//	throw new PersistenceMechanismException(e);
			//}
		}
	}
	
	void around(): getAlbumNames(){
		try{
			proceed();
		} catch (PersistenceMechanismException e) {
			e.printStackTrace();
		}
	}	
	
	void around(): getAlbumNames() || resetMediaData(){
		try{
			proceed();
		} catch (InvalidMediaDataException e) {
			e.printStackTrace();
		} 
	}	
		
	MediaData[] around() throws  UnavailablePhotoAlbumException: getMedias(){
		try {
			return proceed();
		} catch(Exception e) {
			if (e instanceof PersistenceMechanismException)
				throw new UnavailablePhotoAlbumException(e);
			else if (e instanceof InvalidMediaDataException)
				throw new UnavailablePhotoAlbumException(e);
		}
		return null;
	}
}
