import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class ProxyCache {

    public ProxyCache() {

    }

//    public String getHeaders(String server, String resourcePath) {
//
//    }

    // Used to see if the requested resource is already in the cache.
    // If so, the resource is returned as an array of bytes.
    // Otherwise the byte array returned is empty.
    public byte[] getResource(String server, String resourcePath) throws IOException {
        Path path = FileSystems.getDefault().getPath(server + "/" + resourcePath);
        return Files.readAllBytes(path);
    }

    // Used to save a resource
    public void setResource(String server, String resourcePath, byte[] resource) throws IOException {
        File file = new File(server + "/" + resourcePath);
        file.getParentFile().mkdirs();
        FileOutputStream stream = new FileOutputStream(file, true);
        stream.write(resource);
    }
}
