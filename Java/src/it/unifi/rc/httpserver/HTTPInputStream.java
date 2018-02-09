/**
 * 
 */
package it.unifi.rc.httpserver;

import java.io.IOException;
import java.io.InputStream;

/**
 * A class that reads {@link HTTPRequest}/{@link HTTPReply} from a given {@link InputStream}.
 * 
 * @author loreti
 *
 */
public abstract class HTTPInputStream {

	/**
	 * Creates a new instance that uses <code>is</code>.
	 * 
	 * @param is {@link InputStream} used to retrieves data.
	 */
	public HTTPInputStream( InputStream is ) {
		this.setInputStream(is);
	}
	
	/**
	 * Set internal data structures to refer to a given InputStream.
	 * 
	 * @param is <code>InputStream</code> used to retrieves data.
	 */
	protected abstract void setInputStream( InputStream is );
	
	/**
	 * Reads next HTTPRequest from the inner {@link InputStream}.
	 * 
	 * @return a HTTPRequests reqd from the inner {@link InputStream}.
	 */
	public abstract HTTPRequest readHttpRequest( ) throws HTTPProtocolException;
	
	/**
	 * Reads next HTTPReply from the inner {@link InputStream}.
	 * 
	 * @return a HTTPReply reqd from the inner {@link InputStream}.
	 */
	public abstract HTTPReply readHttpReply( ) throws HTTPProtocolException;
	
	/**
	 * Closes this input stream and releases any system resources associated with the stream. 
	 * 
	 * @throws IOException if an error occurs.
	 */
	public abstract void close() throws IOException;
	
}
