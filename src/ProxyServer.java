/**
 * The server should be started before the client. The server has one required and one optional commandline argument.
 * The port number argument is required and indicates on which port the server should listen for connections.
 * The --threaded flag is optional. If present, the server will create a separate thread for each client connection.
 *
 * Usage:
 * java ProxyServer [--threaded] <port number>
 *
 * Example:
 * java ProxyServer --threaded 8080
 *
 *
 * CS460 Project 1 instructions:
 * Write a client/server pair of programs that utilizes a reliable network connection using sockets.
 * The client will send strings of text to the server, each string representing an integer number.
 * The server will read the strings of text, extract the numbers and get back to the client,
 * saying either “odd” or “even”, also in plain text, depending on the nature of the numbers.
 *
 * From a timing perspective, the client will send one string of text with one number in it, wait for the server
 * to respond, output the result and send the next string to the server. The number to be checked will need to be read
 * from the standard input on the client’s side. If the client enters text that is not an integer, the server will
 * respond with "xxx is not an integer", where xxx stands for the text the client entered.
 *
 * Finally, if the client enters "bye", the server will respond by also saying "bye" and then shutdown the connection,
 * upon which the client needs to shutdown too.
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;


/**
 * ProxyServer implements the CS460 Project 1 ProxyServer protocol.
 */
public class ProxyServer {

    /**
     * printUsage prints a usage message and exits.
     */
    private static void printUsage() {
        System.err.println("Usage: java ProxyServer [--threaded] <port number>");
        System.exit(1);
    }

    /**
     * main parses the commandline arguments, opens a ServerSocket, and listens for client connections.
     * @param args
     */
    public static void main(String[] args) {
//        List<String> arguments = new ArrayList<String>(Arrays.asList(args));
//        boolean isThreaded = true;
        int portNumber = 8080;

        // Listen for client connections.
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        ProxyCache cache = new ProxyCache();

        while(true) {
            try {
                ProxyServerThread thread = new ProxyServerThread(cache, serverSocket.accept());
                new Thread(thread).start();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
