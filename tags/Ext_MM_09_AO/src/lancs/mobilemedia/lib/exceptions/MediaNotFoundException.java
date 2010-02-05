package lancs.mobilemedia.lib.exceptions;

public class MediaNotFoundException extends Exception {
	
	private Throwable cause;
	
	public MediaNotFoundException() {
	}

	public MediaNotFoundException(String arg0) {
		super(arg0);
	}
	
	public MediaNotFoundException(Throwable arg0) {
		cause = arg0;
	}
	
	public Throwable getCause(){
		return cause;
	}
}
