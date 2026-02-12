import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class FileCatalogTest {

    private FileCatalog fileCatalog;
    private static final String TEST_DATA_DIR = "./data";
    private static final String TEST_PREFIX = "hw2_fc_";
    private final List<Path> createdPaths = new ArrayList<>();

    @BeforeEach
    void setUp() throws IOException {
        //  Setup a clean "./data" directory for testing
        Path dataPath = Paths.get(TEST_DATA_DIR);
        if (!Files.exists(dataPath)) {
            Files.createDirectories(dataPath);
        }

        // Create dummy files
        writeTestFile(dataPath.resolve(TEST_PREFIX + "gamma.txt"), "Content of Gamma");
        writeTestFile(dataPath.resolve(TEST_PREFIX + "alpha.txt"), "Content of Alpha");
        writeTestFile(dataPath.resolve(TEST_PREFIX + "Beta.txt"), "Content of Beta");

        fileCatalog = new FileCatalog();
    }

    @AfterEach
    void tearDown() throws IOException {
        for (Path path : createdPaths) {
            Files.deleteIfExists(path);
        }
    }

    @Test
    void listFiles() {
        List<String> files = fileCatalog.listFiles();
        assertTrue(files.contains(TEST_PREFIX + "alpha.txt"));
        assertTrue(files.contains(TEST_PREFIX + "Beta.txt"));
        assertTrue(files.contains(TEST_PREFIX + "gamma.txt"));

        int alphaIndex = files.indexOf(TEST_PREFIX + "alpha.txt");
        int betaIndex = files.indexOf(TEST_PREFIX + "Beta.txt");
        int gammaIndex = files.indexOf(TEST_PREFIX + "gamma.txt");
        assertTrue(alphaIndex < betaIndex);
        assertTrue(betaIndex < gammaIndex);
    }

    @Test
    void listFiles_refreshesWhenNewFileAppears() throws IOException {
        List<String> initial = fileCatalog.listFiles();
        int initialSize = initial.size();

        Path dataPath = Paths.get(TEST_DATA_DIR);
        writeTestFile(dataPath.resolve(TEST_PREFIX + "delta.txt"), "Content of Delta");

        List<String> updated = fileCatalog.listFiles();
        assertEquals(initialSize + 1, updated.size());
        assertTrue(updated.contains(TEST_PREFIX + "delta.txt"));
    }

    @Test
    void getByIndex() {
        List<String> files = fileCatalog.listFiles();
        int alphaIndex = files.indexOf(TEST_PREFIX + "alpha.txt") + 1;
        int betaIndex = files.indexOf(TEST_PREFIX + "Beta.txt") + 1;

        Optional<String> result1 = fileCatalog.getByIndex(alphaIndex);
        assertTrue(result1.isPresent());
        assertEquals("Content of Alpha", result1.get());

        Optional<String> result2 = fileCatalog.getByIndex(betaIndex);
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
        Optional<String> result = fileCatalog.getByName(TEST_PREFIX + "gamma.txt");
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
        Path nested = dataPath.resolve(TEST_PREFIX + "nested");
        Files.createDirectories(nested);
        createdPaths.add(nested);
        writeTestFile(dataPath.resolve("." + TEST_PREFIX + "hidden.txt"), "secret");

        List<String> files = fileCatalog.listFiles();

        assertFalse(files.contains(TEST_PREFIX + "nested"));
        assertFalse(files.contains("." + TEST_PREFIX + "hidden.txt"));
    }

    @Test
    void missingDataDirectory_printsWarningAndReturnsEmptyList() throws IOException {
        Path dataPath = Paths.get(TEST_DATA_DIR);
        assumeTrue(Files.notExists(dataPath), "Skipping: data directory exists in this environment.");

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

    private void writeTestFile(Path path, String content) throws IOException {
        Files.writeString(path, content);
        createdPaths.add(path);
    }
}
