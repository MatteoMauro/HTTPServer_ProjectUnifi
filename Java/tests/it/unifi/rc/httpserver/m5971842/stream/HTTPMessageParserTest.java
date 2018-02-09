package it.unifi.rc.httpserver.m5971842.stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import it.unifi.rc.httpserver.HTTPProtocolException;
import it.unifi.rc.httpserver.HTTPReply;
import it.unifi.rc.httpserver.HTTPRequest;
import it.unifi.rc.httpserver.m5971842.stream.HTTPMessageParser.Type;

/**
 * 
 * @author Matteo Mauro 5971842
 *
 *         Tests are supposed to use the files present in folder "/testSamples/"
 *         in order to retrieve a correct BufferedReader.
 */
public class HTTPMessageParserTest {

	/**
	 * expecting IllegalArgumentException if argument are null
	 */
	@Test(expected = IllegalArgumentException.class)
	public void illegalArgumentExceptionHTTPMessageParser() {
		new HTTPMessageParser(null, null);
	}

	/**
	 * Determine if a correct request is recognized.
	 */
	@Test
	public void parseRequest_Valid() {
		BufferedReader reader = setupRequest("RequestCorrectSample.txt");
		HTTPMessageParser parser = new HTTPMessageParser(reader, Type.REQUEST);
		HTTPRequest request = null;
		try {
			request = parser.parseRequest();
			assertEquals("Method does not match.", "GET", request.getMethod());
			assertEquals("Path does not match.", "/index.html", request.getPath());
			assertEquals("Version does not match.", "HTTP/1.0", request.getVersion());
			assertEquals("Body does not match.", null, request.getEntityBody());
			Map<String, String> expected = new HashMap<>();
			expected.put("Host", "www.miosito.it");
			expected.put("Connection", "close");
			expected.put("Content-Length", "0");
			assertEquals("Parameters do not match.", expected, request.getParameters());
		} catch (UnsupportedOperationException | HTTPProtocolException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Determine if a wrong type is detected.
	 * 
	 * @throws UnsupportedOperationException
	 * @throws HTTPProtocolException
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void parseRequest_WrongCall() throws HTTPProtocolException, UnsupportedOperationException {
		BufferedReader reader = setupRequest("RequestWrongRequestLine.txt");
		HTTPMessageParser parser = new HTTPMessageParser(reader, Type.REQUEST);
		HTTPReply reply = parser.parseReply();
	}

	/**
	 * Determine if a wrong request line is detected.
	 */
	@Test(expected = HTTPProtocolException.class)
	public void parseRequest_WrongRequestLine() throws HTTPProtocolException, UnsupportedOperationException {
		BufferedReader reader = setupRequest("RequestWrongRequestLine.txt");
		HTTPMessageParser parser = new HTTPMessageParser(reader, Type.REQUEST);
		HTTPRequest request = parser.parseRequest();
	}

	/**
	 * Determine if a wrong header section is detected.
	 */
	@Test
	public void parseRequest_WrongHeader() {
		BufferedReader reader = setupRequest("RequestWrongHeader.txt");
		HTTPMessageParser parser = new HTTPMessageParser(reader, Type.REQUEST);
		HTTPRequest request = null;
		try {
			request = parser.parseRequest();
			// in the example no header lines is correct, so it has to be 0
			assertTrue(request.getParameters().size() == 0);
		} catch (HTTPProtocolException | UnsupportedOperationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Determine if a correct reply is recognized.
	 */
	@Test
	public void parseReply_Valid() {
		BufferedReader reader = setupReply("ReplyCorrectSample.txt");
		HTTPMessageParser parser = new HTTPMessageParser(reader, Type.REPLY);
		HTTPReply reply = null;
		try {
			reply = parser.parseReply();
			assertEquals("Version does not match.", "HTTP/1.0", reply.getVersion());
			assertEquals("Status code does not match.", "200", reply.getStatusCode());
			assertEquals("Status message does not match.", "OK", reply.getStatusMessage());
			assertEquals("Body does not match.", "SAY PEACE!", reply.getData());
			Map<String, String> expected = new HashMap<>();
			expected.put("Content-Length", "10");
			assertEquals("Parameters do not match.", expected, reply.getParameters());
		} catch (UnsupportedOperationException | HTTPProtocolException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Determine if a wrong reply first line is recognized.
	 * 
	 * @throws UnsupportedOperationException
	 * @throws HTTPProtocolException
	 */
	@Test(expected = HTTPProtocolException.class)
	public void parseReply_WrongStatusLine() throws HTTPProtocolException, UnsupportedOperationException {
		BufferedReader reader = setupReply("ReplyWrongStatusLine.txt");
		HTTPMessageParser parser = new HTTPMessageParser(reader, Type.REPLY);
		HTTPReply reply = null;
		reply = parser.parseReply();
	}

	/**
	 * Determine if a wrong header section is recognized.
	 * 
	 */
	@Test
	public void parseReply_WrongHeader() {
		BufferedReader reader = setupReply("ReplyWrongHeader.txt");
		HTTPMessageParser parser = new HTTPMessageParser(reader, Type.REPLY);
		HTTPReply reply = null;
		try {
			reply = parser.parseReply();
			// in the example no header lines is correct, so it has to be 0
			assertTrue(reply.getParameters().size() == 0);
		} catch (HTTPProtocolException | UnsupportedOperationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Provides the correct stream required
	 * 
	 * @param path
	 * @return
	 */
	private BufferedReader setupRequest(String path) {
		InputStream is = getClass().getResourceAsStream(File.separator + "testSamples" + File.separator + path);
		return new BufferedReader(new InputStreamReader(is));
	}

	/**
	 * Provides the correct stream required
	 * 
	 * @param path
	 * @return
	 */
	private BufferedReader setupReply(String path) {
		InputStream is = getClass().getResourceAsStream(File.separator + "testSamples" + File.separator + path);
		return new BufferedReader(new InputStreamReader(is));
	}
}
