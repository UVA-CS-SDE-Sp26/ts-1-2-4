package interfaces;
import java.io.IOException;

public interface FileReaderServiceInterface {
    /**
     * Reads the content of a file at the given path.
     * @param path The absolute or relative path to the file.
     * @return The content of the file as a String.
     * @throws IOException If the file cannot be read.
     */
    String readFile(String path) throws IOException;
}
