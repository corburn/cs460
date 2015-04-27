/**
 * The server should be started before the client. The server has one required and one optional commandline argument.
 * The port number argument is required and indicates on which port the server should listen for connections.
 * The --threaded flag is optional. If present, the server will create a separate thread for each client connection.
 * <p/>
 * Usage:
 * java ProxyServerThread [--threaded] <port number>
 * <p/>
 * Example:
 * java ProxyServerThread --threaded 8080
 * <p/>
 * <p/>
 * 1. parse Request
 * 2. cache.get(request.URL())
 * 3. if null? conn.getContent()
 * cache.put(content);
 */

import java.io.*;
import java.net.*;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;

/**
 * ProxyServerThread implements the CS460 Project 1 ProxyServerThread protocol.
 */
public class ProxyServerThread implements Runnable {

    ProxyCache cache;
    Socket clientSocket;

    public ProxyServerThread(ProxyCache cache, Socket clientSocket) throws IOException, URISyntaxException {
        this.cache = cache;
        this.clientSocket = clientSocket;
    }

    // sendResource forwards the cached forward if it exists, otherwise it fetches the forward
    // before forwarding it to the client.
    private void sendResource(ProxyCache cache, HttpRequest request) throws IOException {
        String host = request.getHost();
        int port = request.getPort();
        String path = request.getPath();

        byte[] body;
        ArrayList<String> headers;

        try {
            // Get cached resource.
            headers = cache.getHeaders(host, path);
            body = cache.getResource(host, path);
//            if(headers == null || body == null) {
//                throw new NullPointerException("Headers: " + headers.toString() + " body: " + body.toString());
//            }
            System.out.println("Cache hit: " + host + path);
        } catch (NoSuchFileException | NullPointerException e) {
            // A cached copy of the resource does not exist.
            // fetch it from the remote server.
            System.err.println("Cache miss: " + host + path);
            Socket serverSocket = new Socket(host, port);
            HttpResponse response = new HttpResponse(serverSocket, request.getBytes());

            headers = cache.setHeaders(host, path, response.getHeaders());
            body = cache.setResource(host, path, response.getBody());

            System.out.println("CACHE MISS: " + headers + " " + body);
        }

        request.send(headers, body);

        this.clientSocket.close();
    }

    /**
     * run handles a single client connection.
     */
    public void run() {
        HttpRequest request = null;

        // Parse the incoming client request.
        try {
            request = new HttpRequest(this.clientSocket);
        } catch (URISyntaxException | IOException e) {
            System.err.println("Ignoring IOException");
//            e.printStackTrace();
            return;
        }

        // Ignore non GET requests.
        if(!request.getMethod().toUpperCase().equals("GET")) {
//            System.err.println("Ignore non-GET request: " + request.getMethod() + " " + request.getHost() + "/" + request.getPath() + ":" + request.getPort());
            request.sendUnsupportedMethod();
            return;
        }

        // Return the cached forward, fetching it from the remote server if necessary.
        try {
            sendResource(this.cache, request);
        } catch (IOException e) {
            e.printStackTrace();
            request.sendBadRequest();
        }

    }
}