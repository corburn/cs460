import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

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
        return this.requestLine.getUri().getPort();
    }

    public String getPath() {
        return this.requestLine.getUri().getPath();
    }

//    public byte[] getBytes() {
//
//    }

    public void send(byte[] header, byte[] resource) throws IOException {
        this.out.write(header);
        this.out.write(resource);
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
            System.out.println("RequestLine(" + line + ")");

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
