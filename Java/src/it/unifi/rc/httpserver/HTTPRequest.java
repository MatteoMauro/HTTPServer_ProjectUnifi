/**
 * 
 */
package it.unifi.rc.httpserver;

import java.util.Map;

/**
 * An interface describing HTTP Requests.
 * 
 * @author loreti
 *
 */
public interface HTTPRequest {
	
	/**
	 * HTTP version in request line (can return either "HTTP/1.0" or "HTTP/1.1").
	 * 
	 * @return HTTP version in request line.
	 */
	public String getVersion();
	
	
	/**
	 * HTTP Method in request line.
	 * 
	 * @return HTTP Method in request line.
	 */
	public String getMethod();
	
	/**
	 * Path in request line.
	 * 
	 * @return Path in request line.
	 */
	public String getPath();
	
	
	/**
	 * Entity body.
	 * 
	 * @return Entity body.
	 */
	public String getEntityBody();
	
	/**
	 * Return a Map that associates each key in the header lines with the corresponding value.
	 * 
	 * @return
	 */
	public Map<String,String> getParameters();

}
