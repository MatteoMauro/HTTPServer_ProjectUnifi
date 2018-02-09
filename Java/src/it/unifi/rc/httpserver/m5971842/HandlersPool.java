package it.unifi.rc.httpserver.m5971842;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import it.unifi.rc.httpserver.HTTPHandler;
import it.unifi.rc.httpserver.HTTPReply;
import it.unifi.rc.httpserver.HTTPRequest;

/**
 * HandlersPool manages a set of HTTPHandlers, which they can be added every
 * time to the set. It is implemented as a Monitor since it will be shared by
 * every thread.
 * 
 * @author Matteo Mauro 5971842
 *
 */
public class HandlersPool {

	private List<HTTPHandler> handlers;

	public synchronized void add(HTTPHandler handler) {
		// instantiate the list only when there's an handler to add
		if (this.handlers == null)
			this.handlers = new ArrayList<>();
		handlers.add(handler);
	}

	/**
	 * Delegates to the HTTPHandlers the request: if none is able to handle,
	 * then null is returned.
	 * 
	 * @param request
	 * @return
	 */
	public synchronized HTTPReply handle(HTTPRequest request) {
		if (handlers == null)
			// no handlers have been added to pool
			return null;
		HTTPReply reply = null;
		Iterator<HTTPHandler> iterator = handlers.iterator();
		// determine if request has been successfully managed
		boolean handled = false;
		while (iterator.hasNext() && !handled) {
			// get the reply and check if it has been managed
			if ((reply = iterator.next().handle(request)) != null)
				handled = true;
		}
		log(request, reply);
		return reply;
	}

	/**
	 * Perform log operation, delegating it to another thread.
	 * 
	 * @param request
	 *            saved even if it was semantically wrong.
	 * @param reply
	 *            may be null
	 */
	private void log(HTTPRequest request, HTTPReply reply) {
		Runnable logThread = () -> {
			PrintWriter writer = null;
			try {
				// access local file "log.txt", retrieve in /bin/log/ folder
				File log = new File(
						getClass().getResource(File.separator + "log" + File.separator + "log.txt").getPath());
				writer = new PrintWriter(new FileOutputStream(log, true));
				String time = getTime();
				writer.append("--------------------------------------------\n");
				writer.append("REQUEST - " + time + "\n");
				writer.append(request.toString() + "\n\n");
				if (reply != null) {
					writer.append("REPLY - " + time + "\n");
					writer.append(reply.toString() + "\n");
				}
				writer.append("--------------------------------------------\n\n");
				writer.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (writer != null)
					writer.close();
			}
		};
		new Thread(logThread).start();
	}

	private String getTime() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ITALY);
		dateFormat.setTimeZone(TimeZone.getTimeZone("CET"));
		return dateFormat.format(calendar.getTime());
	}

}
