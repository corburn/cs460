/**
 * The server should be started before the client. The server has one required and one optional commandline argument.
 * The port number argument is required and indicates on which port the server should listen for connections.
 * The --threaded flag is optional. If present, the server will create a separate thread for each client connection.
 *
 * Usage:
 * java ProxyServerThread [--threaded] <port number>
 *
 * Example:
 * java ProxyServerThread --threaded 8080
 *
 *
 * 1. parse Request
 * 2. cache.get(request.URL())
 * 3. if null? conn.getContent()
 * cache.put(content);
 */
import java.io.*;
import java.net.*;

/**
 * ProxyServerThread implements the CS460 Project 1 ProxyServerThread protocol.
 */
public class ProxyServerThread implements Runnable {

    Socket clientSocket;
    HttpRequest req;
    HttpResponse res;
    PrintWriter out;

    public ProxyServerThread(Socket clientSocket) throws IOException, URISyntaxException {
        this.clientSocket = clientSocket;
//        DataInputStream in = new DataInputStream(clientSocket.getInputStream());
//        String requestLine = in.readLine();

//        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
//        try {
//            this.req = new HttpRequest(clientSocket.getInputStream());
//        } catch(IOException e) {
//            System.err.println("IOException: " + e.getMessage());
//            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
//            out.println("HTTP/1.1 400 Bad Request");
//            out.println("Connection: close");
//            out.println("");
//            clientSocket.close();
//        }
    }

    public void sendBadRequest(OutputStream stream) {
        PrintWriter out = new PrintWriter(stream, true);
        out.println("HTTP/1.0 400 Bad Request");
        out.println("Connection: close");
        out.println("");
    }

    /**
     * run handles a single client connection.
     */
    public void run() {
        BufferedReader clientIn = null;
        OutputStream clientOut = null;
        try {
            clientIn = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            clientOut = this.clientSocket.getOutputStream();
            String requestLine = clientIn.readLine();
            if(requestLine == null) {
                System.err.println("ClientSocket requestLine null");
                sendBadRequest(clientOut);
                this.clientSocket.close();
                return;
            }
            String[] reqLine = requestLine.split(" ");
            if(reqLine.length != 3) {
                System.err.println("Expected clientSocket requestLine to have 3 parts: " + requestLine);
                sendBadRequest(clientOut);
                this.clientSocket.close();
                return;
            }
            if(!reqLine[0].toUpperCase().equals("GET")) {
                System.err.println("Ignoring non-GET request method: " + requestLine);
                sendBadRequest(clientOut);
                this.clientSocket.close();
                return;
            }
            URI uri = new URI(reqLine[1]);
            int port = uri.getPort();
            if(port == -1) {
                port = 80;
            }
            System.out.println("Connecting to " + uri.getHost() + ":" + port);
//            Socket serverSocket = new Socket(uri.getHost(), port);
            Socket serverSocket = new Socket("206.191.14.108", port);
            PrintWriter serverOut = new PrintWriter(serverSocket.getOutputStream(), true);
            String modRequestLine = reqLine[0] + " " + reqLine[1] + " " + "HTTP/1.0";
            System.out.println("Forwarding downgraded client request line: " + modRequestLine);
            serverOut.println(modRequestLine);

            System.out.println("Forwarding client headers");
            String header;
            while((header = clientIn.readLine()) != null) {
                if(!header.equals("")) {
                    System.out.println("\t" + header);
                    serverOut.println(header);
                } else {
                    System.out.println("Breaking on empty client header");
                    break;
                }
            }
//            serverOut.println("\n");
//            System.out.println("\tConnection: close\n\n");
//            serverOut.println("Connection: close");
            serverOut.println("\n");

            System.out.println("Reading server response");
            BufferedReader serverIn = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            while((header = serverIn.readLine()) != null) {
                if(header.equals("")) {
                    System.out.println("Breaking on empty server header");
                    break;
                }
                System.out.println("\t" + header);
            }
            System.out.println("DONE " + requestLine);

        } catch (IOException e) {
//            e.printStackTrace();
            System.err.println("clientSocket.getInputStream IOException");
            return;
        } catch (URISyntaxException e) {
//            e.printStackTrace();
            System.err.println("clientSocket requestLine URISyntaxException");
        }


//        URI uri = req.getUri();
//        int port = uri.getPort();
//        if(port == -1) {
//            port = 80;
//        }
//
//        System.out.println("Opening socket with " + uri.getHost() + ":" + port);
//        try {
//            Socket serverSocket = new Socket(uri.getHost(), port);
//        } catch(UnknownHostException e) {
////            e.printStackTrace();
//            System.err.println("UnknownHostException: " + req.getRequestLine());
//            out.println("HTTP/1.1 400 Bad Request");
//            out.println("Connection: close");
//            out.println("");
////            this.clientSocket.close();
//            return;
//        } catch(ConnectException e) {
//            System.err.println("ConnectionException: " + req.getRequestLine());
//            out.println("HTTP/1.1 400 Bad Request");
//            out.println("Connection: close");
//            out.println("");
////            clientSocket.close();
//            return;
//        } catch (IOException e) {
//            System.err.println("IOException: " + req.getRequestLine());
//            out.println("HTTP/1.1 400 Bad Request");
//            out.println("Connection: close");
//            out.println("");
//            return;
//        }
    }
}