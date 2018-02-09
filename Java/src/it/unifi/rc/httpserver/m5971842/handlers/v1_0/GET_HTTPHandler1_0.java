/**
 * 
 */
package it.unifi.rc.httpserver.m5971842.handlers.v1_0;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import it.unifi.rc.httpserver.HTTPReply;
import it.unifi.rc.httpserver.HTTPRequest;
import it.unifi.rc.httpserver.m5971842.MyHTTPReply;
import it.unifi.rc.httpserver.m5971842.MyHTTPUtility;

/**
 * @author Matteo Mauro 5971842
 *
 */
public class GET_HTTPHandler1_0 extends HTTPHandler1_0 {
	private final String method = "GET";

	public GET_HTTPHandler1_0(File root) {
		super(root);

	}

	public GET_HTTPHandler1_0(String host, File root) {
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
	protected HTTPReply doHandle(HTTPRequest request) {
		HTTPReply reply = null;
		File prefixPath = getDirectoryRoot();
		File file = new File(prefixPath, request.getPath());
		if (file.exists()) {
			// read the whole file...
			String data = readFile(file);
			Map<String, String> parameters = MyHTTPUtility.getDefaultHeaderLines();
			// ...and set its length
			parameters.put("Content-Length", String.valueOf(file.length()));
			reply = new MyHTTPReply(request.getVersion(), "200", "OK", data, parameters);
		} else {
			// 404 File Not Found
			Map<String, String> parameters = MyHTTPUtility.getDefaultHeaderLines();
			parameters.put("Content-Length", "0");
			reply = new MyHTTPReply(request.getVersion(), "404", "File Not Found", null, parameters);
		}
		return reply;
	}

	/**
	 * Read the file passed as argument character by character
	 * 
	 * @param file
	 * @return
	 */
	private String readFile(File file) {
		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(new FileInputStream(file));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		StringBuffer buffer = new StringBuffer();
		int c;
		try {
			while ((c = reader.read()) != -1) {
				buffer.append((char) c);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	@Override
	public void add(HTTPHandler1_0 handler) throws Exception {
		throw new Exception("Impossible to add handler to this object. Not a Composite type.");
	}

}
