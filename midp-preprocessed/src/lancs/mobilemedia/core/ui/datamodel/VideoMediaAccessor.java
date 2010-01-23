// #ifdef includeVideo
// [NC] Added in the scenario 08
package lancs.mobilemedia.core.ui.datamodel;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.rms.RecordStoreException;

import lancs.mobilemedia.lib.exceptions.MediaNotFoundException;
import lancs.mobilemedia.lib.exceptions.InvalidMediaDataException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;

public class VideoMediaAccessor  extends MultiMediaAccessor{
	
	public VideoMediaAccessor(AlbumData mod) {
		super(mod,"vvp-","vvpi-","My Video Album");
	}
	
	public void resetRecordStore() throws InvalidMediaDataException, PersistenceMechanismException {
		removeRecords();
		
		// Now, create a new default album for testing
		//	addVideoData("Fish", default_album_name, this.getClass().getResourceAsStream(name))
		
		MediaData media = null;
		MediaData mmedi = null;
		InputStream is = (InputStream) this.getClass().getResourceAsStream("/images/fish.mpg");
		byte[] video = null;
		try {
			video = inputStreamToBytes(is);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println("Vai adicionar os dados");		
		addVideoData("Fish", default_album_name, video);
		loadMediaDataFromRMS(default_album_name);

		try {
			media = this.getMediaInfo("Fish");
			mmedi = new MediaData(media.getForeignRecordId(), media.getParentAlbumName(), media.getMediaLabel());
			mmedi.setTypeMedia("video/mpeg");
//			mmedi = new MultiMediaData(media, "video/mpeg");
			this.updateMediaInfo(media, mmedi);
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
		String str=inputStream.toString();
		return str.getBytes();

	}
}

//#endif
