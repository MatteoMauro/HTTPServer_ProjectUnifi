/**
 * 
 */
package it.unifi.rc.httpserver.m5971842.handlers;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import it.unifi.rc.httpserver.HTTPHandler;
import it.unifi.rc.httpserver.HTTPReply;
import it.unifi.rc.httpserver.HTTPRequest;
import it.unifi.rc.httpserver.m5971842.MyHTTPReply;
import it.unifi.rc.httpserver.m5971842.MyHTTPUtility;
import it.unifi.rc.httpserver.m5971842.stream.MyHTTPInputStream;
import it.unifi.rc.httpserver.m5971842.stream.MyHTTPOutputStream;

/**
 * @author Matteo Mauro 5971842
 * 
 *         Provides an efficient way to retrieve cached files already been sent
 *         through the net. Caching is performed saving the body of a reply
 *         associated with the url:=host+path
 *
 */
public class HTTPProxyHandler implements HTTPHandler {

	private Map<String, String> cachedFiles = new HashMap<String, String>();

	/**
	 * Check if the resource has been already requested. If it is present, a
	 * direct reply is sent, otherwise a new request is forwarded to the real
	 * server.
	 */
	@Override
	public HTTPReply handle(HTTPRequest request) {
		String path = request.getPath();
		String host = request.getParameters().get("Host");
		HTTPReply reply = checkCache(request, host + path);
		if (reply == null) {
			Socket socket = null;
			// get the file from the server destination
			try {
				socket = new Socket(host, 80);
				redirectRequest(socket, request);
				reply = getReply(socket);
				if (reply != null && reply.getStatusCode().equals("200"))
					// save the entry <url, reply>
					cachedFiles.put(host + path, reply.getData());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return reply;
	}

	/**
	 * Check if the resource requested (represented by the body) is already
	 * cached. In affirmative case it returns a reply, otherwise null
	 * 
	 * @param request
	 * @param file
	 * @return
	 */
	private HTTPReply checkCache(HTTPRequest request, String file) {
		boolean found = false;
		String body = null;
		HTTPReply reply = null;
		// retrieve the body if exists and create a reply
		Iterator<Entry<String, String>> it = cachedFiles.entrySet().iterator();
		// check every entry of the map until it ends or a correspondence is
		// found
		while (it.hasNext() && !found) {
			Entry<String, String> entry = it.next();
			if (entry.getKey().equals(file)) {
				found = true;
				System.out.println("The file has been successfully retrieved from the proxy.");
				body = entry.getValue();
			}
		}
		if (found) {
			if (body == null) {
				Map<String, String> parameters = MyHTTPUtility.getDefaultHeaderLines();
				parameters.put("Content-Length", "0");
				reply = new MyHTTPReply(request.getVersion(), "200", "OK", null, parameters);
			} else {
				Map<String, String> parameters = MyHTTPUtility.getDefaultHeaderLines();
				parameters.put("Content-Length", String.valueOf(body.length()));
				reply = new MyHTTPReply(request.getVersion(), "200", "OK", body, parameters);
			}
		}
		return reply;
	}

	private void redirectRequest(Socket socket, HTTPRequest request) {
		MyHTTPOutputStream os;
		try {
			os = new MyHTTPOutputStream(socket.getOutputStream());
			os.writeHttpRequest(request);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private HTTPReply getReply(Socket socket) {
		HTTPReply reply = null;
		MyHTTPInputStream is;
		try {
			is = new MyHTTPInputStream(socket.getInputStream());
			reply = is.readHttpReply();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return reply;
	}

}
