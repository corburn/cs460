/**
 * The server should be started before the client. The has two required commandline arguments.
 * The hostname is the server hostname.
 * The port number argument is the port on which the server is listening.
 *
 * Usage:
 * java EvenOddClient <hostname> <port number>
 *
 * Example:
 * java EvenOddClient localhost 8080
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
import java.net.Socket;

/**
 * EvenOddClient implements the CS460 Project 1 EvenOddServer protocol.
 */
public class EvenOddClient {

    /**
     * printUsage prints a usage message and exits.
     */
    public static void printUsage() {
        System.err.println("Usage: java Client <hostname> <port number>");
        System.exit(1);
    }

    /**
     * main parses the commandline arguments and connects to the server.
     */
    public static void main(String[] args) {
        String hostName;
        int portNumber = 8080;
        String response;

        if (args.length != 2) {
            printUsage();
        }

        hostName = args[0];
        try {
            portNumber = Integer.parseInt(args[1]);
        } catch(NumberFormatException e) {
            System.err.println("The port number must be an integer.");
            printUsage();
        }

        try (
                Socket echoSocket = new Socket(hostName, portNumber);
                PrintWriter out =
                        new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader in =
                        new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
                BufferedReader stdIn =
                        new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Client connected to server.");
            System.out.print("Please enter an integer: ");
            while(true) {
                out.println(stdIn.readLine());
                response = in.readLine();
                System.out.println(response);
                if(response.equals("bye")) {
                    break;
                }
            }
        } catch(java.net.UnknownHostException e) {
            System.err.println("The IP address of the hostname could not be determined.");
            printUsage();
        } catch(java.io.IOException e) {
            System.err.println("The connection was closed.");
            System.exit(1);
        }
    }
}
