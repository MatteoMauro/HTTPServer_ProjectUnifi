package it.unifi.rc.httpserver.m5971842.handlers.v1_1;

import java.io.File;

import it.unifi.rc.httpserver.HTTPReply;
import it.unifi.rc.httpserver.HTTPRequest;
import it.unifi.rc.httpserver.m5971842.handlers.MyAbstractHTTPHandler;

/**
 * @author Matteo Mauro 5971842
 *
 *         HTTPHandler1_1 abstracts every Handler of HTTP/1.1 protocol, it's
 *         designed according to "Composite pattern".
 */
public abstract class HTTPHandler1_1 extends MyAbstractHTTPHandler {

	public HTTPHandler1_1(File root) {
		super(root);
	}

	public HTTPHandler1_1(String host, File root) {
		super(host, root);
	}

	/**
	 * This method is realized according to template method: "Host" header line
	 * is mandatory in order to perform a response (this is not required in
	 * version 1.0), so the check is refactored here. doHandle1_1() is the hook
	 * method for the implemented classes.
	 * 
	 * @return an HTTPReply if this handler can manage the request, null
	 *         otherwise (null even in the case the "Host" header line isn't
	 *         specified in the request)
	 * @throws Exception
	 *             since a CompositeHTTPHandler can't perform this operation, an
	 *             exception is required
	 */
	protected HTTPReply doHandle(HTTPRequest request) throws Exception {
		return request.getParameters().get("Host") == null ? null : doHandle1_1(request);
	}

	/**
	 * Hook method for implemented classes.
	 * 
	 * @throws Exception
	 *             since a CompositeHTTPHandler can't perform this operation, an
	 *             exception is required
	 */
	protected abstract HTTPReply doHandle1_1(HTTPRequest request) throws Exception;

	public abstract void add(HTTPHandler1_1 handler) throws Exception;

	@Override
	protected boolean isMyVersion(HTTPRequest request) throws Exception {
		// compatible with both "HTTP/1.0" and "HTTP/1.1"
		return request.getVersion().equals("HTTP/1.0") || request.getVersion().equals("HTTP/1.1");
	}
}
