package it.unifi.rc.httpserver.m5971842.handlers.v1_0;

import java.io.File;

import it.unifi.rc.httpserver.HTTPRequest;
import it.unifi.rc.httpserver.m5971842.handlers.MyAbstractHTTPHandler;

/**
 * @author Matteo Mauro 5971842
 *
 *         HTTPHandler1_0 abstracts every Handler of HTTP/1.0 protocol, it's
 *         designed according to "Composite pattern".
 */
public abstract class HTTPHandler1_0 extends MyAbstractHTTPHandler {

	public HTTPHandler1_0(File root) {
		super(root);
	}

	public HTTPHandler1_0(String host, File root) {
		super(host, root);
	}

	public abstract void add(HTTPHandler1_0 handler) throws Exception;

	@Override
	protected boolean isMyVersion(HTTPRequest request) throws Exception {
		// compatible exclusively with "HTTP/1.0"
		return request.getVersion().equals("HTTP/1.0");
	}

}
