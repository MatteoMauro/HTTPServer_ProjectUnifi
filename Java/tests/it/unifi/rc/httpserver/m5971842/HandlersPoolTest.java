package it.unifi.rc.httpserver.m5971842;

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
import it.unifi.rc.httpserver.m5971842.handlers.v1_0.CompositeHTTPHandler1_0;
import it.unifi.rc.httpserver.m5971842.handlers.v1_0.GET_HTTPHandler1_0;
import it.unifi.rc.httpserver.m5971842.handlers.v1_0.HEAD_HTTPHandler1_0;
import it.unifi.rc.httpserver.m5971842.handlers.v1_0.POST_HTTPHandler1_0;
import it.unifi.rc.httpserver.m5971842.stream.MyHTTPInputStream;

public class HandlersPoolTest {

	private final String testSamples = File.separator + "testSamples" + File.separator;

	/**
	 * If the pool is empty, null must be returned.
	 */
	@Test
	public void emptyPool() {
		HandlersPool pool = new HandlersPool();
		HTTPRequest request = setupHTTPRequest(testSamples + "RequestCorrectSample.txt");
		HTTPReply reply = pool.handle(request);
		assertNull(reply);
	}

	/**
	 * Test if the pool can retrieve the correct HTTPReply to a semantic correct
	 * HTTPRequest.
	 * 
	 */
	@Test
	public void getCorrectReply() throws Exception {
		HandlersPool pool = new HandlersPool();
		CompositeHTTPHandler1_0 handlers1_0 = new CompositeHTTPHandler1_0();
		pool.add(handlers1_0);
		handlers1_0.add(new POST_HTTPHandler1_0(null));
		handlers1_0.add(new HEAD_HTTPHandler1_0(null));
		// get will be the one responsible to handle the request, I put it at
		// last position so the others will have to return null before and I can
		// test if multiple handlers works together
		handlers1_0.add(new GET_HTTPHandler1_0(null));
		HTTPRequest request = setupHTTPRequest(testSamples + "RequestCorrectSample.txt");
		HTTPReply reply = pool.handle(request);
		checkValidReplyFields(reply);
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
		assertEquals("Version does not match.", "HTTP/1.0", reply.getVersion());
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
