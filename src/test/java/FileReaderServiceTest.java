import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileReaderServiceTest {

    // Reading a valid file
    @Test
    void readFile_ReturnsContent_WhenFileExists(@TempDir Path tempDir) throws IOException {
        Path filePath = tempDir.resolve("test-data.txt");
        String expectedContent = "Hello, Team B!";
        Files.writeString(filePath, expectedContent);

        FileReaderService service = new FileReaderService();

        String actualContent = service.readFile(filePath.toString());

        assertEquals(expectedContent, actualContent, "The file content returned should match what was written.");
    }

    // File does not exist
    @Test
    void readFile_ThrowsIOException_WhenFileDoesNotExist(@TempDir Path tempDir) {
        Path missingPath = tempDir.resolve("ghost-file.txt");
        FileReaderService service = new FileReaderService();

        IOException exception = assertThrows(IOException.class, () -> {
            service.readFile(missingPath.toString());
        });

        assertEquals("File does not exist", exception.getMessage());
    }

    // Path is a directory
    @Test
    void readFile_ThrowsIOException_WhenPathIsDirectory(@TempDir Path tempDir) {
        FileReaderService service = new FileReaderService();

        IOException exception = assertThrows(IOException.class, () -> {
            service.readFile(tempDir.toString());
        });

        assertEquals("File does not exist", exception.getMessage());
    }
}