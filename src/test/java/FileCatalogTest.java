import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FileCatalogTest {

    private FileCatalog fileCatalog;
    private static final String TEST_DATA_DIR = "./data";

    @BeforeEach
    void setUp() throws IOException {
        //  Setup a clean "./data" directory for testing
        Path dataPath = Paths.get(TEST_DATA_DIR);
        if (!Files.exists(dataPath)) {
            Files.createDirectories(dataPath);
        }

        // Create dummy files
        Files.writeString(dataPath.resolve("gamma.txt"), "Content of Gamma");
        Files.writeString(dataPath.resolve("alpha.txt"), "Content of Alpha");
        Files.writeString(dataPath.resolve("Beta.txt"), "Content of Beta");

        fileCatalog = new FileCatalog();
    }

    @AfterEach
    void tearDown() throws IOException {
        Path dataPath = Paths.get(TEST_DATA_DIR);
        if (Files.exists(dataPath)) {
            try (Stream<Path> walk = Files.walk(dataPath)) {
                walk.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        }
    }

    @Test
    void listFiles() {
        List<String> files = fileCatalog.listFiles();

        assertEquals(3, files.size(), "Should find exactly 3 files in the data directory");

        assertEquals("alpha.txt", files.get(0));
        assertEquals("Beta.txt", files.get(1));
        assertEquals("gamma.txt", files.get(2));
    }

    @Test
    void listFiles_refreshesWhenNewFileAppears() throws IOException {
        List<String> initial = fileCatalog.listFiles();
        assertEquals(3, initial.size());

        Path dataPath = Paths.get(TEST_DATA_DIR);
        Files.writeString(dataPath.resolve("delta.txt"), "Content of Delta");

        List<String> updated = fileCatalog.listFiles();
        assertEquals(4, updated.size());
        assertTrue(updated.contains("delta.txt"));
    }

    @Test
    void getByIndex() {
        Optional<String> result1 = fileCatalog.getByIndex(1);
        assertTrue(result1.isPresent());
        assertEquals("Content of Alpha", result1.get());

        Optional<String> result2 = fileCatalog.getByIndex(2);
        assertTrue(result2.isPresent());
        assertEquals("Content of Beta", result2.get());

        Optional<String> resultZero = fileCatalog.getByIndex(0);
        assertFalse(resultZero.isPresent(), "Index 0 should return empty Optional");

        Optional<String> resultOut = fileCatalog.getByIndex(99);
        assertFalse(resultOut.isPresent(), "Out of bounds index should return empty Optional");
    }

    @Test
    void getByIndex_returnsEmptyWhenCalledWithNegativeIndex() {
        Optional<String> result = fileCatalog.getByIndex(-1);
        assertTrue(result.isEmpty());
    }

    @Test
    void getByName() {
        Optional<String> result = fileCatalog.getByName("gamma.txt");
        assertTrue(result.isPresent());
        assertEquals("Content of Gamma", result.get());

        Optional<String> resultMissing = fileCatalog.getByName("ghost.txt");
        assertFalse(resultMissing.isPresent(), "Non-existent file should return empty Optional");

        Optional<String> resultDir = fileCatalog.getByName(".");
        assertFalse(resultDir.isPresent());
    }

    @Test
    void listFiles_ignoresDirectoriesAndHiddenFiles() throws IOException {
        Path dataPath = Paths.get(TEST_DATA_DIR);
        Files.createDirectories(dataPath.resolve("nested"));
        Files.writeString(dataPath.resolve(".hidden.txt"), "secret");

        List<String> files = fileCatalog.listFiles();

        assertFalse(files.contains("nested"));
        assertFalse(files.contains(".hidden.txt"));
    }

    @Test
    void missingDataDirectory_printsWarningAndReturnsEmptyList() throws IOException {
        tearDown();

        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(errContent));
        try {
            FileCatalog missingCatalog = new FileCatalog();
            assertTrue(missingCatalog.listFiles().isEmpty());
            assertTrue(errContent.toString().contains("Could not locate 'data' directory."));
        } finally {
            System.setErr(originalErr);
        }
    }
}
