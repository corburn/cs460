//import java.io.*;
//import java.net.Socket;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * HttpRequest represents a client HTTP request such as the following example:
// * <p/>
// * GET http://localhost:8080/tienda1/imagenes/3.gif/ HTTP/1.1
// * User-Agent: Mozilla/5.0 (compatible; Konqueror/3.5; Linux) KHTML/3.5.8 (like Gecko)
// * Pragma: no-cache
// * Cache-control: no-cache
// * Accept: text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*\/*;q=0.5
// * Accept-Encoding: x-gzip, x-deflate, gzip, deflate
// * Accept-Charset: utf-8, utf-8;q=0.5, *;q=0.5
// * Accept-Language: en
// * Host: localhost:8080
// * Cookie: JSESSIONID=FB018FFB06011CFABD60D8E8AD58CA21
// * Connection: close
// * <p/>
// * <body omitted>
// *
// * @see java.net.ResponseCache#get(URI, String, Map<String, List<String>>)
// */
//public class HttpRequest {
////    StringBuilder b;
////    StringBuffer c;
////    PrintWriter out;
//    DataInputStream in;
//
//    private RequestLine requestLine;
//    private Map<String, List<String>> headers;
//
//    public HttpRequest(InputStream stream) throws IOException, URISyntaxException {
//        DataInputStream in = new DataInputStream(stream);
//        String requestLine = in.readLine();
//        if(requestLine == null) {
//            throw new IOException("The request line is null");
//        }
//        this.requestLine = new RequestLine(requestLine);
//        this.headers = parseHeaders(in);
//    }
//
//    private Map<String, List<String>> parseHeaders(DataInputStream in) throws IOException {
//        HashMap<String, List<String>> headers = new HashMap<String, List<String>>();
//        String line;
//        int count = 0;
//
//        // Read headers until end of stream or an empty line delimiting the message body.
//        while((line = in.readLine()) != null) {
//            if(line.isEmpty()) {
//                break;
//            }
//
//            System.out.println(++count + line);
//
////            String[] header = line.split(":", 1);
////            // TODO: Throw exception if unexpected header format
//////            if(header.length != 2) {
//////                throw
//////            }
////            System.out.println("readLine: " + header.toString());
////            String key = header[0];
////            List<String> values = Arrays.asList(header[1].split(","));
////            headers.put(key, values);
//        }
//
//        return headers;
//    }
//
////    /**
////     * @param in
////     * @return
////     * @throws IOException
////     */
////    private Map<String, List<String>> parseHeaders(InputStream stream) throws IOException {
////        InputStreamReader in = new InputStreamReader(stream);
////        StringBuilder buffer = new StringBuilder();
////        int rawChar;
////        char parsedChar;
////        boolean isNewline = false;
////        // While we have not reached the end of the input stream.
////        while((rawChar = in.read()) != -1) {
////            parsedChar = (char)rawChar;
////            if(parsedChar == '\r') {
////
////            }
////            buffer.append(parsedChar);
////        }
////        String line;
////        HashMap<String, List<String>> headers = new HashMap<String, List<String>>();
////
////        reader.read();
////
////        while ((line = in.readLine()) != null) {
////            // The header is separated from the body by a newline.
////            if (line.equals("")) {
////                break;
////            }
////            // key: value,value,value
////            String[] header = line.split(": ");
////            String key = header[0];
////            List<String> values = Arrays.asList(header[1].split(", ?"));
////            headers.put(key, values);
////        }
////
////        return headers;
////    }
////
////    /**
////     * http://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html
////     *
////     * @param in
////     * @return
////     */
////    private String parseRequest(BufferedReader in) throws IOException {
////        // TODO
////        String input;
////
////        // input will be null if the end of the input stream is reached.
////        while ((input = in.readLine()) != null) {
////
////        }
////
////        return "";
////    }
////
//    public URI getUri() {
//        // TODO: handle relative URI?
//        return this.requestLine.getUri();
//    }
//
//    public String getMethod() {
//        return this.requestLine.getMethod();
//    }
//
//    public Map<String, List<String>> getHeaders() {
//        return this.headers;
//    }
//
//    public String getRequestLine() {
//        return this.requestLine.toString();
//    }
//
//    /**
//     * RequestLine parses an HTTP Request Line such as:
//     * GET http://localhost:8080/tienda1/imagenes/3.gif/ HTTP/1.1
//     * <p/>
//     * See http://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html#sec5.1
//     */
//    private class RequestLine {
//        String method;
//        URI uri;
//        String protocol;
//
//        String requestLine;
//
//        public RequestLine(String line) throws URISyntaxException {
//            System.out.println("RequestLine(" + line + ")");
//            requestLine = line;
//
//            String[] parts = line.split(" ");
//            this.method = parts[0];
//            String uri = parts[1];
//            // Assume scheme is http for URI such as 127.0.0.1:8080
//            if(!uri.toLowerCase().startsWith("http")) {
//                uri = "http://" + parts[1];
//            }
//            this.uri = new URI(uri);
//            this.protocol = parts[2];
//        }
//
//        public String getMethod() {
//            return this.method;
//        }
//
//        public URI getUri() {
//            return this.uri;
//        }
//
//        public String getProtocol() {
//            return this.protocol;
//        }
//
//        public String toString() {
//            return this.requestLine;
//        }
//    }
//}
