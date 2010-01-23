package lancs.mobilemedia.lib.exceptions;

public class MediaPathNotValidException extends InvalidMediaDataException {

	private Throwable cause;
	
	public MediaPathNotValidException() {
	}

	public MediaPathNotValidException(String arg0) {
		super(arg0);
	}

	public MediaPathNotValidException(Throwable arg0) {
		cause = arg0;
	}
	
	public Throwable getCause(){
		return cause;
	}
}
