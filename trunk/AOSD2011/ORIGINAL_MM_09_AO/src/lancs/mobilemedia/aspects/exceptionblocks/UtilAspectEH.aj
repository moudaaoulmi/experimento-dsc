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
	
	after(String mediaFile) throwing(Exception e) throws  MediaPathNotValidException: readMediaAsByteArray(mediaFile){
		throw new MediaPathNotValidException("Path not valid for this image: "+mediaFile);
	}
	
	//Method ImageUtil.readImageAsByteArray 2- block - Scenario 3
	pointcut readInternalMediaAsByteArray(String mediaFile): 
		 call(private byte[] MediaUtil.internalReadMediaAsByteArray(byte[],InputStream,int, byte[]))&& (withincode(public byte[] MediaUtil.readMediaAsByteArray(String))&&(args(mediaFile)));
	
	declare soft: IOException: call(private byte[] MediaUtil.internalReadMediaAsByteArray(byte[],InputStream,int, byte[]))&& (withincode(public byte[] MediaUtil.readMediaAsByteArray(String)));
	
	after(String mediaFile) throwing(Exception e) throws  InvalidMediaFormatException, MediaPathNotValidException: readInternalMediaAsByteArray(mediaFile){
		if (e instanceof IOException){
			throw new InvalidMediaFormatException(
					"The file "+mediaFile+" does not have PNG format");
		}else if (e instanceof NullPointerException){
			throw new MediaPathNotValidException(
					"Path not valid for this image:"+mediaFile);
		}
	}
	
	//Method public ImageData ImageUtil.getImageInfoFromBytes(byte[] bytes) 1- block - Scenario 1
	pointcut getMediaInfoFromBytes(): 
		 execution(public MediaData MediaUtil.getMediaInfoFromBytes(byte[]));
	
	after() throwing(Exception e) throws  InvalidArrayFormatException: getMediaInfoFromBytes(){
		throw new InvalidArrayFormatException();
	}
	
	//Method public byte[] ImageUtil.getBytesFromImageInfo(ImageData ii) 1- block - Scenario 1
	pointcut getBytesFromMediaInfo(): 
		 execution(public String MediaUtil.getBytesFromMediaInfo(MediaData));
	
	after() throwing(Exception e) throws  InvalidMediaDataException: getBytesFromMediaInfo(){
		throw new InvalidMediaDataException("The provided data are not valid");
	}
}