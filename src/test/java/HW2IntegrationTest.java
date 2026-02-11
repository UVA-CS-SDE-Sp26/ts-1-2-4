import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HW2IntegrationTest {
    private Path dataDir;
    private boolean createdDataDir;
    private final List<Path> createdFiles = new ArrayList<>();

    @BeforeEach
    void setUp() throws IOException {
        dataDir = Paths.get("data");
        createdDataDir = Files.notExists(dataDir);
        if (createdDataDir) {
            Files.createDirectories(dataDir);
        }
        createdFiles.clear();
    }

    @AfterEach
    void tearDown() throws IOException {
        for (Path file : createdFiles) {
            Files.deleteIfExists(file);
        }
        if (createdDataDir && Files.exists(dataDir)) {
            try (var stream = Files.list(dataDir)) {
                if (stream.findAny().isEmpty()) {
                    Files.deleteIfExists(dataDir);
                }
            }
        }
    }

    @Test
    void listFiles_integration() throws IOException {
        String fileA = createDataFile("hw2_it_alpha.txt", "bcd");
        String fileB = createDataFile("hw2_it_beta.txt", "efg");

        ProgramController controller = new ProgramController(
            new FileCatalog(),
            new FileReaderService(),
            new CipherService()
        );

        String output = controller.listFiles();
        assertTrue(output.contains(" " + fileA + "\n"));
        assertTrue(output.contains(" " + fileB + "\n"));
    }

    @Test
    void showFile_integration_withKey() throws IOException {
        String filename = createDataFile("hw2_it_secret.txt", "bcd");
        FileCatalog catalog = new FileCatalog();
        List<String> files = catalog.listFiles();
        int index = files.indexOf(filename) + 1;
        assertTrue(index > 0, "Expected file to be present in catalog list");

        ProgramController controller = new ProgramController(
            catalog,
            new FileReaderService(),
            new CipherService()
        );

        String output = controller.showFile(index, Optional.of("ciphers/key.txt"));
        assertEquals("abc", output);
    }

    private String createDataFile(String baseName, String content) throws IOException {
        String filename = uniqueFilename(baseName);
        Path path = dataDir.resolve(filename);
        Files.writeString(path, content);
        createdFiles.add(path);
        return filename;
    }

    private String uniqueFilename(String baseName) throws IOException {
        String name = baseName;
        String stem = baseName;
        String ext = "";
        int dot = baseName.lastIndexOf('.');
        if (dot > 0) {
            stem = baseName.substring(0, dot);
            ext = baseName.substring(dot);
        }

        int counter = 1;
        while (Files.exists(dataDir.resolve(name))) {
            name = stem + "_" + counter + ext;
            counter++;
        }
        return name;
    }
}
