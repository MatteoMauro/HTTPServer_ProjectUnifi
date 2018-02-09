package it.unifi.rc.httpserver.m5971842;

import java.io.File;
import java.util.Map;
import java.util.regex.Matcher;

import it.unifi.rc.httpserver.HTTPRequest;

/**
 * @author Matteo Mauro 5971842
 *
 */
public class MyHTTPRequest implements HTTPRequest {

	private String version;
	private String method;
	private String path;
	private String body;
	private Map<String, String> parameters;

	public MyHTTPRequest(String method, String path, String version, String body, Map<String, String> parameters) {
		this.version = version;
		this.method = method;
		// in case the file has to be searched in Windows, replace '/' with
		// File.separator
		this.path = path.replaceAll("/", Matcher.quoteReplacement(File.separator));
		this.body = body;
		this.parameters = parameters;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public String getMethod() {
		return method;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public String getEntityBody() {
		return body;
	}

	@Override
	public Map<String, String> getParameters() {
		return parameters;
	}

	/**
	 * return a string formatted exactly as an HTTP request.
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for (Map.Entry<String, String> entry : getParameters().entrySet()) {
			buffer.append(entry.getKey() + ": " + entry.getValue() + "\n");
		}
		buffer.append("\n");// separation between header and body
		return getMethod() + " " + getPath() + " " + getVersion() + "\n" + buffer.toString() + getEntityBody();
	}

}
