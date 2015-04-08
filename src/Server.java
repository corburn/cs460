/**
 * @author Jason Travis
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server {
    protected DatagramSocket socket = null;
    protected BufferedReader in = null;

    public static void main(String[] args) throws SocketException {
        if (args.length != 1) {
            System.out.println("Usage: java Server <port>");
            return;
        }

        Server server = new Server(8080);
        server.run();
    }

    public Server(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }

    public void run() {
        while(true) {
            try {
                byte[] buf = new byte[256];

                // receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                String msg = new String(packet.getData(), 0, packet.getLength());

                System.out.println("Message received: " + msg);

                try {
                    int number = Integer.parseInt(msg);
                    if (number % 2 == 0) {
                        buf = "even".getBytes();
                    } else {
                        buf = "odd".getBytes();
                    }
                } catch (NumberFormatException e) {
                    msg = msg + " is not a number";
                    buf = msg.getBytes();
                }

                System.out.println("Message to send: " + new String(buf, 0, buf.length));

                // send the response to the client at "address" and "port"
                packet = new DatagramPacket(buf, buf.length, packet.getAddress(), packet.getPort());
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
//        socket.close();
    }

}
