import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class HttpRequest {
    BufferedReader in;
    DataOutputStream out;
    RequestLine requestLine;

    public HttpRequest(Socket socket) throws URISyntaxException, IOException {
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new DataOutputStream(socket.getOutputStream());

        this.requestLine = new RequestLine(in.readLine());
    }

    public void sendBadRequest() {
        try {
            this.out.writeBytes("HTTP/1.0 400 Bad Request\r\nConnection: close\r\n\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendUnsupportedMethod() {
        try {
            this.out.writeBytes("HTTP/1.0 501 Unsupported Method\r\nConnection: close\r\n\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMethod() {
        return this.requestLine.getMethod();
    }

    public String getHost() {
        return this.requestLine.getUri().getHost();
    }

    public int getPort() {
        // Default to port 80 webtraffic if unspecified
        int port = this.requestLine.getUri().getPort();
        return port == -1 ? 80 : port;
    }

    public String getPath() {
        return this.requestLine.getUri().getPath();
    }

    // getBytes returns the request Header as a array of bytes.
    public byte[] getBytes() throws IOException {
        StringBuilder headers = new StringBuilder();

        headers.append(getFilteredRequestLine() + "\r\n");

        String input = "";
        while((input = this.in.readLine()) != null) {
            if(!input.equals("")) {
                headers.append(input + "\r\n");
            } else {
                // Blank line marks the end of the header section.
                headers.append("\r\n");
                break;
            }
        }
        System.out.println(headers);
        return headers.toString().getBytes();
    }

    public void send(ArrayList<String> headers, byte[] resource) throws IOException {
        ByteArrayOutputStream msg = new ByteArrayOutputStream();
        StringBuilder str = new StringBuilder();

        System.out.println("HttpRequest.send " + headers);

        for(String header : headers) {
            System.out.println("SEND" + header);
            str.append(header + "\r\n");
        }

        str.append("Content-Length: " + resource.length + "\r\n" +
                "Connection: close\r\n");
        this.out.writeBytes(str.toString());
        this.out.writeBytes("\r\n");
        this.out.write(resource);
    }

    private String getFilteredRequestLine() {
        return getMethod() + " " + this.requestLine.getUri().toString() + " HTTP/1.0";
    }

    /**
     * RequestLine parses an HTTP Request Line such as:
     * GET http://localhost:8080/tienda1/imagenes/3.gif/ HTTP/1.1
     *
     * See http://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html#sec5.1
     */
    private class RequestLine {
        String method;
        URI uri;
        String protocol;

        String requestLine;

        public RequestLine(String line) throws URISyntaxException, IOException {

            if(line == null) {
                throw new IOException("The client request line is null");
            }

            requestLine = line;

            String[] parts = line.split(" ");
            this.method = parts[0];
            String uri = parts[1];
            // Assume scheme is http for URI such as 127.0.0.1:8080
            if (!uri.toLowerCase().startsWith("http")) {
                uri = "http://" + parts[1];
            }
            this.uri = new URI(uri);
            this.protocol = parts[2];
        }

        public String getMethod() {
            return this.method;
        }

        public URI getUri() {
            return this.uri;
        }

        public String getProtocol() {
            return this.protocol;
        }

        public String toString() {
            return this.requestLine;
        }
    }
}
