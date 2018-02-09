/**
 * 
 */
package it.unifi.rc.httpserver.m5971842.handlers.v1_0;

import java.io.File;
import java.util.Map;

import it.unifi.rc.httpserver.HTTPReply;
import it.unifi.rc.httpserver.HTTPRequest;
import it.unifi.rc.httpserver.m5971842.MyHTTPReply;
import it.unifi.rc.httpserver.m5971842.MyHTTPUtility;

/**
 * @author Matteo Mauro 5971842
 *
 */
public class HEAD_HTTPHandler1_0 extends HTTPHandler1_0 {

	private final String method = "HEAD";

	public HEAD_HTTPHandler1_0(File root) {
		super(root);

	}

	public HEAD_HTTPHandler1_0(String host, File root) {
		super(host, root);
	}

	@Override
	protected boolean isMyMethod(HTTPRequest request) {
		return request.getMethod().equals(this.method) ? true : false;
	}

	/**
	 * Gets the path of the file and if it exists, it gets the metadata of the
	 * file and build the reply. It sends 404 if the file does not exist.
	 */
	@Override
	protected HTTPReply doHandle(HTTPRequest request) {
		HTTPReply reply = null;
		File prefixPath = getDirectoryRoot();
		File file = new File(prefixPath, request.getPath());
		if (file.exists()) {
			// I don't have to read, just get metadata (I put just length and
			// location)
			Map<String, String> parameters = MyHTTPUtility.getDefaultHeaderLines();
			parameters.put("Content-Length", String.valueOf(file.length()));
			parameters.put("Location", "http://" + request.getParameters().get("Host") + file.getPath());
			reply = new MyHTTPReply(request.getVersion(), "200", "OK", null, parameters);
		} else {
			// 404 File Not Found
			Map<String, String> parameters = MyHTTPUtility.getDefaultHeaderLines();
			parameters.put("Content-Length", "0");
			reply = new MyHTTPReply(request.getVersion(), "404", "File Not Found", null, parameters);
		}
		return reply;
	}

	@Override
	public void add(HTTPHandler1_0 handler) throws Exception {
		throw new Exception("Impossible to add handler to this object. Not a Composite type.");
	}

}
