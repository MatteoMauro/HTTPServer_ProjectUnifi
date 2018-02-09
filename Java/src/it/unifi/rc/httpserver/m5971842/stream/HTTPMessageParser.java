package it.unifi.rc.httpserver.m5971842.stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.unifi.rc.httpserver.HTTPProtocolException;
import it.unifi.rc.httpserver.HTTPReply;
import it.unifi.rc.httpserver.HTTPRequest;
import it.unifi.rc.httpserver.m5971842.MyHTTPReply;
import it.unifi.rc.httpserver.m5971842.MyHTTPRequest;

/**
 * @author Matteo Mauro 5971842
 *
 *         HTTPMessageParser takes a stream and performs the parsing of the
 *         request/reply, checking if the syntax is correct (if it's not it can
 *         notify what went wrong throwing an exception). This class can
 *         efficiently perform the parsing for both type of messages
 *         (REPLY/REQUEST), it requires just to pass to constructor the Type.
 */
public class HTTPMessageParser {

	public static enum Type {
		REQUEST, REPLY
	};

	/*
	 * Regex defined for matching REQUEST: "<method> <path> HTTP/<version>"
	 * REPLY: "HTTP/<version> <status> <message>"
	 */
	private final Pattern patternFirstLine;
	// Regex defined for matching "<parameter>: <value>"
	private final Pattern patternHeaderLine = Pattern.compile("^.+: .+$");
	private BufferedReader reader;
	private final Type flagType;

	/**
	 * 
	 * @param reader
	 *            stream which contains the message
	 * @param type
	 *            can be any value belonging to Type enumeration
	 * @throws IllegalArgumentException
	 *             if Type value is not allowed
	 */
	public HTTPMessageParser(BufferedReader reader, Type type) throws IllegalArgumentException {
		if (reader == null || type == null)
			throw new IllegalArgumentException("Arguments must be not null.");
		this.reader = reader;
		// regex_patterns -> [0]="REQUEST" , [1]="REPLY"
		Pattern[] patterns = { Pattern.compile("^.+ .+ HTTP/1.[01]$"), Pattern.compile("^HTTP/1.[01] \\d+ [\\w\\s]+$") };
		switch (type) {
		case REQUEST:
			flagType = Type.REQUEST;
			this.patternFirstLine = patterns[0];
			break;
		case REPLY:
			flagType = Type.REPLY;
			this.patternFirstLine = patterns[1];
			break;
		default:
			throw new IllegalArgumentException(
					"type argument must be on of this: " + java.util.Arrays.asList(Type.values()));
		}
	}

	/**
	 * Request Line-> "<method> <path> HTTP/<version>" Header Lines->
	 * "<parameter>: <value>". Performs the parsing of the message and return an
	 * HTTPRequest.
	 * 
	 * @return HTTPRequest if client request is correctly expressed, otherwise
	 *         null
	 * @throws HTTPProtocolException
	 *             if request doesn't match the pattern protocol
	 * @throws UnsupportedOperationException
	 *             if this class has been constructed for other type of messages
	 */
	public HTTPRequest parseRequest() throws HTTPProtocolException, UnsupportedOperationException {
		// Check if this intance can perform this method according to his
		// construction
		if (flagType != Type.REQUEST) {
			throw new UnsupportedOperationException("HTTPMessageParser has been constructed only for HTTPRequest.");
		}
		HTTPRequest request = null;
		try {
			Map<String, String> headerLines = null;
			String body = null;
			String requestLine = parseRequestLine();
			if (requestLine != null) {
				// request line was correct, proceed to header section
				headerLines = parseHeadersLines();
				// headerLines can be empty if no header were present or correct
				// checking if there is a specified "Content-Length"
				if (headerLines.containsKey("Content-Length")) {
					long length = Long.parseLong(headerLines.get("Content-Length"));
					if (length != 0)
						body = readBody(length);
				}
				// temp-> [0] method, [1] path, [2] version
				String[] temp = splitRequestLine(requestLine);
				request = new MyHTTPRequest(temp[0], temp[1], temp[2], body, headerLines);
			}
		} catch (HTTPProtocolException e) {
			// forwarding exception to upper method...
			throw e;
		}
		return request;
	}

