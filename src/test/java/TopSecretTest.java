import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TopSecretTest {

    private PrintStream originalOut;
    private ByteArrayOutputStream outContent;

    /**
     * Fake controller used for testing. This allows us to isolate testing without depending on real
     * file tests from depending on real life reading or cipher logic.
     *
     * Allows us to:
     * - Control return values
     * - Track method calls
     **/
    static class FakeProgramController extends ProgramController {
        String lastShowFileIndex = null;
        Optional<String> lastKeyPath = null; // Value might not exist


        /**
         * Creates a fake controller.
         * Passes null dependencies since overridden methods do not use them.
         */
        FakeProgramController() {
            super(null, null, null); // OK for tests since we override methods we use
        }


        /**
         * Simulates listing available files.
         *
         * @return mock file list string
         */
        @Override
        public String listFiles() {
            return "01 filea.txt\n02 fileb.txt\n";
        }


        /**
         * Simulates displaying a file.
         * Records inputs so tests can verify correct argument parsing.
         *
         * @param index   file number
         * @param keyPath optional key path
         * @return mock file content string
         */
        @Override
        public String showFile(int index, Optional<String> keyPath) {
            lastShowFileIndex = String.valueOf(index);
            lastKeyPath = keyPath;
            return "FILE_CONTENT_" + index;
        }
    }

    // Run this method before every test
    @BeforeEach
    void setUp() {
        originalOut = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }


    // Run this method after every test
    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void noArgs_printsFileList() {
        FakeProgramController fake = new FakeProgramController();
        TopSecret.setControllerForTests(fake); // uses your test hook :contentReference[oaicite:4]{index=4}

        TopSecret.main(new String[]{});

        assertEquals("01 filea.txt\n02 fileb.txt", outContent.toString().trim());
    }

    @Test
    void oneArg_parsesNumber_callsShowFile_withDefaultKey() {
        FakeProgramController fake = new FakeProgramController();
        TopSecret.setControllerForTests(fake);

        TopSecret.main(new String[]{"1"}); // using "1" as numeric index per your controller signature :contentReference[oaicite:5]{index=5}

        assertEquals("FILE_CONTENT_1", outContent.toString().trim());
        assertEquals("1", fake.lastShowFileIndex);
        assertTrue(fake.lastKeyPath.isEmpty(), "Expected default keyPath (empty Optional)");
    }

    @Test
    void twoArgs_parsesNumber_callsShowFile_withProvidedKeyPath() {
        FakeProgramController fake = new FakeProgramController();
        TopSecret.setControllerForTests(fake);

        TopSecret.main(new String[]{"1", "key.txt"});

        assertEquals("FILE_CONTENT_1", outContent.toString().trim());
        assertEquals("1", fake.lastShowFileIndex);
        assertTrue(fake.lastKeyPath.isPresent());
        assertEquals("key.txt", fake.lastKeyPath.get());
    }

    @Test
    void tooManyArgs_printsTooManyArgumentsError() {
        FakeProgramController fake = new FakeProgramController();
        TopSecret.setControllerForTests(fake);

        TopSecret.main(new String[]{"1", "key.txt", "extra"});

        assertEquals("Too many arguments", outContent.toString().trim());
    }

    @Test
    void invalidFileNumberFormat_printsInvalidFileNumberError() {
        FakeProgramController fake = new FakeProgramController();
        TopSecret.setControllerForTests(fake);

        TopSecret.main(new String[]{"abc"});

        assertEquals("Invalid file number", outContent.toString().trim());
    }

    @Test
    void run_acceptsNegativeNumberAndDelegatesToController() {
        FakeProgramController fake = new FakeProgramController();
        TopSecret.setControllerForTests(fake);

        TopSecret app = new TopSecret();
        app.run(new String[]{"-1"});

        assertEquals("FILE_CONTENT_-1", outContent.toString().trim());
        assertEquals("-1", fake.lastShowFileIndex);
    }
}
