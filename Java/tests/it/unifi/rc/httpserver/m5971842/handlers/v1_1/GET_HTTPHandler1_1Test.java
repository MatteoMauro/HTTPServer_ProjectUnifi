package it.unifi.rc.httpserver.m5971842.handlers.v1_1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import it.unifi.rc.httpserver.HTTPProtocolException;
import it.unifi.rc.httpserver.HTTPReply;
import it.unifi.rc.httpserver.HTTPRequest;
import it.unifi.rc.httpserver.m5971842.stream.MyHTTPInputStream;

/**
 * 
 * @author Matteo Mauro 5971842
 *
 *         Tests are supposed to use the files present in folder "/testSamples/"
 *         in order to retrieve the correct files.
 */
public class GET_HTTPHandler1_1Test {
	private final String validHost = "www.miosito.it";
	private final File validRoot = new File(File.separator + "www" + File.separator + "anotherFolder");
	private final String methodSamples = File.separator + "testSamples" + File.separator + "methodSamples"
			+ File.separator;

	/**
	 * Test if GET_HTTPHandler1_1 can retrieve correctly the files in this case:
	 * 1) host and root not specified (default -> host = null, root="/www"); 2)
	 * correct host specified; 3) root specified.
	 */
	@Test
	public void validRequest() {
		GET_HTTPHandler1_1 handlerDefault = setupHandler(null, null);
		GET_HTTPHandler1_1 handlerSpecifiedHost = setupHandler(validHost, null);
		GET_HTTPHandler1_1 handlerSpecifiedRoot = setupHandler(null, validRoot);
		HTTPRequest request = setupHTTPRequest(methodSamples + "get11.txt");
		// default
		HTTPReply reply = handlerDefault.handle(request);
		checkValidReplyFields(reply);
		// host has been specified
		reply = handlerSpecifiedHost.handle(request);
		checkValidReplyFields(reply);
		// root has been specified
		reply = handlerSpecifiedRoot.handle(request);
		checkValidReplyFields(reply);
	}

	/**
	 * Test if GET_HTTPHandler1_1 returns 404 if file is not found.
	 */
	@Test
	public void fileNotFound404() {
		GET_HTTPHandler1_1 handlerDefault = setupHandler(null, null);
		HTTPRequest request = setupHTTPRequest(methodSamples + "get11FileNotFound.txt");
		HTTPReply reply = handlerDefault.handle(request);
		assertEquals("Version does not match.", "HTTP/1.1", reply.getVersion());
		assertEquals("Status code does not match.", "404", reply.getStatusCode());
		assertEquals("Status message does not match.", "File Not Found", reply.getStatusMessage());
		assertNull("Body does not match.", reply.getData());
		Map<String, String> expected = new HashMap<>();
		expected.put("Content-Length", "0");
		// in order to perform correctly the comparison, I can ignore the Date
		// entry
		reply.getParameters().remove("Date");
		assertEquals("Parameters do not match.", expected, reply.getParameters());
	}

	/**
	 * Test if a request is denied if the host is not compatible.
	 */
	@Test
	public void hostNotCompatible() {
		// validHost is not equals to the Host specified in the request
		GET_HTTPHandler1_1 handler = setupHandler(validHost, null);
		HTTPRequest request = setupHTTPRequest(methodSamples + "get11WrongHost.txt");
		HTTPReply reply = handler.handle(request);
		assertNull(reply);
	}

	/**
	 * Test if a request is denied if the method is not compatible.
	 */
	@Test
	public void methodNotCompatible() {
		GET_HTTPHandler1_1 handler = setupHandler(null, null);
		HTTPRequest request = setupHTTPRequest(methodSamples + "post11.txt");
		HTTPReply reply = handler.handle(request);
		// post11.txt is not the right method
		assertNull(reply);
	}

	private GET_HTTPHandler1_1 setupHandler(String host, File root) {
		return new GET_HTTPHandler1_1(host, root);
	}

	private HTTPRequest setupHTTPRequest(String file) {
		InputStream is = getClass().getResourceAsStream(file);
		MyHTTPInputStream httpis = new MyHTTPInputStream(is);
		try {
			return httpis.readHttpRequest();
		} catch (HTTPProtocolException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void checkValidReplyFields(HTTPReply reply) {
		assertEquals("Version does not match.", "HTTP/1.1", reply.getVersion());
		assertEquals("Status code does not match.", "200", reply.getStatusCode());
		assertEquals("Status message does not match.", "OK", reply.getStatusMessage());
		assertEquals("Body does not match.", "SAY PEACE!", reply.getData());
		Map<String, String> expected = new HashMap<>();
		expected.put("Content-Length", "10");
		// in order to perform correctly the comparison, I can ignore the Date
		// entry
		reply.getParameters().remove("Date");
		assertEquals("Parameters do not match.", expected, reply.getParameters());
	}

}
