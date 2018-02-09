/**
 * 
 */
package it.unifi.rc.httpserver.m5971842;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import it.unifi.rc.httpserver.HTTPInputStream;
import it.unifi.rc.httpserver.HTTPOutputStream;
import it.unifi.rc.httpserver.HTTPProtocolException;
import it.unifi.rc.httpserver.HTTPReply;
import it.unifi.rc.httpserver.HTTPRequest;
import it.unifi.rc.httpserver.m5971842.stream.MyHTTPInputStream;
import it.unifi.rc.httpserver.m5971842.stream.MyHTTPOutputStream;

/**
 * @author Matteo Mauro 5971842
 * 
 *         HTTPReplyThread represents a Runnable class associated with a socket,
 *         which purpose is to build and send a reply using the pool of handlers
 *         given.
 *
 */
public class HTTPReplyThread implements Runnable {

	private Socket socket;
	private HandlersPool handlers;
	private HTTPInputStream httpIn;
	private HTTPOutputStream httpOut;
	/**
	 * specify if this connection has to be closed, by default for HTTP/1.0 is
	 * non persistent while for HTTP/1.1 is persistent. The possible values are:
	 * - true: close the connection - false: do not close the connection
	 */
	private boolean flagCloseConnection;

	/**
	 * 
	 * @param socket
	 *            associated with the client.
	 * @param handlers
	 *            pool of the handlers available.
	 */
	public HTTPReplyThread(Socket socket, HandlersPool handlers) {
		this.socket = socket;
		this.handlers = handlers;
	}

	/**
	 * Instantiate streams, handle the request and at last answer the client.
	 */
	@Override
	public void run() {
		// get the streams only when the thread has been launched
		instantiateStreams();
		do {
			try {
				HTTPRequest request = httpIn.readHttpRequest();
				// determine if the connection has been specified
				flagCloseConnection = setClosure(request);
				// delegate the handling to the pool and just get the reply
				HTTPReply reply = handlers.handle(request);
				if (reply == null) {
					// 501 Not Implemented method
					Map<String, String> parameters = MyHTTPUtility.getDefaultHeaderLines();
					parameters.put("Connection", flagCloseConnection ? "close" : "keep-alive");
					reply = new MyHTTPReply(request.getVersion(), "501", "Not Implemented Method", null, parameters);
				}
				httpOut.writeHttpReply(reply);
			} catch (HTTPProtocolException e) {
				e.printStackTrace();
				/*
				 * if an exception occurs, it must be checked: 
				 * - wrong request line: the request can't be sent, close everything; 
				 * - wrong header line: discard the line but continue.
				 */
				if (e.getMessage().contains("Request line doesn't match HTTP protocol definition")) {
					// close the stream since request line is not correct.
					sendBadRequest();
				}
			}
		} while (flagCloseConnection == false);
		closeStreams();
	}

	/**
	 * if request line isn't correct, discard all and send 400 Bad Error, at
	 * last close connection
	 */
	private void sendBadRequest() {
		flagCloseConnection = true;
		Map<String, String> parameters = MyHTTPUtility.getDefaultHeaderLines();
		parameters.put("Connection", "close");
		HTTPReply reply = new MyHTTPReply("HTTP/1.0", "400", "Bad Request", null, parameters);
		httpOut.writeHttpReply(reply);
	}

	private void instantiateStreams() {
		try {
			httpIn = new MyHTTPInputStream(socket.getInputStream());
			httpOut = new MyHTTPOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void closeStreams() {
		try {
			httpIn.close();
			httpOut.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Search for header line "Connection", if it does not exist it has to be
	 * specified for the handlers: - HTTP/1.0: by default sets to "close" -
	 * HTTP/1.1: by default sets to "keep-alive"
	 * 
	 * @param request
	 * @return true to close the connection, otherwise false
	 */
	private boolean setClosure(HTTPRequest request) {

		boolean rst;
		Map<String, String> parameters = request.getParameters();
		String value;
		if ((value = parameters.get("Connection")) != null) {
			rst = value.equals("close");
		} else {
			// Connection wasn't specified, so set it by default
			if (request.getVersion().equals("HTTP/1.0")) {
				parameters.put("Connection", "close");
				rst = true;
			} else {
				parameters.put("Connection", "keep-alive");
				rst = false;
			}
		}
		return rst;
	}

}
