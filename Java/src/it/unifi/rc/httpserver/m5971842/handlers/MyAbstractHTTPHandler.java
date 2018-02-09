package it.unifi.rc.httpserver.m5971842.handlers;

import java.io.File;
import java.util.Map;

import it.unifi.rc.httpserver.HTTPHandler;
import it.unifi.rc.httpserver.HTTPReply;
import it.unifi.rc.httpserver.HTTPRequest;

/**
 * @author Matteo Mauro 5971842
 *
 *         MyAbstractHTTPHandler is designed in order to abstract the sequence
 *         of operations that an handler must carry out, according to the
 *         "Template pattern".
 * 
 */
public abstract class MyAbstractHTTPHandler implements HTTPHandler {

	/**
	 * Represents the host to which this Handler is allowed to perform
	 * operations
	 */
	private String host = null;
	/**
	 * Represents the directory root where the "files searching" starts from
	 */
	private File directoryRoot;

	public MyAbstractHTTPHandler(File root) {
		if (root == null)
			// default path for server files: <root_index_partition>/www/
			directoryRoot = new File(File.separator + "www" + File.separator);
		else
			this.directoryRoot = root;
		this.host = null;
	}

	public MyAbstractHTTPHandler(String host, File root) {
		this(root);
		if (host != null)
			this.host = host;
	}

	/**
	 * handle is a "template method": checking if this handler is able to manage
	 * the method is a common operation, so it's refactored here.
	 * 
	 * @return an HTTPReply if it's responsible for the request, otherwise null
	 */
	@Override
	public HTTPReply handle(HTTPRequest request) {
		try {
			/*
			 * in order to handle a request: method, version and host must be
			 * compatible. Host can be resolved in this class (so the method is
			 * implemented in this class), but method and version will be
			 * resolved by concrete classes.
			 */
			return isMyMethod(request) && isMyHost(request) && isMyVersion(request) ? doHandle(request) : null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Checks if this Handler can perform operation for the host specified in
	 * the request.
	 * 
	 * @param request
	 * @return
	 */
	private boolean isMyHost(HTTPRequest request) {
		if (host != null) {
			Map<String, String> parameters = request.getParameters();
			String host = parameters.get("Host");
			return this.getHost().equals(host);
		} else {
			// if the host is not specified, then it can replies every requests
			// towards every host
			return true;
		}
	}

	/**
	 * Check if this class is compatible with the method of the HTTPRequest.
	 * 
	 * @return true if this class is responsible for the request, otherwise
	 *         false
	 * @throws Exception
	 *             since a CompositeHTTPHandler can't perform this operation, an
	 *             exception is required
	 */
	protected abstract boolean isMyMethod(HTTPRequest request) throws Exception;

	/**
	 * Check if this class is compatible with the version of the HTTPRequest
	 * 
	 * @return true if this class is responsible for the request, otherwise
	 *         false
	 * @throws Exception
	 *             since a CompositeHTTPHandler can't perform this operation, an
	 *             exception is required
	 */
	protected abstract boolean isMyVersion(HTTPRequest request) throws Exception;

	/**
	 * This is the "hook" which must be implemented in subclasses in order to
	 * handler requests, according to "template pattern".
	 * 
	 * @return an HTTPReply
	 * @throws Exception
	 *             since a CompositeHTTPHandler can't perform this operation, an
	 *             exception is required
	 */
	protected abstract HTTPReply doHandle(HTTPRequest request) throws Exception;

	public String getHost() {
		return host;
	}

	public File getDirectoryRoot() {
		return directoryRoot;
	}

}
