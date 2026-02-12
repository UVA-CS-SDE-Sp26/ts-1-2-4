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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HW2IntegrationTest {
    private Path dataDir;
    private boolean createdDataDir;
    private Path ciphersDir;
    private boolean createdCiphersDir;
    private final List<Path> createdPaths = new ArrayList<>();

    @BeforeEach
    void setUp() throws IOException {
        dataDir = Paths.get("data");
        ciphersDir = Paths.get("ciphers");
        createdDataDir = Files.notExists(dataDir);
        createdCiphersDir = Files.notExists(ciphersDir);
        if (createdDataDir) {
            Files.createDirectories(dataDir);
        }
        if (createdCiphersDir) {
            Files.createDirectories(ciphersDir);
        }
        createdPaths.clear();
    }

    @AfterEach
    void tearDown() throws IOException {
        for (Path file : createdPaths) {
            Files.deleteIfExists(file);
        }
        if (createdDataDir && Files.exists(dataDir)) {
            try (var stream = Files.list(dataDir)) {
                if (stream.findAny().isEmpty()) {
                    Files.deleteIfExists(dataDir);
                }
            }
        }
        if (createdCiphersDir && Files.exists(ciphersDir)) {
            try (var stream = Files.list(ciphersDir)) {
                if (stream.findAny().isEmpty()) {
                    Files.deleteIfExists(ciphersDir);
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
    void showFile_integration_withProvidedKey() throws IOException {
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

    @Test
    void showFile_integration_withDefaultKeyWhenOptionalPathMissing() throws IOException {
        String filename = createDataFile("hw2_it_default_key.txt", "bcd");
        FileCatalog catalog = new FileCatalog();
        int index = catalog.listFiles().indexOf(filename) + 1;
        assertTrue(index > 0);

        ProgramController controller = new ProgramController(
            catalog,
            new FileReaderService(),
            new CipherService()
        );

        String output = controller.showFile(index, Optional.empty());
        assertEquals("abc", output);
    }

    @Test
    void showFile_integration_invalidIndexReturnsFileNotFound() throws IOException {
        createDataFile("hw2_it_one_file.txt", "abc");

        ProgramController controller = new ProgramController(
            new FileCatalog(),
            new FileReaderService(),
            new CipherService()
        );

        String output = controller.showFile(9999, Optional.empty());
        assertEquals("File not found", output);
    }

    @Test
    void showFile_integration_invalidCustomKeyPathReturnsInvalidKey() throws IOException {
        String filename = createDataFile("hw2_it_bad_key_path.txt", "bcd");
        FileCatalog catalog = new FileCatalog();
        int index = catalog.listFiles().indexOf(filename) + 1;
        assertTrue(index > 0);

        ProgramController controller = new ProgramController(
            catalog,
            new FileReaderService(),
            new CipherService()
        );

        String output = controller.showFile(index, Optional.of("ciphers/does_not_exist.txt"));
        assertEquals("Invalid key", output);
    }

    @Test
    void showFile_integration_malformedCustomKeyReturnsInvalidKey() throws IOException {
        String filename = createDataFile("hw2_it_bad_key_content.txt", "bcd");
        String malformedKey = createCipherFile("hw2_it_bad.key", "abc\ncc");
        FileCatalog catalog = new FileCatalog();
        int index = catalog.listFiles().indexOf(filename) + 1;
        assertTrue(index > 0);

        ProgramController controller = new ProgramController(
            catalog,
            new FileReaderService(),
            new CipherService()
        );

        String output = controller.showFile(index, Optional.of(malformedKey));
        assertEquals("Invalid key", output);
    }

    @Test
    void showFile_integration_preservesUnmappedCharacters() throws IOException {
        String filename = createDataFile("hw2_it_symbols.txt", "bcd-!? 42\n");
        FileCatalog catalog = new FileCatalog();
        int index = catalog.listFiles().indexOf(filename) + 1;
        assertTrue(index > 0);

        ProgramController controller = new ProgramController(
            catalog,
            new FileReaderService(),
            new CipherService()
        );

        String output = controller.showFile(index, Optional.of("ciphers/key.txt"));
        assertEquals("abc-!? 31\n", output);
    }

    @Test
    void createDataFile_integration_generatesUniqueNameWhenFileExists() throws IOException {
        String first = createDataFile("hw2_it_dup.txt", "abc");
        String second = createDataFile("hw2_it_dup.txt", "def");

        assertNotEquals(first, second);
        ProgramController controller = new ProgramController(
            new FileCatalog(),
            new FileReaderService(),
            new CipherService()
        );
        String output = controller.listFiles();
        assertTrue(output.contains(" " + first + "\n"));
        assertTrue(output.contains(" " + second + "\n"));
    }

    private String createDataFile(String baseName, String content) throws IOException {
        String filename = uniqueFilename(baseName);
        Path path = dataDir.resolve(filename);
        Files.writeString(path, content);
        createdPaths.add(path);
        return filename;
    }

    private String createCipherFile(String baseName, String content) throws IOException {
        String filename = uniqueFilenameInDirectory(ciphersDir, baseName);
        Path path = ciphersDir.resolve(filename);
        Files.writeString(path, content);
        createdPaths.add(path);
        return ciphersDir.resolve(filename).toString();
    }

    private String uniqueFilename(String baseName) throws IOException {
        return uniqueFilenameInDirectory(dataDir, baseName);
    }

    private String uniqueFilenameInDirectory(Path directory, String baseName) throws IOException {
        String name = baseName;
        String stem = baseName;
        String ext = "";
        int dot = baseName.lastIndexOf('.');
        if (dot > 0) {
            stem = baseName.substring(0, dot);
            ext = baseName.substring(dot);
        }

        int counter = 1;
        while (Files.exists(directory.resolve(name))) {
            name = stem + "_" + counter + ext;
            counter++;
        }
        return name;
    }
}
