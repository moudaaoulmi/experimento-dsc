// #if includeMMAPI || includeVideo
// [NC] Added in the scenario 07
package lancs.mobilemedia.core.ui.datamodel;

import lancs.mobilemedia.core.util.MultiMediaUtil;
import lancs.mobilemedia.lib.exceptions.MediaNotFoundException;
import lancs.mobilemedia.lib.exceptions.MediaPathNotValidException;
import lancs.mobilemedia.lib.exceptions.InvalidArrayFormatException;
import lancs.mobilemedia.lib.exceptions.InvalidMediaDataException;
import lancs.mobilemedia.lib.exceptions.InvalidMediaFormatException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;

public class MultiMediaAccessor extends MediaAccessor {

	private MultiMediaUtil converter = new MultiMediaUtil();

	public MultiMediaAccessor(AlbumData mod) {
		super("mmp-","mmpi-","My Music Album");
	}
	
	// #ifdef includeVideo
	// [NC] Added in the scenario 08
	public MultiMediaAccessor(AlbumData mod, String album_label, String info_label, String default_album_name) {
		super(album_label,info_label,default_album_name);
	}
	//#endif

	protected  byte[] getMediaArrayOfByte(String path)	throws MediaPathNotValidException, InvalidMediaFormatException {
		byte[] data1 = converter.readMediaAsByteArray(path);
		return data1;
	}
	
	protected byte[] getByteFromMediaInfo(MediaData ii) throws InvalidMediaDataException {
			return converter.getBytesFromMediaInfo(ii);
	}
	
	protected MediaData getMediaFromBytes(byte[] data) throws InvalidArrayFormatException {
		MediaData iiObject = converter.getMultiMediaInfoFromBytes(data);
		return iiObject;
	}

	public void resetRecordStore() throws InvalidMediaDataException, PersistenceMechanismException {
		removeRecords();
		
		MediaData media = null;
//		MultiMediaData mmedi = null;

		addMediaData("Applause", "/images/applause.wav", default_album_name);
		addMediaData("Baby", "/images/baby.wav", default_album_name);
		addMediaData("Bong", "/images/bong.wav", default_album_name);
//		addMediaData("Frogs", "/images/frogs.mp3", default_album_name);
		addMediaData("Jump", "/images/jump.wav", default_album_name);
		addMediaData("Printer", "/images/printer.wav", default_album_name);
//		addMediaData("Tango", "/images/cabeza.mid", default_album_name);
	
		loadMediaDataFromRMS(default_album_name);
		try {
			media = this.getMediaInfo("Applause");
			media.setTypeMedia("audio/x-wav");
			this.updateMediaInfo(media, media);

			media = this.getMediaInfo("Baby");
			media.setTypeMedia("audio/x-wav");
//			mmedi = new MultiMediaData(media, "audio/x-wav");
			this.updateMediaInfo(media, media);

			media = this.getMediaInfo("Bong");
			media.setTypeMedia("audio/x-wav");
//			mmedi = new MultiMediaData(media, "audio/x-wav");
			this.updateMediaInfo(media, media);

//			media = this.getMediaInfo("Frogs");
//			mmedi = new MultiMediaData(media, "audio/mpeg");
//			this.updateMediaInfo(media, media);

			media = this.getMediaInfo("Jump");
			media.setTypeMedia("audio/x-wav");
//			mmedi = new MultiMediaData(media, "audio/x-wav");
			this.updateMediaInfo(media, media);

			media = this.getMediaInfo("Printer");
			media.setTypeMedia("audio/x-wav");
//			mmedi = new MultiMediaData(media, "audio/x-wav");
			this.updateMediaInfo(media, media);
			
//			media = this.getMediaInfo("Tango");
//			mmedi = new MultiMediaData(media, "audio/midi");
//			this.updateMediaInfo(media, media);

		} catch (MediaNotFoundException e) {
			e.printStackTrace();
		}
	}
}
//#endif
