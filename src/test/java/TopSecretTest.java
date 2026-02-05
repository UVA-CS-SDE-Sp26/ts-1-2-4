import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;

import static org.junit.jupiter.api.Assertions.*;

class TopSecretTest {

    @Test
    void main() {


        // Test that object is instance of ProgramController
        // Test args types: 1, 2, 3
        // Errors:
        String exampleFile = "Example file output. ";

        FileCatalog catalog = null;
        FileReaderService reader = null;
        CipherService cipher = null;


        ProgramController programController = new ProgramController(catalog, reader, cipher);
        String[] args = {};
        // Test fails if number of arguments is greater than 2
        assertFalse(args.length < 2);


//        No args prints a numbered list.
//        - One arg prints selected file content.
//        - Two args uses alternate key path.
//        - Invalid number format prints error.
//        - Too many args prints error.

        }

    @Test
    void testNoArgs() {
        String[] args = {};
        TopSecret.main(args);

    }

    @Test
    void testOneArgs() {
        String[] args = {"File Number"};
        TopSecret.main(args);
    }

    @Test
    void testTwoArgs() {
        String[] args = {"File.txt", "key.txt"};
        TopSecret.main(args);

    }
}