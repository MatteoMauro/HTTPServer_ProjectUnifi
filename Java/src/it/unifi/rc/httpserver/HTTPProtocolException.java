/**
 * 
 */
package it.unifi.rc.httpserver;

import java.io.IOException;

/**
 * This exception is generated when an error occur in the HTTP interaction.
 * 
 * @author loreti
 *
 */
public class HTTPProtocolException extends IOException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3709052420371798241L;

	/**
	 * Constructs a HTTPProtocolException with the specified detail message.
	 * 
	 * @param message
	 */
	public HTTPProtocolException(String message) {
		super(message);
	}

	/**
	 * Constructs a HTTPProtocolException with null as its error detail message.
	 */
	public HTTPProtocolException() {
		super();
	}

	/**
	 * Constructs an IOException with the specified cause. This constructor is 
	 * useful for exceptions that are little more than wrappers for other throwables.
	 * 
	 * @param cause the cause of the exception.
	 */
	public HTTPProtocolException(Throwable cause) {
		super(cause);
	}
	
	
	
	

}
