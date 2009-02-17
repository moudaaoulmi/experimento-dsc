package healthwatcher.view.command;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * A generic response to a command
 */
public interface CommandResponse {
	
	public PrintWriter getWriter() throws IOException;
}
