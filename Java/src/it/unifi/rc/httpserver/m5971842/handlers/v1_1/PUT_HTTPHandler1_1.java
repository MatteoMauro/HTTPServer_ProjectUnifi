package it.unifi.rc.httpserver.m5971842.handlers.v1_1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import it.unifi.rc.httpserver.HTTPReply;
import it.unifi.rc.httpserver.HTTPRequest;
import it.unifi.rc.httpserver.m5971842.MyHTTPReply;
import it.unifi.rc.httpserver.m5971842.MyHTTPUtility;

/**
 * @author Matteo Mauro 5971842
 *
 */
public class PUT_HTTPHandler1_1 extends HTTPHandler1_1 {

	private final String method = "PUT";

	public PUT_HTTPHandler1_1(File root) {
		super(root);

	}

	public PUT_HTTPHandler1_1(String host, File root) {
		super(host, root);
	}

	@Override
	protected boolean isMyMethod(HTTPRequest request) {
		return request.getMethod().equals(this.method) ? true : false;
	}

	/**
	 * Create a file (specified by the URI) and copy the content body.
	 */
	@Override
	protected HTTPReply doHandle1_1(HTTPRequest request) {
		HTTPReply reply = null;
		File prefixPath = getDirectoryRoot();
		FileWriter writer = null;
		Map<String, String> parameters = MyHTTPUtility.getDefaultHeaderLines();
		parameters.put("Content-Length", "0");
		try {
			// if the file already exists, it will be rewritten
			File file = new File(prefixPath, request.getPath());
			writer = new FileWriter(file);
			String data = request.getEntityBody();
			writer.write(data != null ? data : "");
			writer.flush();
			reply = new MyHTTPReply(request.getVersion(), "200", "OK", null, parameters);
		} catch (IOException e) {
			reply = new MyHTTPReply(request.getVersion(), "409", "Conflict", null, parameters);
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return reply;
	}

	@Override
	public void add(HTTPHandler1_1 handler) throws Exception {
		throw new Exception("Impossible to add handler to this object. Not a Composite type.");
	}
}
