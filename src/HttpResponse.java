import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class HttpResponse {
    DataInputStream in;
    DataOutputStream out;
    ArrayList<String> headers;
    byte[] body;

    static final String CONTENT_LENGTH = "content-length";

    public HttpResponse(Socket socket, byte[] request) throws IOException {
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());

        forward(request);

        this.headers = parseHeaders(this.in);

        // The content-length header, if present, is guaranteed to be the last element of the headers.
        int contentLength = -1;
        if (headers.size() > 0) {
            contentLength = getContentLength(headers.get(headers.size() - 1));
            if(contentLength == -1) {
                this.headers.remove(headers.size() - 1);
            }
        }

        this.body = parseBody(contentLength);
    }

    // forward takes a client request and forwards it to the remote server.
    private void forward(byte[] request) throws IOException {
        this.out.write(request);
    }

    /**
     * Parse the headers from the HTTP response by reading until either an empty line or
     * the end of the stream. The content-length is appended last so it can easily be found.
     * Content-length will be an empty string if it not present.
     * @return ArrayList of headers
     * @throws IOException
     */
    private ArrayList<String> parseHeaders(DataInputStream in) throws IOException {
        ArrayList<String> headers = new ArrayList<>();
        String contentLength = "";
        // Parse content length/type from headers.
        String input;
        while((input = in.readLine()) != null) {
            if(input.equals("")) {
                // End of header section
                break;
            }

            String lower = input.toLowerCase();
            if(lower.startsWith("alternate-protocol")) {
                continue;
            }

            if(lower.startsWith("connection")) {
                continue;
            }

            if(lower.startsWith("upgrade")) {
                continue;
            }

            if(lower.startsWith("content-length")) {
                // content-length will be the last element whether it exists or not.
                contentLength = input;
                continue;
            }

            headers.add(input);
        }

        headers.add(contentLength);

        System.out.println("HttpResponse.parseHeaders" + headers);

        return headers;
    }

    /**
     *
     * @param contentLengthHeader
     * @return The content length as an integer, or -1 if it could not be determined.
     */
    private int getContentLength(String contentLengthHeader) {
        // The header was not present.
        if(contentLengthHeader.equals("")) {
            System.err.println("The content-length header was not found");
            return -1;
        }

        String[] split = contentLengthHeader.split(":");

        // The header is delimited by a semicolon.
        if(split.length != 2) {
            System.err.println("Expected the content-length header to be delimited by a semicolon: " + contentLengthHeader);
            return -1;
        }

        try {
            return Integer.valueOf(split[1].trim());
        } catch(Exception e) {
            // Probably NumberFormatException, but it doesn't really matter at this point.
            System.err.println("Could not determine the content-length integer value: " + contentLengthHeader);
            return -1;
        }
    }

    /**
     * parseBody reads the body using a buffer the size of the content-length,
     * otherwise it uses a default value and reads the stream to the end.
     * @param contentLength The body length or a negative value if unknown.
     * @return response body as a byte[]
     * @throws IOException
     */
    private byte[] parseBody(int contentLength) throws IOException {
        // Special case - empty body such as in a redirect.
        if(contentLength == 0) {
            return new byte[0];
        }

        contentLength = contentLength < 0 ? 4096 : contentLength;
        ByteArrayOutputStream body = new ByteArrayOutputStream(contentLength);
        byte[] buf = new byte[contentLength];

        int bytesRead = 0;
        while((bytesRead = this.in.read(buf)) != -1) {
            body.write(buf, 0, bytesRead);
        }

        return body.toByteArray();
    }

    // getBytes returns the filtered client request headers.
    public byte[] getBody() {
        return this.body;
    }

    public ArrayList<String> getHeaders() {
        return this.headers;
    }
}
