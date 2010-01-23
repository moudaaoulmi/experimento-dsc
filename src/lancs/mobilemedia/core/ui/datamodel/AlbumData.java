/*
 * Created on Sep 28, 2004
 */
package lancs.mobilemedia.core.ui.datamodel;

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

	public MediaAccessor mediaAccessor;
	
	/**
	 *  Load any photo albums that are currently defined in the record store
	 */
	public String[] getAlbumNames() {
		//Shouldn't load all the albums each time
		//Add a check somewhere in ImageAccessor to see if they've been
		//loaded into memory already, and avoid the extra work...
		mediaAccessor.loadAlbums();
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
		result = mediaAccessor.loadMediaDataFromRMS(recordName);
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
	
	public void deleteAlbum(String albumName) throws PersistenceMechanismException{
		mediaAccessor.deleteAlbum(albumName);
	}


	public void addNewMediaToAlbum(String label, String path, String album) throws InvalidMediaDataException, PersistenceMechanismException{
		mediaAccessor.addMediaData(label, path, album);
	}

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
		mediaAccessor.resetRecordStore();
	}
	
	/**
	 * @param mediaName
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
}