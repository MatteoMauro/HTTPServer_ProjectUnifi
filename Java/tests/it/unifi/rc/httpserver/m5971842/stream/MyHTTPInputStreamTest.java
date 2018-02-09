package it.unifi.rc.httpserver.m5971842.stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import it.unifi.rc.httpserver.HTTPInputStream;
import it.unifi.rc.httpserver.HTTPProtocolException;
import it.unifi.rc.httpserver.HTTPReply;
import it.unifi.rc.httpserver.HTTPRequest;

/**
 * 
 * @author Matteo Mauro 5971842
 *
 *         Tests are supposed to use the files present in folder "/testSamples/"
 *         in order to retrieve a correct InputStream.
 */
public class MyHTTPInputStreamTest {

	/**
	 * Check if a semantically correct request can be read from the stream.
	 */
	@Test
	public void readHTTPRequest_Valid() {
		InputStream is = setupStream("RequestCorrectSample.txt");
		HTTPInputStream stream = new MyHTTPInputStream(is);
		try {
			HTTPRequest request = stream.readHttpRequest();
			assertEquals("Method does not match.", "GET", request.getMethod());
			assertEquals("Path does not match.", "/index.html", request.getPath());
			assertEquals("Version does not match.", "HTTP/1.0", request.getVersion());
			assertEquals("Body does not match.", null, request.getEntityBody());
			Map<String, String> expected = new HashMap<>();
			expected.put("Host", "www.miosito.it");
			expected.put("Connection", "close");
			expected.put("Content-Length", "0");
			assertEquals("Parameters do not match.", expected, request.getParameters());
		} catch (HTTPProtocolException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Determine if returns null in case the request is wrongly formatted.
	 * 
	 * @throws UnsupportedOperationException
	 * @throws HTTPProtocolException
	 */
	@Test(expected = HTTPProtocolException.class)
	public void readHTTPRequest_SemanticWrong() throws HTTPProtocolException, UnsupportedOperationException {
		InputStream is = setupStream("RequestWrongRequestLine.txt");
		HTTPInputStream stream = new MyHTTPInputStream(is);
		assertNull(stream.readHttpRequest());
	}

	/**
	 * Check if a semantically correct reply can be read from the stream.
	 */
	@Test
	public void readHTTPReply_Valid() {
		InputStream is = setupStream("ReplyCorrectSample.txt");
		HTTPInputStream stream = new MyHTTPInputStream(is);
		try {
			HTTPReply reply = stream.readHttpReply();
			assertEquals("Version does not match.", "HTTP/1.0", reply.getVersion());
			assertEquals("Status code does not match.", "200", reply.getStatusCode());
			assertEquals("Status message does not match.", "OK", reply.getStatusMessage());
			assertEquals("Body does not match.", "SAY PEACE!", reply.getData());
			Map<String, String> expected = new HashMap<>();
			expected.put("Content-Length", "10");
			assertEquals("Parameters do not match.", expected, reply.getParameters());
		} catch (HTTPProtocolException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Determine if returns null in case the reply is wrongly formatted.
	 * 
	 * @throws UnsupportedOperationException
	 * @throws HTTPProtocolException
	 */
	@Test(expected = HTTPProtocolException.class)
	public void readHTTPReply_SemanticWrong() throws HTTPProtocolException, UnsupportedOperationException {
		InputStream is = setupStream("ReplyWrongStatusLine.txt");
		HTTPInputStream stream = new MyHTTPInputStream(is);
		assertNull(stream.readHttpReply());
	}

	/**
	 * Provides the correct stream required
	 * 
	 * @param path
	 * @return
	 */
	private InputStream setupStream(String path) {
		return getClass().getResourceAsStream(File.separator + "testSamples" + File.separator + path);
	}

}
