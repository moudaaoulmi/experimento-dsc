/*
 * Lancaster University
 * Computing Department
 * 
 * Created by Eduardo Figueiredo
 * Date: 14 Jun 2008
 * 
 */
package lancs.mobilemedia.alternative.musicvideo;

import lancs.mobilemedia.core.ui.datamodel.MediaData;

/**
 * @author Eduardo Figueiredo
 *
 */
public aspect MusicOrVideo {

	public static String MediaData.PHOTO = "image/png";
	public static String MediaData.MUSIC = "audio/x-wav";
	public static String MediaData.VIDEO = "video/mpeg";
	
	private String MediaData.typemedia;
	
	public String MediaData.getTypeMedia() {
		return typemedia;
	}
	
	public void MediaData.setTypeMedia(String type) {
		this.typemedia = type;
	}
}
