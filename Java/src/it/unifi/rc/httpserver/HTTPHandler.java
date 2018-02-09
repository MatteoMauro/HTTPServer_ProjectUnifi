/**
 * 
 */
package it.unifi.rc.httpserver;

/**
 * A generic class that is able to handle HTTP requests.
 * 
 * 
 * @author loreti
 *
 */
public interface HTTPHandler {
	
	/**
	 * Handle a given HTTPRequest and, if this is the right handler, returns a HTTPReply. If this
	 * handler is not able to manage the request, value null is returned.  
	 * 
	 * @param request a 
	 * 
	 * @return a HTTPReply that is null if this handler cannot manage the request.
	 */
	public HTTPReply handle(HTTPRequest request);

}
