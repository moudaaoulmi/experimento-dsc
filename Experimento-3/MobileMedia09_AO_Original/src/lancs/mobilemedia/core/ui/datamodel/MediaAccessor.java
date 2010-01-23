/*
 * Created on Sep 13, 2004
 *
 */
package lancs.mobilemedia.core.ui.datamodel;

import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

import lancs.mobilemedia.lib.exceptions.MediaNotFoundException;
import lancs.mobilemedia.lib.exceptions.MediaPathNotValidException;
import lancs.mobilemedia.lib.exceptions.InvalidArrayFormatException;
import lancs.mobilemedia.lib.exceptions.InvalidMediaDataException;
import lancs.mobilemedia.lib.exceptions.InvalidMediaFormatException;
import lancs.mobilemedia.lib.exceptions.InvalidAlbumNameException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;

/**
 * @author trevor
 * 
 * This is the main data access class. It handles all the connectivity with the
 * RMS record stores to fetch and save data associated with MobileMedia TODO:
 * Refactor into stable interface for future updates. We may want to access data
 * from RMS, or eventually direct from the 'file system' on devices that support
 * the FileConnection optional API.
 * 
 */
public abstract class MediaAccessor {

	// Note: Our midlet only ever has access to Record Stores it created
	// For now, use naming convention to create record stores used by
	// MobileMedia
	public String album_label; // "mpa- all album names
								// are prefixed with
								// this label
	public String info_label; // "mpi- all album info
								// stores are prefixed with
								// this label
	public String default_album_name;
	//imageInfo holds image metadata like label, album name and 'foreign key' index to
	// corresponding RMS entry that stores the actual Image object
	protected Hashtable mediaInfoTable = new Hashtable();

	protected String[] albumNames; // User defined names of photo albums

	// Record Stores
	public RecordStore mediaRS = null;
	public RecordStore mediaInfoRS = null;

	/*
	 * Constructor
	 */
	public MediaAccessor(String album_label, String info_label, String default_album_name) {
		this.album_label = album_label; 
		this.info_label = info_label; 
		this.default_album_name = default_album_name; 
	}

	/**
	 * Load all existing photo albums that are defined in the record store.
	 * 
	 * @throws InvalidMediaDataException
	 * @throws PersistenceMechanismException
	 */
	public void loadAlbums() throws InvalidMediaDataException,
			PersistenceMechanismException {
		// Try to find any existing Albums (record stores)
		String[] currentStores = RecordStore.listRecordStores();
		if (currentStores != null) {
			System.out.println("MediaAccessor::loadAlbums: Found: " + currentStores.length + " existing record stores");
			String[] temp = new String[currentStores.length];
			int count = 0;
			// Only use record stores that follow the naming convention defined
			for (int i = 0; i < currentStores.length; i++) {
				String curr = currentStores[i];
				System.out.println("MediaAccessor::loadAlbums: Current store "+curr+" = "+album_label);
				// If this record store is a photo album...
				if (curr.startsWith(album_label)) {
					// Strip out the mpa- identifier
					curr = curr.substring(4);
					// Add the album name to the array
					temp[i] = curr;
					count++;
				}
			}
			// Re-copy the contents into a smaller array now that we know the
			// size
			albumNames = new String[count];
			int count2 = 0;
			for (int i = 0; i < temp.length; i++) {
				if (temp[i] != null) {
					albumNames[count2] = temp[i];
					count2++;
				}
			}
		} else {
			System.out.println("MediaAccessor::loadAlbums: 0 record stores exist. Creating default one.");
			resetRecordStore();
			loadAlbums();
		}
	}
	
	/**
	 * Reset the album data for MobileMedia. This will delete all existing photo
	 * data from the record store and re-create the default album and photos.
	 * 
	 * @throws InvalidMediaFormatException
	 * @throws MediaPathNotValidException
	 * @throws InvalidMediaDataException
	 * @throws PersistenceMechanismException
	 * 
	 */
	protected abstract void resetRecordStore()
			throws InvalidMediaDataException, PersistenceMechanismException;

	public byte[] getByteFromMediaInfo(MediaData ii)
			throws InvalidMediaDataException {
		// [EF] Stub to avoid compilation error  
		return null;
	}

	protected abstract byte[] getMediaArrayOfByte(String path)
			throws MediaPathNotValidException, InvalidMediaFormatException;

