import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HttpRequest represents a client HTTP request such as the following example:
 * <p/>
 * GET http://localhost:8080/tienda1/imagenes/3.gif/ HTTP/1.1 User-Agent:
 * Mozilla/5.0 (compatible; Konqueror/3.5; Linux) KHTML/3.5.8 (like Gecko)
 * Pragma: no-cache Cache-control: no-cache Accept:
 * text/xml,application/xml,application
 * /xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*\/*;q=0.5
 * Accept-Encoding: x-gzip, x-deflate, gzip, deflate Accept-Charset: utf-8,
 * utf-8;q=0.5, *;q=0.5 Accept-Language: en Host: localhost:8080 Cookie:
 * JSESSIONID=FB018FFB06011CFABD60D8E8AD58CA21 Connection: close
 * 
 * <body omitted>
 * 
 * @see java.net.ResponseCache#get(URI, String, Map<String, List<String>>)
 */
public class HttpRequest {
	// StringBuilder b;
	// StringBuffer c;
	// PrintWriter out;
	
	Socket serverSocket;

	public HttpRequest(Socket clientSocket) throws IOException, URISyntaxException {
		/**
		 * run handles a single client connection.
		 */
		BufferedReader clientIn = null;
		OutputStream clientOut = null;
		try {
			clientIn = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			clientOut = clientSocket.getOutputStream();
			String requestLine = clientIn.readLine();
			if (requestLine == null) {
				System.err.println("ClientSocket requestLine null");
				sendBadRequest(clientOut);
				clientSocket.close();
				return;
			}
			String[] reqLine = requestLine.split(" ");
			if (reqLine.length != 3) {
				System.err
						.println("Expected clientSocket requestLine to have 3 parts: "
								+ requestLine);
				sendBadRequest(clientOut);
				clientSocket.close();
				return;
			}

			if (!reqLine[0].toUpperCase().equals("GET")) {
				System.err.println("Ignoring non-GET request method: "
						+ requestLine);
				sendBadRequest(clientOut);
				clientSocket.close();
				return;
			}
			URI uri = new URI(reqLine[1]);
			int port = uri.getPort();
			if (port == -1) {
				port = 80;
			}

			System.out.println("Connecting to " + uri.getHost() + ":" + port);
			serverSocket = new Socket(uri.getHost(), port);
			PrintWriter serverOut = new PrintWriter(
					serverSocket.getOutputStream(), true);
			String modRequestLine = reqLine[0] + " " + reqLine[1] + " "
					+ "HTTP/1.0";
			System.out.println("Forwarding downgraded client request line: "
					+ modRequestLine);
			serverOut.println(modRequestLine);

			System.out.println("Forwarding client headers");
			String header;
			while ((header = clientIn.readLine()) != null) {
				if (!header.equals("")) {
					System.out.println("\t" + header);
					serverOut.println(header);
				} else {
					System.out.println("Breaking on empty client header");
					break;
				}
			}
			serverOut.println("\n");
		} finally {
			

		}
	}

	public Socket getServerSocket() {
		return this.serverSocket;
	}
	
	public void sendBadRequest(OutputStream stream) {
		PrintWriter out = new PrintWriter(stream, true);
		out.println("HTTP/1.0 400 Bad Request");
		out.println("Connection: close");
		out.println("");

	}
}
