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

    private byte[] fetchResource(String host, int port, byte[] request) throws IOException {
        Socket serverSocket = new Socket(host, port);
        HttpResponse response = new HttpResponse(serverSocket);
        response.forward(request);
        return response.getBytes();
    }
//
    private void sendResource(ProxyCache cache, HttpRequest request) throws IOException {
        String host = request.getHost();
        String path = request.getPath();
        byte[] content;
        try {
            content = cache.getResource(host, path);
            System.out.println("Cache hit: " + host + path);
        } catch (NoSuchFileException e) {
            System.err.println("Cache miss: " + host + path);
            content = this.fetchResource(host, request.getPort(), request.getBytes());
            cache.setResource(host, path, content);
        }

        request.send("Content-Type: text/html", "<h1>Hello World</h1>".getBytes());
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

        // Return the cached resource, fetching it from the remote server if necessary.
        try {
            sendResource(this.cache, request);
        } catch (IOException e) {
            e.printStackTrace();
            request.sendBadRequest();
        }

//        BufferedReader clientIn = null;
//        OutputStream clientOut = null;
//        try {
//            clientIn = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
//            clientOut = this.clientSocket.getOutputStream();
//            String requestLine = clientIn.readLine();
//            if(requestLine == null) {
//                System.err.println("ClientSocket requestLine null");
//                sendBadRequest(clientOut);
//                this.clientSocket.close();
//                return;
//            }
//            String[] reqLine = requestLine.split(" ");
//            if(reqLine.length != 3) {
//                System.err.println("Expected clientSocket requestLine to have 3 parts: " + requestLine);
//                sendBadRequest(clientOut);
//                this.clientSocket.close();
//                return;
//            }
//            if(!reqLine[0].toUpperCase().equals("GET")) {
//                System.err.println("Ignoring non-GET request method: " + requestLine);
//                sendBadRequest(clientOut);
//                this.clientSocket.close();
//                return;
//            }
//            URI uri = new URI(reqLine[1]);
//            int port = uri.getPort();
//            if(port == -1) {
//                port = 80;
//            }
//        System.out.println("Connecting to " + uri.getHost() + ":" + port);
//        Socket serverSocket = new Socket(uri.getHost(), port);
//        PrintWriter serverOut = new PrintWriter(serverSocket.getOutputStream(), true);
//        String modRequestLine = reqLine[0] + " " + reqLine[1] + " " + "HTTP/1.0";
//        System.out.println("Forwarding downgraded client request line: " + modRequestLine);
//        serverOut.println(modRequestLine);

//        System.out.println("Forwarding client headers");
//        String header;
//        while ((header = clientIn.readLine()) != null) {
//            if (!header.equals("")) {
//                System.out.println("\t" + header);
//                serverOut.println(header);
//            } else {
//                System.out.println("Breaking on empty client header");
//                break;
//            }
//        }
//        serverOut.println("\n");
//
//        System.out.println("Reading server response");
//        DataInputStream serverIn = new DataInputStream(serverSocket.getInputStream());
//        DataOutputStream clientOutd = new DataOutputStream(clientOut);
//        byte[] buf = new byte[8192];
//        int bytesRead = 0;
//        while ((bytesRead = serverIn.read(buf)) != -1) {
//            clientOutd.write(buf, 0, bytesRead);
//        }
//        System.out.println("DONE " + requestLine);

    }
}