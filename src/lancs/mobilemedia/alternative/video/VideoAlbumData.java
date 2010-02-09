package lancs.mobilemedia.alternative.video;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.datamodel.MediaData;
import lancs.mobilemedia.lib.exceptions.MediaNotFoundException;
import lancs.mobilemedia.lib.exceptions.InvalidMediaDataException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;

public class VideoAlbumData extends AlbumData {
	
	public VideoAlbumData() {
		mediaAccessor = new VideoMediaAccessor(this);
	}
	
	public InputStream getVideoFromRecordStore(String recordStore, String musicName) throws MediaNotFoundException, PersistenceMechanismException {
		MediaData mediaInfo = null;
		mediaInfo = mediaAccessor.getMediaInfo(musicName);
		//Find the record ID and store name of the image to retrieve
		int mediaId = mediaInfo.getForeignRecordId();
		String album = mediaInfo.getParentAlbumName();
		//Now, load the image (on demand) from RMS and cache it in the hashtable
		byte[] musicData = (mediaAccessor).loadMediaBytesFromRMS(album, mediaId);
		return new ByteArrayInputStream(musicData);
	}
	
	public void addVideoData(String videoname, String albumname, byte[] video)
			throws InvalidMediaDataException, PersistenceMechanismException {
		((VideoMediaAccessor)mediaAccessor).addVideoData(videoname, albumname, video);
	}
}