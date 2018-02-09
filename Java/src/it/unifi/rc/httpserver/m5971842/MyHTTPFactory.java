package it.unifi.rc.httpserver.m5971842;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

import it.unifi.rc.httpserver.HTTPFactory;
import it.unifi.rc.httpserver.HTTPHandler;
import it.unifi.rc.httpserver.HTTPInputStream;
import it.unifi.rc.httpserver.HTTPOutputStream;
import it.unifi.rc.httpserver.HTTPServer;
import it.unifi.rc.httpserver.m5971842.handlers.HTTPProxyHandler;
import it.unifi.rc.httpserver.m5971842.handlers.v1_0.CompositeHTTPHandler1_0;
import it.unifi.rc.httpserver.m5971842.handlers.v1_0.GET_HTTPHandler1_0;
import it.unifi.rc.httpserver.m5971842.handlers.v1_0.HEAD_HTTPHandler1_0;
import it.unifi.rc.httpserver.m5971842.handlers.v1_0.POST_HTTPHandler1_0;
import it.unifi.rc.httpserver.m5971842.handlers.v1_1.CompositeHTTPHandler1_1;
import it.unifi.rc.httpserver.m5971842.handlers.v1_1.DELETE_HTTPHandler1_1;
import it.unifi.rc.httpserver.m5971842.handlers.v1_1.GET_HTTPHandler1_1;
import it.unifi.rc.httpserver.m5971842.handlers.v1_1.HEAD_HTTPHandler1_1;
import it.unifi.rc.httpserver.m5971842.handlers.v1_1.POST_HTTPHandler1_1;
import it.unifi.rc.httpserver.m5971842.handlers.v1_1.PUT_HTTPHandler1_1;
import it.unifi.rc.httpserver.m5971842.stream.MyHTTPInputStream;
import it.unifi.rc.httpserver.m5971842.stream.MyHTTPOutputStream;

/**
 * @author Matteo Mauro 5971842
 *
 */
public class MyHTTPFactory implements HTTPFactory {

	@Override
	public HTTPInputStream getHTTPInputStream(InputStream is) {
		return new MyHTTPInputStream(is);
	}

	@Override
	public HTTPOutputStream getHTTPOutputStream(OutputStream os) {
		return new MyHTTPOutputStream(os);
	}

	@Override
	public HTTPServer getHTTPServer(int port, int backlog, InetAddress address, HTTPHandler... handlers) {
		try {
			return new MyHTTPServer(port, backlog, address, handlers);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * MyHTTPFactory for version HTTP/1.0 provides GET, POST and HEAD.
	 * 
	 * @param root
	 *            specifies the directory root from where starting to look for
	 *            files, if not specified by default is "www/"
	 */
	@Override
	public HTTPHandler getFileSystemHandler1_0(File root) {
		CompositeHTTPHandler1_0 handler = new CompositeHTTPHandler1_0();
		try {
			// exception required because I could have put composite objects
			handler.add(new GET_HTTPHandler1_0(root));
			handler.add(new POST_HTTPHandler1_0(root));
			handler.add(new HEAD_HTTPHandler1_0(root));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return handler;
	}

	/**
	 * MyHTTPFactory for version HTTP/1.0 provides GET, POST and HEAD.
	 * 
	 * @param root
	 *            specifies the directory root from where starting to look for
	 *            files, if not specified by default is "www/"
	 * @param host
	 *            specifies that every request which doesn't contain the
	 *            specified Host will not be replied.
	 */
	@Override
	public HTTPHandler getFileSystemHandler1_0(String host, File root) {
		CompositeHTTPHandler1_0 handler = new CompositeHTTPHandler1_0();
		try {
			// exception required because I should have put composite objects
			handler.add(new GET_HTTPHandler1_0(host, root));
			handler.add(new POST_HTTPHandler1_0(host, root));
			handler.add(new HEAD_HTTPHandler1_0(host, root));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return handler;
	}

	/**
	 * MyHTTPFactory for version HTTP/1.1 provides GET, POST, HEAD, PUT and
	 * DELETE.
	 * 
	 * @param root
	 *            specifies the directory root from where starting to look for
	 *            files, if not specified by default is "www/"
	 */
	@Override
	public HTTPHandler getFileSystemHandler1_1(File root) {
		CompositeHTTPHandler1_1 handler = new CompositeHTTPHandler1_1();
		try {
			// exception required because I should have put composite objects
			handler.add(new GET_HTTPHandler1_1(root));
			handler.add(new POST_HTTPHandler1_1(root));
			handler.add(new HEAD_HTTPHandler1_1(root));
			handler.add(new PUT_HTTPHandler1_1(root));
			handler.add(new DELETE_HTTPHandler1_1(root));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return handler;
	}

	/**
	 * MyHTTPFactory for version HTTP/1.1 provides GET, POST, HEAD, PUT and
	 * DELETE.
	 * 
	 * @param root
	 *            specifies the directory root from where starting to look for
	 *            files, if not specified by default is "www/"
	 * @param host
	 *            specifies that every request which doesn't contain the
	 *            specified Host will not be replied.
	 */
	@Override
	public HTTPHandler getFileSystemHandler1_1(String host, File root) {
		CompositeHTTPHandler1_1 handler = new CompositeHTTPHandler1_1();
		try {
			// exception required because I should have put composite objects
			handler.add(new GET_HTTPHandler1_1(host, root));
			handler.add(new POST_HTTPHandler1_1(host, root));
			handler.add(new HEAD_HTTPHandler1_1(host, root));
			handler.add(new PUT_HTTPHandler1_1(host, root));
			handler.add(new DELETE_HTTPHandler1_1(host, root));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return handler;
	}

	@Override
	public HTTPHandler getProxyHandler() {
		return new HTTPProxyHandler();
	}

}
