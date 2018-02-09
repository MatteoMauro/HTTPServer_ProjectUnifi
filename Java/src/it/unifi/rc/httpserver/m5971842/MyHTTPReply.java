package it.unifi.rc.httpserver.m5971842;

import java.util.Map;

import it.unifi.rc.httpserver.HTTPReply;

/**
 * @author Matteo Mauro 5971842
 *
 */
public class MyHTTPReply implements HTTPReply {

	private String version;
	private String statusCode;
	private String statusMessage;
	private String data;
	private Map<String, String> parameters;

	public MyHTTPReply(String version, String statusCode, String statusMessage, String data,
			Map<String, String> parameters) {
		this.version = version;
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
		this.data = data;
		this.parameters = parameters;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public String getStatusCode() {
		return statusCode;
	}

	@Override
	public String getStatusMessage() {
		return statusMessage;
	}

	@Override
	public String getData() {
		return data;
	}

	@Override
	public Map<String, String> getParameters() {
		return parameters;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for (Map.Entry<String, String> entry : getParameters().entrySet()) {
			buffer.append(entry.getKey() + ": " + entry.getValue() + "\n");
		}
		buffer.append("\n");// separation between header and body
		return getVersion() + " " + getStatusCode() + " " + getStatusMessage() + "\n" + buffer.toString() + getData();
	}

}
