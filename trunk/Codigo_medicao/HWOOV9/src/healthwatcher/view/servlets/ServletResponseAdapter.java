package healthwatcher.view.servlets;

import healthwatcher.view.command.CommandResponse;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

/**
 * TODO - describe this file
 * 
 */
public class ServletResponseAdapter implements CommandResponse {

	protected HttpServletResponse response;

	public ServletResponseAdapter(HttpServletResponse response) {
		this.response = response;
		this.response.setContentType("text/html");
	}
	
	public PrintWriter getWriter() throws IOException {
		return this.response.getWriter();
	}
}
