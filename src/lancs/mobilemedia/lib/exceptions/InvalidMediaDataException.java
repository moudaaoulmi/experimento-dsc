package lancs.mobilemedia.lib.exceptions;

public class InvalidMediaDataException extends Exception {

	private Throwable cause;
	
	public InvalidMediaDataException() {
		super();
	}

	public InvalidMediaDataException(String arg0) {
		super(arg0);
	}
	
	public InvalidMediaDataException(Throwable arg0) {
		cause = arg0;
	}
	
	public Throwable getCause(){
		return cause;
	}
}
