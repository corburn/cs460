/**
 * @author Jason Travis
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Client {
    public static void printUsage() {
        System.out.println("Usage: java Client <hostname> <port>");
        System.exit(1);
    }

    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            printUsage();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        DatagramSocket socket = new DatagramSocket();
        InetAddress address = null;
        String input = "";
        int port = -1;
        try {
            address = InetAddress.getByName(args[0]);
            port = Integer.parseInt(args[1]);
        } catch(UnknownHostException e) {
            System.err.println("Unknown Host: " + args[0]);
            printUsage();
        } catch(NumberFormatException e) {
            System.err.println("Port must be an integer: " + args[1]);
            printUsage();
        }

        while(true) {
            System.out.print("Enter a number: ");
            input = in.readLine();

            if (input.toLowerCase().equals("bye")) {
                break;
            }

            // send request
            byte[] buf = input.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
            System.out.println("Sending message: " + new String(buf, 0, buf.length));
            socket.send(packet);

            buf = new byte[256];

            // get response
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            // display response
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Message received: " + received);
        }

        socket.close();
    }
}
