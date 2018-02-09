package it.unifi.rc.httpserver.m5971842.handlers.v1_1;

import java.util.Iterator;
import java.util.LinkedList;

import it.unifi.rc.httpserver.HTTPReply;
import it.unifi.rc.httpserver.HTTPRequest;

/**
 * @author Matteo Mauro 5971842
 *
 *         CompositeHTTPHandler1_1 represents the implementation of a class
 *         containing an arbitrary number of Handler of HTTP/1.1 protocol.
 */
public class CompositeHTTPHandler1_1 extends HTTPHandler1_1 {

	private LinkedList<HTTPHandler1_1> handlers;

	public CompositeHTTPHandler1_1() {
		// a composite object does not need to specify root directory or host,
		// every handler will be independent
		super(null);
		handlers = new LinkedList<>();
	}

	@Override
	public void add(HTTPHandler1_1 handler) throws Exception {
		handlers.add(handler);
	}

	/**
	 * Call the handle() method for every HTTPHanlder1_0 until one of them is
	 * able to perform the operation. If none of them can do it, null is
	 * returned.
	 */
	@Override
	public HTTPReply handle(HTTPRequest request) {
		HTTPReply reply = null;
		Iterator<HTTPHandler1_1> it = handlers.iterator();
		boolean handled = false;
		while (it.hasNext() && !handled) {
			reply = it.next().handle(request);
			if (reply != null)
				// succesfully managed, exit the loop
				handled = true;
		}
		return reply;
	}

	/**
	 * This method can't be performed by a Composite class, but actually this
	 * can't be called outside.
	 */
	@Override
	protected boolean isMyMethod(HTTPRequest request) throws Exception {
		throw new Exception("Can't perform this operation on a Composite object.");
	}

	@Override
	protected HTTPReply doHandle1_1(HTTPRequest request) throws Exception {
		throw new Exception("Can't perform this operation on a Composite object.");
	}

}
