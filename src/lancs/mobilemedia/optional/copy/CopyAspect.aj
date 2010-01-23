/*
 * Lancaster University
 * Computing Department
 * 
 * Created by Eduardo Figueiredo
 * Date: 18 Aug 2008
 * 
 */
package lancs.mobilemedia.optional.copy;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

import lancs.mobilemedia.core.ui.controller.AbstractController;
import lancs.mobilemedia.core.ui.controller.MediaController;
import lancs.mobilemedia.core.ui.datamodel.MediaAccessor;
import lancs.mobilemedia.core.ui.datamodel.MediaData;
import lancs.mobilemedia.core.ui.screens.AlbumListScreen;
import lancs.mobilemedia.lib.exceptions.InvalidMediaDataException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;
import lancs.mobilemedia.optional.copySMS.PhotoViewController;

/**
 * @author Eduardo Figueiredo
 *
 */
public abstract aspect CopyAspect {

	// ********  MediaController  ********* //
	
	//public AbstractController PhotoController.getMediaController(String mediaName)
	pointcut getMediaController(MediaController controller, String imageName): 
		 (call(public AbstractController MediaController.getMediaController(String)) && this(controller))&& args (imageName);
	
	AbstractController around (MediaController controller, String imageName): getMediaController(controller, imageName) {
		AbstractController nextcontroller = proceed(controller, imageName);
		PhotoViewController control = new PhotoViewController(controller.midlet, controller.getAlbumData(), (AlbumListScreen) controller.getAlbumListScreen(), imageName);
		control.setNextController(nextcontroller);
		return control;
	}
	
	// ********  MediaAccessor  ********* //
	
	/**
	 * [EF] Add in scenario 05
	 * @param photoname
	 * @param imageData
	 * @param albumname
	 * @throws InvalidMediaDataException
	 * @throws PersistenceMechanismException
	 */
	public void MediaAccessor.addMediaData(MediaData mediaData, String albumname) throws InvalidMediaDataException, PersistenceMechanismException {
		try {
			mediaRS = RecordStore.openRecordStore(album_label + albumname, true);
			mediaInfoRS = RecordStore.openRecordStore(info_label + albumname, true);
			int rid2; // new record ID for ImageData (metadata)
			rid2 = mediaInfoRS.getNextRecordID();
			mediaData.setRecordId(rid2);
			byte[] data1 = this.getByteFromMediaInfo(mediaData);
			mediaInfoRS.addRecord(data1, 0, data1.length);
		} catch (RecordStoreException e) {
			throw new PersistenceMechanismException();
		}finally{
			try {
				mediaRS.closeRecordStore();
				mediaInfoRS.closeRecordStore();
			} catch (RecordStoreNotOpenException e) {
				e.printStackTrace();
			} catch (RecordStoreException e) {
				e.printStackTrace();
			}
		}
	}
}
