package lancs.mobilemedia.alternative.video;

import java.io.InputStream;
import lancs.mobilemedia.alternative.musicvideo.MultiMediaAccessor;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.datamodel.MediaData;
import lancs.mobilemedia.lib.exceptions.InvalidMediaDataException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;

public class VideoMediaAccessor  extends MultiMediaAccessor {
	
	public VideoMediaAccessor(AlbumData mod) {
		super("vvp-","vvpi-","My Video Album");
	}
	
	public void resetRecordStore() throws InvalidMediaDataException, PersistenceMechanismException {
		removeRecords();
		
		// Now, create a new default album for testing
		//	addVideoData("Fish", default_album_name, this.getClass().getResourceAsStream(name))
		
		MediaData media = null;
		InputStream is = (InputStream) this.getClass().getResourceAsStream("/images/fish.mpg");
		byte[] video = null;
		
		// this line had the exception handling aspectized
		video = inputStreamToBytes(is);
		
		addVideoData("Fish", default_album_name, video);
		loadMediaDataFromRMS(default_album_name);

		// this line had the exception handling aspectized
		media = internalResetRecordStore(media); 
	}

	private MediaData internalResetRecordStore(MediaData media) throws InvalidMediaDataException, PersistenceMechanismException {
		media = this.getMediaInfo("Fish");
		media.setTypeMedia("video/mpeg");
		this.updateMediaInfo(media, media);
		return media;
	}
	
	public void addVideoData(String videoname, String albumname, byte[] video) throws InvalidMediaDataException, PersistenceMechanismException {
		addMediaArrayOfBytes(videoname, albumname, video);
	}
	
	public byte[] inputStreamToBytes(InputStream inputStream){
		String str = inputStream.toString();
		return str.getBytes();
	}
}