	protected abstract MediaData getMediaFromBytes(byte[] data)
			throws InvalidArrayFormatException;


	public void addMediaData(String photoname, String path, String albumname)
			throws InvalidMediaDataException, PersistenceMechanismException {
		byte[] data1 = getMediaArrayOfByte(path);
		addMediaArrayOfBytes(photoname, albumname, data1);
	}

	protected void addMediaArrayOfBytes(String photoname, String albumname,
			byte[] data1) throws RecordStoreException,
			RecordStoreFullException, RecordStoreNotFoundException,
			RecordStoreNotOpenException, InvalidMediaDataException {
		mediaRS = RecordStore.openRecordStore(album_label + albumname, true);
		mediaInfoRS = RecordStore.openRecordStore(info_label + albumname, true);

		int rid; // new record ID for Image (bytes)
		int rid2; // new record ID for ImageData (metadata)
		rid = mediaRS.addRecord(data1, 0, data1.length);
		MediaData ii = new MediaData(rid,
				album_label + albumname, photoname);
		rid2 = mediaInfoRS.getNextRecordID();
		ii.setRecordId(rid2);
		data1 = getByteFromMediaInfo(ii);
		mediaInfoRS.addRecord(data1, 0, data1.length);
		mediaRS.closeRecordStore();
		mediaInfoRS.closeRecordStore();
	}

	/**
	 * This will populate the imageInfo hashtable with the ImageInfo object,
	 * referenced by label name and populate the imageTable hashtable with Image
	 * objects referenced by the RMS record Id
	 * 
	 * @throws PersistenceMechanismException
	 */
	public MediaData[] loadMediaDataFromRMS(String recordName)
			throws PersistenceMechanismException, InvalidMediaDataException {
		Vector mediaVector = new Vector();
		String infoStoreName = info_label + recordName;

		RecordStore infoStore = RecordStore.openRecordStore(infoStoreName, false);
		RecordEnumeration isEnum = infoStore.enumerateRecords(null, null, false);

		while (isEnum.hasNextElement()) {
			// Get next record
			int currentId = isEnum.nextRecordId();
			byte[] data = infoStore.getRecord(currentId);

			// Convert the data from a byte array into our ImageData
			// (metadata) object
			MediaData iiObject = getMediaFromBytes(data);

			// Add the info to the metadata hashtable
			String label = iiObject.getMediaLabel();
			mediaVector.addElement(iiObject);
			getMediaInfoTable().put(label, iiObject);
		}
		infoStore.closeRecordStore();

		// Re-copy the contents into a smaller array
		MediaData[] labelArray = new MediaData[mediaVector.size()];
		mediaVector.copyInto(labelArray);
		return labelArray;
	}

	/**
	 * Update the Image metadata associated with this named photo
	 * @throws InvalidMediaDataException 
	 * @throws PersistenceMechanismException 
	 */
	public boolean updateMediaInfo(MediaData oldData, MediaData newData) throws InvalidMediaDataException, PersistenceMechanismException {
		// Parse the Data store name to get the Info store name
		String infoStoreName = oldData.getParentAlbumName();
		infoStoreName = info_label + infoStoreName.substring(album_label.length());
		RecordStore infoStore = RecordStore.openRecordStore(infoStoreName, false);
		byte[] mediaDataBytes = getByteFromMediaInfo(newData);

		int recordId = oldData.getRecordId();
		int recordLength = mediaDataBytes.length;
		infoStore.setRecord(recordId, mediaDataBytes, 0, recordLength);
		// Update the Hashtable 'cache'
		setMediaInfo(oldData.getMediaLabel(), newData);
		infoStore.closeRecordStore();
		return false;
	}

	/**
	 * Retrieve the metadata associated with a specified image (by name)
	 * 
	 * @throws MediaNotFoundException
	 */
	public MediaData getMediaInfo(String imageName) throws MediaNotFoundException {
		MediaData ii = (MediaData) getMediaInfoTable().get(imageName);
		if (ii == null)
			throw new MediaNotFoundException(imageName +" was NULL in ImageAccessor Hashtable.");
		return ii;
	}

	/**
	 * Update the hashtable with new ImageInfo data
	 */
	public void setMediaInfo(String mediaName, MediaData newData) {
		getMediaInfoTable().put(newData.getMediaLabel(), newData);
	}

