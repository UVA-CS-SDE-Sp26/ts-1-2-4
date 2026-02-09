import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import interfaces.FileReaderServiceInterface;

public class FileReaderService implements FileReaderServiceInterface {

    @Override
    public String readFile(String pathString) throws IOException {
        Path path = Paths.get(pathString);

        if (Files.exists(path) && !Files.isDirectory(path)) {
            // java.nio.file.Files provides a clean way to read all lines/content
            return Files.readString(path);
        } else {
            throw new IOException("File does not exist");
        }
    }
}
