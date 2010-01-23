package lancs.mobilemedia.alternative.video;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.rms.RecordStoreException;

import lancs.mobilemedia.alternative.musicvideo.MultiMediaAccessor;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.datamodel.MediaData;
import lancs.mobilemedia.lib.exceptions.MediaNotFoundException;
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
		try {
			video = inputStreamToBytes(is);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		addVideoData("Fish", default_album_name, video);
		loadMediaDataFromRMS(default_album_name);

		try {
			media = this.getMediaInfo("Fish");
			media.setTypeMedia("video/mpeg");
			this.updateMediaInfo(media, media);
		} catch (MediaNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void addVideoData(String videoname, String albumname, byte[] video)
		throws InvalidMediaDataException, PersistenceMechanismException {
		try {
			addMediaArrayOfBytes(videoname, albumname, video);
		} catch (RecordStoreException e) {
			throw new PersistenceMechanismException();
		}
	}
	
	public byte[] inputStreamToBytes(InputStream inputStream) throws IOException {
		String str = inputStream.toString();
		return str.getBytes();
	}
}