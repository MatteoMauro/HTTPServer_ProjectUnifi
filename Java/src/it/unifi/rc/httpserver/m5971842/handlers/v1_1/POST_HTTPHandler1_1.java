package it.unifi.rc.httpserver.m5971842.handlers.v1_1;

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
public class POST_HTTPHandler1_1 extends HTTPHandler1_1 {

	private final String method = "POST";

	public POST_HTTPHandler1_1(File root) {
		super(root);

	}

	public POST_HTTPHandler1_1(String host, File root) {
		super(host, root);
	}

	@Override
	protected boolean isMyMethod(HTTPRequest request) {
		return request.getMethod().equals(this.method) ? true : false;
	}

	/**
	 * Gets the path of the file and if it exists, it reads the file and build
	 * the reply. It sends 404 if the file does not exist.
	 */
	@Override
	protected HTTPReply doHandle1_1(HTTPRequest request) {
		HTTPReply reply = null;
		File prefixPath = getDirectoryRoot();
		File file = new File(prefixPath, request.getPath());
		if (file.exists()) {
			// I don't have to read, just print the data of the body
			System.out.println(request.getEntityBody());
			Map<String, String> parameters = MyHTTPUtility.getDefaultHeaderLines();
			// it may be useful to insert the Location of the file
			parameters.put("Location", "http://" + request.getParameters().get("Host") + file.getPath());
			parameters.put("Content-Length", "0");
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
	public void add(HTTPHandler1_1 handler) throws Exception {
		throw new Exception("Impossible to add handler to this object. Not a Composite type.");
	}

}
