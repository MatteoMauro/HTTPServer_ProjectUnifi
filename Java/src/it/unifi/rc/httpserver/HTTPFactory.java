/**
 * 
 */
package it.unifi.rc.httpserver;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

/**
 * This interface provides the methods for creating of all the classes considered in the project.
 * 
 * @author loreti
 *
 */
public interface HTTPFactory {

	/**
	 * Given an InputStream returns an instance of HTTPInputStream that can be used to read HTTP
	 * requests/replies.
	 * 
	 * @param is an InputStream
	 * @return an instance of HTTPInputStream that can be used to read HTTP
	 * requests/replies.
	 */
	public HTTPInputStream getHTTPInputStream( InputStream is );
	
	/**
	 * Given an OutputStream returns an instance of HTTPOutputStream that can be used to write HTTP
	 * requests/replies.
	 * 
	 * @param os an OutputStream
	 * @return an instance of HTTPOutputStream that can be used to write HTTP
	 * requests/replies.
	 */
	public HTTPOutputStream getHTTPOutputStream( OutputStream os );
	
	/**
	 * Creates a HTTPServer listening at <code>address:port</port> with a given <code>backlog</code>.
	 * This server uses the handlers passed as parameters.
	 * 
	 * @param port server port
	 * @param backlog server backlog
	 * @param address server address
	 * @param handlers HTTP handlers
	 * @return a HTTPServer 
	 */
	public HTTPServer getHTTPServer( int port , int backlog , InetAddress address , HTTPHandler ... handlers);
	
	/**
	 * Creates a HTTPHandler that resolves data starting from directory <code>root</code>. The handler
	 * implements HTTP/1.0 methods.
	 * 
	 * @param root document root.
	 * @return a HTTPHandler that resolves data starting from directory <code>root</code>.
	 */
	public HTTPHandler getFileSystemHandler1_0( File root );
	
	/**
	 * Creates a HTTPHandler that resolves data starting from directory <code>root</code> and handling requests 
	 * for a specific <code>host</code>. The handler implements HTTP/1.0 methods.
	 * 
	 * @param host server host
	 * @param root document root.
	 * @return a HTTPHandler that resolves data starting from directory <code>root</code>.
	 */
	public HTTPHandler getFileSystemHandler1_0( String host, File root );
	
	/**
	 * Creates a HTTPHandler that resolves data starting from directory <code>root</code>. The handler
	 * implements HTTP/1.1 methods (see https://tools.ietf.org/html/rfc2616).
	 * 
	 * @param root document root.
	 * @return a HTTPHandler that resolves data starting from directory <code>root</code>.
	 */
	public HTTPHandler getFileSystemHandler1_1( File root );
	
	/**
	 * Creates a HTTPHandler that resolves data starting from directory <code>root</code>  and handling requests 
	 * for a specific <code>host</code>. The handler implements HTTP/1.1 methods (see https://tools.ietf.org/html/rfc2616).
	 * 
	 * @param host server host
	 * @param root document root.
	 * @return a HTTPHandler that resolves data starting from directory <code>root</code>.
	 */
	public HTTPHandler getFileSystemHandler1_1( String host, File root );
	
	/**
	 * Creates a HTTPHandler implementing a proxy.
	 * 
	 * @return a HTTPHandler implementing a proxy.
	 */
	public HTTPHandler getProxyHandler( );
	
}
