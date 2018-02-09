/**
 * 
 */
package it.unifi.rc.httpserver;

import java.util.Map;

/**
 * An interface descriging HTTP Requests.
 * 
 * @author loreti
 *
 */
public interface HTTPReply {
	
	/**
	 * HTTP version in status line (can return either "HTTP/1.0" or "HTTP/1.1").
	 * 
	 * @return HTTP version in status line.
	 */
	public String getVersion();
	
	
	/**
	 * HTTP Status Code.
	 * 
	 * @return HTTP Status Code.
	 */
	public String getStatusCode();
	
	/**
	 * HTTP Status Message.
	 * 
	 * @return HTTP Status Message.
	 */
	public String getStatusMessage();
	
	
	/**
	 * Reply data.
	 * 
	 * @return Reply data.
	 */
	public String getData();
	
	/**
	 * Return a Map that associates each key in the header lines with the corresponding value.
	 * 
	 * @return
	 */
	public Map<String,String> getParameters();

}