	/**
	 * Get the data for an Image as a byte array. This is useful for sending
	 * images via SMS or HTTP
	 * @throws PersistenceMechanismException 
	 */
	public byte[] loadMediaBytesFromRMS(String recordName,
			int recordId) throws PersistenceMechanismException {
		byte[] mediaData = null;
		RecordStore albumStore = RecordStore.openRecordStore(recordName, false);
		mediaData = albumStore.getRecord(recordId);
		albumStore.closeRecordStore();
		return mediaData;
	}

	/**
	 * Delete a single (specified) image from the (specified) record store. This
	 * will permanently delete the image data and metadata from the device.
	 * @throws PersistenceMechanismException 
	 * @throws MediaNotFoundException 
	 */
	public boolean deleteSingleMediaFromRMS(String storeName, String mediaName) throws PersistenceMechanismException, MediaNotFoundException {
		boolean success = false;
		// Open the record stores containing the byte data and the meta data
		// Verify storeName is name without pre-fix
		mediaRS = RecordStore.openRecordStore(album_label + storeName, true);
		mediaInfoRS = RecordStore.openRecordStore(info_label + storeName, true);

		MediaData mediaData = getMediaInfo(mediaName);
		int rid = mediaData.getForeignRecordId();

		mediaRS.deleteRecord(rid);
		mediaInfoRS.deleteRecord(rid);

		mediaRS.closeRecordStore();
		mediaInfoRS.closeRecordStore();

		// TODO: It's not clear from the API whether the record store needs to
		// be closed or not...
		return success;
	}

	/**
	 * Define a new photo album for mobile photo users. This creates a new
	 * record store to store photos for the album.
	 * @throws PersistenceMechanismException 
	 * @throws InvalidAlbumNameException 
	 */
	public void createNewAlbum(String albumName) throws PersistenceMechanismException, InvalidAlbumNameException {
		RecordStore newAlbumRS = null;
		RecordStore newAlbumInfoRS = null;
		if (albumName.equals("")){
			throw new InvalidAlbumNameException();
		}
		String[] names  = getAlbumNames();
		for (int i = 0; i < names.length; i++) {
			if (names[i].equals(albumName))
				throw new InvalidAlbumNameException();
		}
		newAlbumRS = RecordStore.openRecordStore(album_label + albumName, true);
		newAlbumInfoRS = RecordStore.openRecordStore(info_label + albumName, true);
		newAlbumRS.closeRecordStore();
		newAlbumInfoRS.closeRecordStore();
	}

	public void deleteAlbum(String albumName) throws PersistenceMechanismException {
		RecordStore.deleteRecordStore(album_label + albumName);
		RecordStore.deleteRecordStore(info_label + albumName);
	}

	/**
	 * Get the list of photo album names currently loaded.
	 * 
	 * @return Returns the albumNames.
	 */
	public String[] getAlbumNames() {
		return albumNames;
	}

	/**
	 * Get the hashtable that stores the image metadata in memory.
	 * @return Returns the imageInfoTable.
	 */
	public Hashtable getMediaInfoTable() {
		return mediaInfoTable;
	}

	/**
	 * Update the hashtable that stores the image metadata in memory
	 * @param imageInfoTable
	 *            The imageInfoTable to set.
	 */
	public void setMediaInfoTable(Hashtable mediaInfoTable) {
		this.mediaInfoTable = mediaInfoTable;
	}
	
	protected void removeRecords() {
		String storeName = null;
		String infoStoreName = null;

		// remove any existing album stores...
		if (albumNames != null) {
			for (int i = 0; i < albumNames.length; i++) {
				try {
					// Delete all existing stores containing Image objects as
					// well as the associated ImageInfo objects
					// Add the prefixes labels to the info store
					storeName = album_label + albumNames[i];
					infoStoreName = info_label + albumNames[i];
					RecordStore.deleteRecordStore(storeName);
					RecordStore.deleteRecordStore(infoStoreName);
				} catch (RecordStoreException e) {
					System.out.println("No record store named " + storeName + " to delete.");
					System.out.println("...or...No record store named " + infoStoreName + " to delete.");
					System.out.println("Ignoring Exception: " + e);
					// ignore any errors...
				}
			}
		} else {
			// Do nothing for now
			System.out.println("ImageAccessor::resetVideoRecordStore: albumNames array was null. Nothing to delete.");
		}
	}
}