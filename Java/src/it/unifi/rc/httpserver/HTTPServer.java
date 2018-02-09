/**
 * 
 */
package it.unifi.rc.httpserver;

import java.io.IOException;

/**
 * A HTTP Server. 
 * 
 * @author loreti
 *
 */
public interface HTTPServer {
	
	/**
	 * Adds a new handler in the server. Each server has a list of {@link HTTPHandler}. A request is
	 * handled by the first handler that is able to handle it.  
	 * 
	 * @param handler an {@link HTTPHandler}.
	 */
	public void addHandler( HTTPHandler handler );
	
	/**
	 * Start the server.
	 */
	public void start() throws IOException;
	
	/**
	 * Stops the server.
	 */
	public void stop();

}
