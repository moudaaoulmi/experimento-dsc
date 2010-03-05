// [NC] Added in the scenario 07
package lancs.mobilemedia.alternative.musicvideo;

import lancs.mobilemedia.core.ui.datamodel.MediaAccessor;
import lancs.mobilemedia.core.ui.datamodel.MediaData;
import lancs.mobilemedia.lib.exceptions.MediaPathNotValidException;
import lancs.mobilemedia.lib.exceptions.InvalidArrayFormatException;
import lancs.mobilemedia.lib.exceptions.InvalidMediaDataException;
import lancs.mobilemedia.lib.exceptions.InvalidMediaFormatException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;

public class MultiMediaAccessor extends MediaAccessor {

	private MultiMediaUtil converter = new MultiMediaUtil();
	
	public MultiMediaAccessor() {
		super("mmp-","mmpi-","My Music Album");
	}
	
	public MultiMediaAccessor(String album_label, String info_label, String default_album_name) {
		super(album_label,info_label,default_album_name);
	}
	
	protected  byte[] getMediaArrayOfByte(String path)	throws MediaPathNotValidException, InvalidMediaFormatException {
		byte[] data1 = converter.readMediaAsByteArray(path);
		return data1;
	}
	
	public byte[] getByteFromMediaInfo(MediaData ii) throws InvalidMediaDataException {
		return converter.getBytesFromMediaInfo(ii).getBytes();
	}
	
	protected MediaData getMediaFromBytes(byte[] data) throws InvalidArrayFormatException {
		MediaData iiObject = converter.getMultiMediaInfoFromBytes(data);
		return iiObject;
	}

	public void resetRecordStore() throws InvalidMediaDataException, PersistenceMechanismException {
		removeRecords();
		// Now, create a new default album for testing
		MediaData media = null;

		addMediaData("Applause", "/images/applause.wav", default_album_name);
		addMediaData("Baby", "/images/baby.wav", default_album_name);
		addMediaData("Bong", "/images/bong.wav", default_album_name);
//		addMediaData("Frogs", "/images/frogs.mp3", default_album_name);
		addMediaData("Jump", "/images/jump.wav", default_album_name);
		addMediaData("Printer", "/images/printer.wav", default_album_name);
//		addMediaData("Tango", "/images/cabeza.mid", default_album_name);

		loadMediaDataFromRMS(default_album_name);
		internalResetRecordStore(media); 
	}

	private void internalResetRecordStore(MediaData media) throws InvalidMediaDataException, PersistenceMechanismException {
		media = this.getMediaInfo("Applause");
		media.setTypeMedia("audio/x-wav");
		this.updateMediaInfo(media, media);
		
		media = this.getMediaInfo("Baby");
		media.setTypeMedia("audio/x-wav");
		this.updateMediaInfo(media, media);

		media = this.getMediaInfo("Bong");
		media.setTypeMedia("audio/x-wav");
		this.updateMediaInfo(media, media);

//			media = this.getMediaInfo("Frogs");
//			mmedi = new MediaData(media.getForeignRecordId(), media.getParentAlbumName(), media.getMediaLabel());
//			mmedi = new MultiMediaData(media, "audio/mpeg");
//			this.updateMediaInfo(media, mmedi);

		media = this.getMediaInfo("Jump");
		media.setTypeMedia("audio/x-wav");
		this.updateMediaInfo(media, media);

		media = this.getMediaInfo("Printer");
		media.setTypeMedia("audio/x-wav");
		this.updateMediaInfo(media, media);
		
//			media = this.getMediaInfo("Tango");
//			mmedi = new MediaData(media.getForeignRecordId(), media.getParentAlbumName(), media.getMediaLabel());
//			mmedi = new MultiMediaData(media, "audio/midi");
//			this.updateMediaInfo(media, mmedi);
	}
}
