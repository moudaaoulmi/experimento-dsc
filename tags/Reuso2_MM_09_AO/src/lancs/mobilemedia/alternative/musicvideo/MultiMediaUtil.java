// [NC] Added in the scenario 07
package lancs.mobilemedia.alternative.musicvideo;

import lancs.mobilemedia.core.ui.datamodel.MediaData;
import lancs.mobilemedia.core.util.MediaUtil;
import lancs.mobilemedia.lib.exceptions.InvalidArrayFormatException;
import lancs.mobilemedia.lib.exceptions.InvalidMediaDataException;

public class MultiMediaUtil extends MediaUtil {

	public String getBytesFromMediaInfo(MediaData ii) throws InvalidMediaDataException {
		String mediadata = super.getBytesFromMediaInfo(ii);
		if (ii.getTypeMedia() != null) {
			if ((ii.getTypeMedia().equals(MediaData.MUSIC)) || (ii.getTypeMedia().equals(MediaData.VIDEO))) {
				String byteString = new String(mediadata);
				byteString = byteString.concat(DELIMITER);
				String typeOfMedia = ii.getTypeMedia();
				byteString = byteString.concat(typeOfMedia);
				return byteString;
			}
		}
		return mediadata; 
	}

	public MediaData getMultiMediaInfoFromBytes(byte[] bytes)
			throws InvalidArrayFormatException {
		MediaData mediadata =  super.getMediaInfoFromBytes(bytes);
		String iiString = new String(bytes);
		int startIndex = endIndex + 1;
		if (endIndex==iiString.length())
			return mediadata;
		endIndex = iiString.indexOf(DELIMITER, startIndex);
		if (endIndex == -1)
			endIndex = iiString.length();
		String mediaType = iiString.substring(startIndex, endIndex);
		mediadata.setTypeMedia(mediaType);
		return mediadata;
	}
}
