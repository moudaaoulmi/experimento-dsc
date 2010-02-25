package lancs.mobilemedia.optional.smsorcapturephotoorvideo;

import lancs.mobilemedia.core.ui.screens.AddMediaToAlbum;

public aspect SmsOrCapturePhotoOrVideo {
	
	 private byte[] AddMediaToAlbum.CapturedMedia = null;
	 
	 public byte[] AddMediaToAlbum.getCapturedMedia() {
			return CapturedMedia;
		}

	public void AddMediaToAlbum.setCapturedMedia(byte[] capturedMedia) {
			CapturedMedia = capturedMedia;
	}
}