	/**
	 * Status Line-> "HTTP/<version> <status> <message>" Header Lines->
	 * "<parameter>: <value>". Performs the parsing of the message and return an
	 * HTTPReply.
	 * 
	 * @return HTTPReply if client request is correctly expressed, otherwise
	 *         null
	 * @throws HTTPProtocolException
	 *             if request doesn't match the pattern protocol
	 * @throws UnsupportedOperationException
	 *             if this class has been constructed for other type of messages
	 */
	public HTTPReply parseReply() throws HTTPProtocolException, UnsupportedOperationException {
		// Check if this instance can perform this method according to its
		// construction
		if (flagType != Type.REPLY) {
			throw new UnsupportedOperationException("HTTPMessageParser has been constructed only for HTTPREPLY.");
		}
		HTTPReply reply = null;
		try {
			Map<String, String> headerLines = null;
			String body = null;
			String requestLine = parseRequestLine();
			if (requestLine != null) {
				// request line was correct, proceed to header section
				headerLines = parseHeadersLines();
				// headerLines can be empty if no header were present or correct
				// checking if there is a specified "Content-Length"
				if (headerLines.containsKey("Content-Length")) {
					long length = Long.parseLong(headerLines.get("Content-Length"));
					if (length != 0)
						body = readBody(length);
				}
				// temp-> [0] version, [1] status, [2] message
				String[] temp = splitRequestLine(requestLine);
				reply = new MyHTTPReply(temp[0], temp[1], temp[2], body, headerLines);
			}
		} catch (HTTPProtocolException e) {
			// forwarding exception to upper method...
			throw e;
		}
		return reply;

	}

	/**
	 * Get the request line within the stream and parse it, in order to check
	 * the syntax. The request line is accepted if and only if respects the
	 * pattern.
	 * 
	 * @return string representing the request line if correctly expressed in
	 *         the message, otherwise null
	 * @throws HTTPProtocolException
	 *             syntax error of request line
	 */
	private String parseRequestLine() throws HTTPProtocolException {
		String requestLine = null;
		requestLine = readLine();
		if (requestLine != null) {
			Matcher matcher = patternFirstLine.matcher(requestLine);
			if (!matcher.matches()) {
				// RequestLine it's not correctly expressed
				throw new HTTPProtocolException(requestLine + "\nRequest line doesn't match HTTP protocol definition.");
			}
		}
		return requestLine;
	}

	/**
	 * Get the header lines within the stream and parse them in order to check
	 * the syntax. A header line is accepted if and only if respects the
	 * pattern.
	 * 
	 * @return map representing the tuples <parameter>:<value> of the header
	 *         section, lines not regularly expressed are ignored
	 * @throws HTTPProtocolException
	 *             syntax error of request line
	 */
	private Map<String, String> parseHeadersLines() throws HTTPProtocolException {
		Map<String, String> headerLines = new HashMap<>();
		String line;
		try {
			/*
			 * exit the loop when you find "\n" that separate header section
			 * from body
			 */
			while ((line = readLine()) != null && !line.equals("\n")) {
				Matcher matcher = patternHeaderLine.matcher(line);
				if (!matcher.matches()) {
					// header line it's not correctly expressed
					throw new HTTPProtocolException(
							line + "\nThis header line doesn't match HTTP protocol definition.");
				} else {
					// splitting key and value...
					StringTokenizer st = new StringTokenizer(line, ": ");
					String key = st.nextToken();
					String value = st.nextToken().replace("\n", "");
					headerLines.put(key, value);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return headerLines;
	}

	/**
	 * Reads an arbitrary line from the stream, the line must end with '\n'
	 * 
	 * @return String string representing the line read only if the line ends
	 *         with '\n'
	 */
	private String readLine() {
		StringBuffer buffer = new StringBuffer();
		String rst = null;
		int c;
		boolean lastLine = true;
		try {
			// read until '\n' or EOF have been reached
			while ((c = reader.read()) != '\n' && c != -1) {
				// ignore '\r'
				if (c != '\r') {
					// recognize that this isn't the last line of header section
					lastLine = false;
					buffer.append((char) c);
				}
			}
			// check if i have read the separator between hedaer and body
			if (lastLine && c == '\n') {
				// if the string returned is only "\n", then it is the last line
				// of the header section
				buffer.append('\n');
			}
			if (c == -1 && buffer.length() == 0)
				// nothing to return
				rst = null;
			else
				// I've read EOF but there are data to return
				rst = buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rst;
	}

	/**
	 * Return the content of the body as string, if length==0 it will not read
	 * anything and null will be returned.
	 * 
	 * @parameter length: specifies the size in byte of the body
	 * 
	 * @return the content of body as String, or null if length==0
	 */
	private String readBody(long length) {
		StringBuffer buffer = new StringBuffer();
		int c;
		long count = 0;
		try {
			// read all the content of the body until length bytes or EOF
			while (count < length && (c = reader.read()) != -1) {
				buffer.append((char) c);
				count++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String body = buffer.toString();
		return body.length() == 0 ? null : body;
	}

	/**
	 * Splits the request line in 3 part: -REQUEST: [0] method, [1] path, [2]
	 * version -REPLY: [0] version, [1] status, [2] message
	 * 
	 * @param requestLine
	 * @return return an array of strings representing the elements of request
	 *         line
	 * 
	 */
	private String[] splitRequestLine(String requestLine) {
		StringTokenizer st = new StringTokenizer(requestLine, " ");
		String[] rst = new String[3];
		rst[0] = st.nextToken().toString();
		rst[1] = st.nextToken().toString();
		// remove '\n'
		rst[2] = st.nextToken().toString().replace("\n", "");
		return rst;
	}
}
