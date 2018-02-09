/**
 * 
 */
package it.unifi.rc.httpserver.m5971842;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author Matteo Mauro
 *
 */
public class MyHTTPUtility {

	/**
	 * Returns a default HTTP header lines
	 * 
	 * @returna default HTTP header lines
	 */
	public static Map<String, String> getDefaultHeaderLines() {
		// get the Date and put into the map
		Map<String, String> headerLines = new HashMap<String, String>();
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ITALY);
		dateFormat.setTimeZone(TimeZone.getTimeZone("CET"));
		headerLines.put("Date", dateFormat.format(calendar.getTime()));
		return headerLines;
	}
}
