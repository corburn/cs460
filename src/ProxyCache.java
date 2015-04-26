import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class ProxyCache {

//    HashMap<String, String> headers;

    public ProxyCache() {
//        this.headers = new HashMap<>();
    }

    // Used to see if the requested resource is already in the cache.
    // If so, the resource is returned as an array of bytes.
    public byte[] getResource(String server, String resourcePath) throws IOException {
        String filepath = "cache" + File.separator + server + resourcePath;
        System.out.println("Query cache for ./" + filepath);
        Path path = FileSystems.getDefault().getPath(filepath);
        return Files.readAllBytes(path);
    }

    // Used to save a resource
    public void setResource(String server, String resourcePath, byte[] resource) throws IOException {
        String filepath = "cache" + File.separator + server + resourcePath;
        System.out.println("Caching ./" + filepath);
        File file = new File(filepath);
        file.getParentFile().mkdirs();
        FileOutputStream stream = new FileOutputStream(file, true);
        stream.write(resource);
    }
}
