package interfaces;

import java.io.IOException;

public interface FileReaderServiceInterface {
    String readFile(String path) throws IOException;
}
