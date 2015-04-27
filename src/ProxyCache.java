import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class ProxyCache {

    HashMap<String, ArrayList<String>> headers;

    public ProxyCache() {
        this.headers = new HashMap<>();
    }

    private String getFilepath(String server, String resourcePath) {
        System.out.println(server + " " + resourcePath);
        if(resourcePath.equals("/")) {
            resourcePath = "/index.html";
        }
        return "cache" + File.separator + server + resourcePath;
    }

    // Used to see if the requested forward is already in the cache.
    // If so, the forward is returned as an array of bytes.
    public byte[] getResource(String server, String resourcePath) throws IOException {
        String filepath = getFilepath(server, resourcePath);
        System.out.println("Query cache for ./" + filepath);
        Path path = FileSystems.getDefault().getPath(filepath);
        return Files.readAllBytes(path);
    }

    public ArrayList<String> getHeaders(String server, String resourcePath) {
        return this.headers.get(getFilepath(server, resourcePath));
    }

    // Used to save a forward
    public byte[] setResource(String server, String resourcePath, byte[] resource) throws IOException {
        String filepath = getFilepath(server, resourcePath);
        System.out.println("Caching ./" + filepath);
        File file = new File(filepath);
        file.getParentFile().mkdirs();
        FileOutputStream stream = new FileOutputStream(file, true);
        stream.write(resource);
        return resource;
    }

    public ArrayList<String> setHeaders(String server, String resourcePath, ArrayList<String> headers) {
        this.headers.put(getFilepath(server, resourcePath), headers);
        return headers;
    }
}
