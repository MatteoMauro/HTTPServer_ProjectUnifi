/**
 * 
 */
package it.unifi.rc.httpserver.m5971842;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import it.unifi.rc.httpserver.HTTPHandler;
import it.unifi.rc.httpserver.HTTPServer;

/**
 * @author Matteo Mauro 5971842
 *
 *         MyHTTPServer represents a multi-threaded server which can manage
 *         HTTPRequests.
 */
public class MyHTTPServer implements HTTPServer {

	/**
	 * HandlersPool is a monitor object that contains a set of handlers. This
	 * server is responsible of instantiate it, but it will be shared from every
	 * HTTPReplyThread.
	 */
	private HandlersPool handlers;
	private ServerSocket serverSocket;

	public MyHTTPServer(int port, int backlog, InetAddress address, HTTPHandler... handlers) throws IOException {
		serverSocket = new ServerSocket(port, backlog, address);
		for (HTTPHandler handler : handlers) {
			addHandler(handler);
		}
	}

	/**
	 * Add the handler to the pool of handlers.
	 * 
	 * @param handler
	 */
	@Override
	public void addHandler(HTTPHandler handler) {
		// instantiate only when there's an handler to add
		if (handlers == null)
			handlers = new HandlersPool();
		handlers.add(handler);
	}

	/**
	 * Start the server, every request triggers the creation of a
	 * HTTPReplyThread and makes the server returning to listen.
	 */
	@Override
	public void start() throws IOException {
		// Always listening and delegating requests to new Threads
		while (true) {
			System.out.println("Server listening...");
			Socket socket = serverSocket.accept();
			new Thread(new HTTPReplyThread(socket, handlers)).start();
			System.out.println("Request accepted.\n");
		}
	}

	@Override
	public void stop() {
		// when the server stops, there could be some socket still connected
		// with clients, but they will close themselves after sending the
		// replies
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
