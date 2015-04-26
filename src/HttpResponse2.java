//import java.io.DataInputStream;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.net.Socket;
//
//public class HttpResponse {
//    DataInputStream in;
//    PrintWriter out;
//
//    public HttpResponse(Socket socket) throws IOException {
//        in = new DataInputStream(socket.getInputStream());
//        out = new PrintWriter(socket.getOutputStream(), true);
//    }
//
//    public void forward(String request) {
//        this.out.write(request);
//
////        System.out.println("Forwarding client headers");
////        String header;
////        while ((header = clientIn.readLine()) != null) {
////            if (!header.equals("")) {
////                System.out.println("\t" + header);
////                serverOut.println(header);
////            } else {
////                System.out.println("Breaking on empty client header");
////                break;
////            }
////        }
////        serverOut.println("\n");
//    }
//}
