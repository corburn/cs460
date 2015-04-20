import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class HttpResponse {
    DataInputStream serverIn;
    DataOutputStream clientOutd;

    public HttpResponse(Socket socket, OutputStream out ) throws IOException {
        serverIn = new DataInputStream(socket.getInputStream());
        clientOutd = new DataOutputStream(out);
  
    System.out.println("Reading server response");
    //DataInputStream serverIn = new DataInputStream(serverSocket.getInputStream());
    //DataOutputStream clientOutd = new DataOutputStream(out);
    String input;
    while((input = serverIn.readLine()) != null) {
    	
    	//Obtain Content Length
        String ContentLength;
    		if(input.toLowerCase().startsWith("content-length:"))
    		{
    			String[] conleng = input.split(" ");
    			ContentLength = conleng[1];
    			System.out.println(ContentLength);
    		}
        if(input.equals("")) {
            clientOutd.write("Connection: close\r\n\r\n".getBytes());
            break;
        }
        clientOutd.write((input + "\r\n").getBytes());
    }
    byte[] buf = new byte[8192];
    int bytesRead = 0;
    while((bytesRead = serverIn.read(buf)) != -1) {
        clientOutd.write(buf, 0, bytesRead);
    }
    
    }
}
