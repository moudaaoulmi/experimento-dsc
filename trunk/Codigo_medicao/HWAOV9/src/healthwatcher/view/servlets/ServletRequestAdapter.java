package healthwatcher.view.servlets;

import healthwatcher.view.command.CommandRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * TODO - describe this file
 * 
 */
public class ServletRequestAdapter implements CommandRequest {

	protected HttpServletRequest request;
	
	public ServletRequestAdapter(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * Returns a parameter for the request with this key
	 * 
	 * @param key the parameter key
	 * @return the value for this key as a string
	 */
	public String getInput(String key) {
		return request.getParameter(key);
	}
	
	/**
	 * Stores this value using this key in some sort of storage that
	 * persists between commands. Just works if it is authorized.
	 * 
	 * @param key the key used to store the value
	 * @param value the value to be stored
	 */
	public void put(String key, Object value) {
		if (this.isAuthorized()) {
			request.getSession(true).putValue(key, value);
		}
	}
	
	/**
	 * Retrieves a pre-stored value using this key. Returns the stored
	 * value or null if no value was stored with this key
	 * 
	 * @param key the key to the value
	 * @return the value or null if not found
	 */
	public Object get(String key) {
		if (this.isAuthorized()) {
			return request.getSession(false).getValue(key);
		}
		return null;
	}
	
	/**
	 * Resets the persistent storage of values and the authorized flag
	 */
	public void reset() {
		request.getSession(true).invalidate();
	}
	
	/**
	 * Sets the value of the authorized flag
	 * 
	 * @param authorized the new authorized flag
	 */
	public void setAuthorized(boolean authorized) {
		if (authorized) {
			request.getSession(true);
		} else {
			reset();
		}
	}
	
	/**
	 * Retrieves the value of the authorized flag
	 * 
	 * @return the authorized flag
	 */
	public boolean isAuthorized() {
		return request.getSession(false) != null;
	}
}
