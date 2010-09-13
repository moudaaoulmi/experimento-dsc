package lancs.mobilemedia.optional.smsorcapturephoto;

import javax.microedition.rms.RecordStoreException;

import lancs.mobilemedia.alternative.photo.ImageMediaAccessor;
import lancs.mobilemedia.alternative.photo.PhotoViewScreen;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.datamodel.MediaData;
import lancs.mobilemedia.core.ui.screens.AddMediaToAlbum;
import lancs.mobilemedia.lib.exceptions.InvalidMediaDataException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;
import lancs.mobilemedia.optional.copySMS.PhotoViewController;

public privileged aspect SmSOrCapturePhoto {

	// ********  AlbumData  ********* //
	
	public void AlbumData.addImageData(String photoname, byte[] imgdata, String albumname)
			throws InvalidMediaDataException, PersistenceMechanismException {
		if (mediaAccessor instanceof ImageMediaAccessor)
			((ImageMediaAccessor)mediaAccessor).addImageData(photoname, imgdata, albumname);
	}
	
	// ********  ImageMediaAccessor  ********* //
	
	public void ImageMediaAccessor.addImageData(String photoname, byte[] imgdata, String albumname)
			throws InvalidMediaDataException, PersistenceMechanismException {
		try {
			addMediaArrayOfBytes(photoname, albumname, imgdata);
		} catch (RecordStoreException e) {
			throw new PersistenceMechanismException();
		}
	}

	// ********  PhotoViewController  ********* //
	
	pointcut processCopy(PhotoViewController photoViewController, AddMediaToAlbum copyPhotoToAlbum):
		execution(private void PhotoViewController.processCopy(AddMediaToAlbum))
		&& this(photoViewController) && args(copyPhotoToAlbum);
	
	after(PhotoViewController photoViewController, AddMediaToAlbum copyPhotoToAlbum): processCopy(photoViewController, copyPhotoToAlbum) {
		if (((PhotoViewScreen)photoViewController.getCurrentScreen()).isFromSMS()){
			copyPhotoToAlbum.setCapturedMedia(((PhotoViewScreen)photoViewController.getCurrentScreen()).getImage());
		}
	}

	pointcut processImageData(PhotoViewController photoViewController, String photoName, String albumname):
		execution(private MediaData PhotoViewController.processImageData(String, String))
		&& this(photoViewController)
		&& args(photoName, albumname);
	
	MediaData around (PhotoViewController photoViewController, String photoName, String albumname) throws InvalidMediaDataException, PersistenceMechanismException :
		          processImageData(photoViewController,photoName, albumname) {
		MediaData imageData = null;
		byte[] imgByte= ((AddMediaToAlbum)photoViewController.getCurrentScreen()).getCapturedMedia();
		if (imgByte == null){
		   imageData = proceed(photoViewController, photoName, albumname);
		}else{
			photoViewController.getAlbumData().addImageData(photoName, imgByte, albumname);
		}		
		return imageData;
	}
}
