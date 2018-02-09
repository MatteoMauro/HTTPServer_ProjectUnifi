package it.unifi.rc.httpserver.m5971842.stream;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import it.unifi.rc.httpserver.HTTPOutputStream;
import it.unifi.rc.httpserver.HTTPReply;
import it.unifi.rc.httpserver.HTTPRequest;

/**
 * @author Matteo Mauro 5971842
 *
 */
public class MyHTTPOutputStream extends HTTPOutputStream {

	private OutputStream os;

	public MyHTTPOutputStream(OutputStream os) {
		super(os);
	}

	@Override
	public void writeHttpReply(HTTPReply reply) {
		write(reply.toString());
	}

	@Override
	public void writeHttpRequest(HTTPRequest request) {
		write(request.toString());
	}

	private void write(String message) {
		byte[] messageByte = null;
		messageByte = message.getBytes(StandardCharsets.US_ASCII);
		try {
			os.write(messageByte);
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void setOutputStream(OutputStream os) {
		this.os = os;
	}

	@Override
	public void close() throws IOException {
		os.close();
	}

}
