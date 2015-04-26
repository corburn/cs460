import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class HttpResponse implements Response {
    DataInputStream in;
    DataOutputStream out;

    public HttpResponse(Socket socket) throws IOException {
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
    }

    public void forward(byte[] request) throws IOException {
        this.out.write(request);
    }

    public String getHeader() {
        return "";
    }

    public byte[] getBytes() {
        // TODO
        return "".getBytes();
    }
}
