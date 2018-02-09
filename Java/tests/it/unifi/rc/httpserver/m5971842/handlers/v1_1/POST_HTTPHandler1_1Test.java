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
public class POST_HTTPHandler1_1Test {
	private final String validHost = "www.miosito.it";
	private final File validRoot = new File(File.separator + "www" + File.separator + "anotherFolder");
	private final String methodSamples = File.separator + "testSamples" + File.separator + "methodSamples"
			+ File.separator;

	/**
	 * Test if POST_HTTPHandler1_1 can retrieve correctly the files in this
	 * case: 1) host and root not specified (default -> host = null,
	 * root="/www"); 2) correct host specified; 3) root specified.
	 */
	@Test
	public void validRequest() {
		POST_HTTPHandler1_1 handlerDefault = setupHandler(null, null);
		POST_HTTPHandler1_1 handlerSpecifiedHost = setupHandler(validHost, null);
		POST_HTTPHandler1_1 handlerSpecifiedRoot = setupHandler(null, validRoot);
		HTTPRequest request = setupHTTPRequest(methodSamples + "post11.txt");
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
	 * Test if POST_HTTPHandler1_1 returns 404 if file is not found.
	 */
	@Test
	public void fileNotFound404() {
		POST_HTTPHandler1_1 handlerDefault = setupHandler(null, null);
		HTTPRequest request = setupHTTPRequest(methodSamples + "post11FileNotFound.txt");
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
		POST_HTTPHandler1_1 handler = setupHandler(validHost, null);
		HTTPRequest request = setupHTTPRequest(methodSamples + "post11WrongHost.txt");
		HTTPReply reply = handler.handle(request);
		assertNull(reply);
	}

	/**
	 * Test if a request is denied if the method is not compatible.
	 */
	@Test
	public void methodNotCompatible() {
		POST_HTTPHandler1_1 handler = setupHandler(null, null);
		HTTPRequest request = setupHTTPRequest(methodSamples + "get11.txt");
		HTTPReply reply = handler.handle(request);
		// get11.txt is not the right method
		assertNull(reply);
	}

	private POST_HTTPHandler1_1 setupHandler(String host, File root) {
		return new POST_HTTPHandler1_1(host, root);
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
		assertNull("Body does not match.", reply.getData());
		Map<String, String> expected = new HashMap<>();
		expected.put("Content-Length", "0");
		// in order to perform correctly the comparison, I can ignore the Date
		// entry and the Location that changes for every test
		reply.getParameters().remove("Date");
		reply.getParameters().remove("Location");
		assertEquals("Parameters do not match.", expected, reply.getParameters());
	}

}
