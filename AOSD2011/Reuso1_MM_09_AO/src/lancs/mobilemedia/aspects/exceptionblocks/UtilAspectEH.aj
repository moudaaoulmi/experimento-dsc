package lancs.mobilemedia.aspects.exceptionblocks;

import java.io.IOException;
import java.io.InputStream;

import lancs.mobilemedia.core.ui.datamodel.MediaData;
import lancs.mobilemedia.core.util.MediaUtil;
import lancs.mobilemedia.lib.exceptions.MediaPathNotValidException;
import lancs.mobilemedia.lib.exceptions.InvalidArrayFormatException;
import lancs.mobilemedia.lib.exceptions.InvalidMediaDataException;
import lancs.mobilemedia.lib.exceptions.InvalidMediaFormatException;

public aspect UtilAspectEH {
	
	//Method ImageUtil.readImageAsByteArray 1- block - Scenario 3
	pointcut readMediaAsByteArray(String mediaFile): 
		 (call(public void Class.getResourceAsStream(String))&&(args(mediaFile)))&& withincode(public byte[] MediaUtil.readMediaAsByteArray(String)) ;
	pointcut readInternalMediaAsByteArray(String mediaFile): 
		 call(private byte[] MediaUtil.internalReadMediaAsByteArray(byte[],InputStream,int, byte[]))&& (withincode(public byte[] MediaUtil.readMediaAsByteArray(String))&&(args(mediaFile)));
	pointcut getMediaInfoFromBytes(): 
		 execution(public MediaData MediaUtil.getMediaInfoFromBytes(byte[]));
	pointcut getBytesFromMediaInfo(): 
		 execution(public String MediaUtil.getBytesFromMediaInfo(MediaData));
	
	declare soft: IOException: call(private byte[] MediaUtil.internalReadMediaAsByteArray(byte[],InputStream,int, byte[]))&& (withincode(public byte[] MediaUtil.readMediaAsByteArray(String)));
	
	void around(String mediaFile) throws  MediaPathNotValidException: readMediaAsByteArray(mediaFile){
		try {
			proceed(mediaFile);
		} catch(Exception e) {
			throw new MediaPathNotValidException("Path not valid for this image: "+mediaFile);
		}
	}
	
	byte[] around(String mediaFile) throws  InvalidMediaFormatException, MediaPathNotValidException: readInternalMediaAsByteArray(mediaFile){
		try {
			return proceed(mediaFile);
		} catch(Exception e) {
			if (e instanceof IOException){
				throw new InvalidMediaFormatException(
						"The file "+mediaFile+" does not have PNG format");
			}else if (e instanceof NullPointerException){
				throw new MediaPathNotValidException(
						"Path not valid for this image:"+mediaFile);
			}
		}
		return null;
	}
	
	MediaData around() throws  InvalidArrayFormatException: getMediaInfoFromBytes(){
		try {
			return proceed();
		} catch(Exception e) {
			throw new InvalidArrayFormatException();
		}
	}
	
	String around() throws  InvalidMediaDataException: getBytesFromMediaInfo(){
		try {
			return proceed();
		} catch(Exception e) {
			throw new InvalidMediaDataException("The provided data are not valid");
		}
	}
}