/*
 * Created on Sep 28, 2004
 */
package lancs.mobilemedia.core.ui.datamodel;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

import lancs.mobilemedia.lib.exceptions.MediaNotFoundException;
import lancs.mobilemedia.lib.exceptions.InvalidMediaDataException;
import lancs.mobilemedia.lib.exceptions.InvalidAlbumNameException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;
import lancs.mobilemedia.lib.exceptions.UnavailablePhotoAlbumException;

/**
 * @author tyoung
 * 
 * This class represents the data model for Photo Albums. A Photo Album object
 * is essentially a list of photos or images, stored in a Hashtable. Due to
 * constraints of the J2ME RecordStore implementation, the class stores a table
 * of the images, indexed by an identifier, and a second table of image metadata
 * (ie. labels, album name etc.)
 * 
 * This uses the ImageAccessor class to retrieve the image data from the
 * recordstore (and eventually file system etc.)
 */
public abstract class AlbumData {

	protected MediaAccessor mediaAccessor;
	
	/**
	 *  Load any photo albums that are currently defined in the record store
	 */
	public String[] getAlbumNames() {
		//Shouldn't load all the albums each time
		//Add a check somewhere in ImageAccessor to see if they've been
		//loaded into memory already, and avoid the extra work...
		try {
			mediaAccessor.loadAlbums();
		} catch (InvalidMediaDataException e) {
			e.printStackTrace();
		} catch (PersistenceMechanismException e) {
			e.printStackTrace();
		}
		return mediaAccessor.getAlbumNames();
	}

	/**
	 *  Get all images for a given Photo Album that exist in the Record Store.
	 * @throws UnavailablePhotoAlbumException 
	 * @throws InvalidMediaDataException 
	 * @throws PersistenceMechanismException 
	 */
	public MediaData[] getMedias(String recordName) throws UnavailablePhotoAlbumException  {

		MediaData[] result;
		try {
			result = mediaAccessor.loadMediaDataFromRMS(recordName);
		} catch (PersistenceMechanismException e) {
			throw new UnavailablePhotoAlbumException(e);
			
		} catch (InvalidMediaDataException e) {
			throw new UnavailablePhotoAlbumException(e);
		}

		return result;

	}

	/**
	 *  Define a new user photo album. This results in the creation of a new
	 *  RMS Record store.
	 * @throws PersistenceMechanismException 
	 * @throws InvalidAlbumNameException 
	 */
	public void createNewAlbum(String albumName) throws PersistenceMechanismException, InvalidAlbumNameException {
		mediaAccessor.createNewAlbum(albumName);
	}
	
	//#ifdef includePrivacy

	//[CD] Define a password to a specific album	
	public void addPassword(String albumname, String passwd){
		mediaAccessor.addPassword(albumname, passwd);
	}
	
	//[CD] Get a password from a specific album
	public String getPassword(String albumname){
		return mediaAccessor.getPassword(albumname);  
	}
	
	
	//#endif
	
	/**
	 * @param albumName
	 * @throws PersistenceMechanismException
	 */
	public void deleteAlbum(String albumName) throws PersistenceMechanismException{
		mediaAccessor.deleteAlbum(albumName);
	}

	/**
	 * @param label
	 * @param path
	 * @param album
	 * @throws InvalidMediaDataException
	 * @throws PersistenceMechanismException
	 */
	public void addNewMediaToAlbum(String label, String path, String album) throws InvalidMediaDataException, PersistenceMechanismException{
		mediaAccessor.addMediaData(label, path, album);
	}

	// #ifdef includeCopyPhoto
	/**
	 * @param mediaData
	 * @param albumname
	 * @throws InvalidMediaDataException
	 * @throws PersistenceMechanismException
	 */
	public void addMediaData(MediaData mediaData, String albumname) throws InvalidMediaDataException, PersistenceMechanismException {
		mediaAccessor.addMediaData(mediaData, albumname);
	}
	// #endif
	
	/**
	 *  Delete a photo from the photo album. This permanently deletes the image from the record store
	 * @throws MediaNotFoundException 
	 * @throws PersistenceMechanismException 
	 */
	public void deleteMedia(String mediaName, String storeName) throws PersistenceMechanismException, MediaNotFoundException {
			mediaAccessor.deleteSingleMediaFromRMS(mediaName, storeName);
	}
	
	/**
	 *  Reset the image data for the application. This is a wrapper to the ImageAccessor.resetImageRecordStore
	 *  method. It is mainly used for testing purposes, to reset device data to the default album and photos.
	 * @throws PersistenceMechanismException 
	 * @throws InvalidMediaDataException 
	 */
	public void resetMediaData() throws PersistenceMechanismException {
		try {
			mediaAccessor.resetRecordStore();
		} catch (InvalidMediaDataException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param imageName
	 * @return
	 * @throws MediaNotFoundException
	 */
	public MediaData getMediaInfo(String imageName) throws MediaNotFoundException {
		return mediaAccessor.getMediaInfo(imageName);
	}
	
	/**
	 * @param recordName
	 * @return
	 * @throws PersistenceMechanismException
	 * @throws InvalidMediaDataException
	 */
	public MediaData[] loadMediaDataFromRMS(String recordName) throws PersistenceMechanismException, InvalidMediaDataException {
		return mediaAccessor.loadMediaDataFromRMS(recordName);
	}
	
	/**
	 * @param oldData
	 * @param newData
	 * @return
	 * @throws InvalidMediaDataException
	 * @throws PersistenceMechanismException
	 */
	public boolean updateMediaInfo(MediaData oldData, MediaData newData) throws InvalidMediaDataException, PersistenceMechanismException {
		return mediaAccessor.updateMediaInfo(oldData, newData);
	}

	/**
	 * @param recordName
	 * @param recordId
	 * @return
	 * @throws PersistenceMechanismException
	 */
	public byte[] loadMediaBytesFromRMS(String recordName, int recordId) throws PersistenceMechanismException {
		return mediaAccessor.loadMediaBytesFromRMS(recordName, recordId);
	}
	
	// #ifdef includeVideo
	/**
	 * @param videoname
	 * @param albumname
	 * @param video
	 * @throws InvalidMediaDataException
	 * @throws PersistenceMechanismException
	 */
	public void addVideoData(String videoname, String albumname, byte[] video)
		throws InvalidMediaDataException, PersistenceMechanismException {
		if (mediaAccessor instanceof VideoMediaAccessor)
			((VideoMediaAccessor)mediaAccessor).addVideoData(videoname, albumname, video);
	}
	//#endif
	
	// #if includeSmsFeature || capturePhoto
	/* [NC] Added in scenario 06 */
	/**
	 * @param photoname
	 * @param imgdata
	 * @param albumname
	 * @throws InvalidMediaDataException
	 * @throws PersistenceMechanismException
	 */
	public void addImageData(String photoname, byte[] imgdata, String albumname)
		throws InvalidMediaDataException, PersistenceMechanismException {
		if (mediaAccessor instanceof ImageMediaAccessor)
			((ImageMediaAccessor)mediaAccessor).addImageData(photoname, imgdata, albumname);
	}
	//#endif
}