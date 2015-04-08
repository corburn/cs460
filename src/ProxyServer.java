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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * ProxyServer implements the CS460 Project 1 ProxyServer protocol.
 */
public class ProxyServer implements Runnable {

    Socket clientSocket;

    /**
     * @param clientSocket
     */
    public ProxyServer(java.net.Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     * printUsage prints a usage message and exits.
     */
    public static void printUsage() {
        System.err.println("Usage: java ProxyServer [--threaded] <port number>");
        System.exit(1);
    }

    /**
     * main parses the commandline arguments, opens a ServerSocket, and listens for client connections.
     * @param args
     */
    public static void main(String[] args) {
        List<String> arguments = new ArrayList<String>(Arrays.asList(args));
        boolean isThreaded = false;
        int portNumber = 8080;


        // Parse optional argument.
        if(arguments.contains("--threaded")) {
            isThreaded = true;
            arguments.remove("--threaded");
        }

        // After removing the optional argument, only the required argument should remain.
        if (arguments.size() != 1) {
            printUsage();
        }

        // Parse required argument.
        try {
            portNumber = Integer.parseInt(arguments.get(0));
        } catch(NumberFormatException e) {
            printUsage();
        }

        // Listen for client connections.
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            while(true) {
                // Each connection will be handled in a separate thread if the --threaded flag was used,
                // otherwise, the server will be single-threaded.
                if(isThreaded) {
                    (new Thread(new ProxyServer(serverSocket.accept()))).start();
                } else {
                    new ProxyServer(serverSocket.accept()).run();
                }
            }
        } catch (java.io.IOException e) {
            // TODO: Write to log file.
        }
    }

    /**
     * run handles a single client connection.
     */
    public void run() {
        PrintWriter out;
        BufferedReader in;
        String input;

        try {
            out = new PrintWriter(this.clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));


            // Respond to client input until they send "bye\n".
            while ((input = in.readLine()) != null) {
                System.out.println(in.readLine());
//                if (input.toLowerCase().equals("bye")) {
//                    break;
//                }
//
//                try {
//                    if(Integer.parseInt(input) % 2 == 0) {
//                        out.println("even");
//                    } else {
//                        out.println("odd");
//                    }
//                } catch (NumberFormatException e) {
//                    out.println(input + " is not an integer");
//                }
            }
//
//            out.println("bye");
        } catch(java.io.IOException e) {
            // TODO: Write to log file.
        }
    }
}
