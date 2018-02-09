package it.unifi.rc.httpserver.m5971842.stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import it.unifi.rc.httpserver.HTTPInputStream;
import it.unifi.rc.httpserver.HTTPProtocolException;
import it.unifi.rc.httpserver.HTTPReply;
import it.unifi.rc.httpserver.HTTPRequest;
import it.unifi.rc.httpserver.m5971842.stream.HTTPMessageParser.Type;

/**
 * @author Matteo Mauro 5971842
 *
 */
public class MyHTTPInputStream extends HTTPInputStream {

	/**
	 * Since an HTTPRequest/Reply is ASCII 7 bit, a BufferedReader provides an
	 * efficient way for reading.
	 * 
	 * @throws IllegalArgumentExcpetion
	 *             if the argument it's null.
	 */
	private BufferedReader reader;

	public MyHTTPInputStream(InputStream is) {
		super(is);
	}

	@Override
	protected void setInputStream(InputStream is) {
		this.reader = new BufferedReader(new InputStreamReader(is));
	}

	@Override
	public HTTPRequest readHttpRequest() throws HTTPProtocolException {
		HTTPMessageParser parser = new HTTPMessageParser(reader, Type.REQUEST);
		return parser.parseRequest();
	}

	@Override
	public HTTPReply readHttpReply() throws HTTPProtocolException {
		HTTPMessageParser parser = new HTTPMessageParser(reader, Type.REPLY);
		return parser.parseReply();
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}

}
