import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class ProgramControllerTest {

    @Test
    void listFiles() {
        List<String> files = List.of("file1.txt", "file2.txt", "file3.txt");
        String dataPath = "src/test/resources/testdata/";
        /* Replace this with mockito later
        FileCatalog catalog = new FileCatalog(dataPath);
        FileReaderService reader = new FileReaderService();
        CipherService cipher = new CipherService(); */
        ProgramController controller = new ProgramController(catalog, reader, cipher);
        String expectedOutput = "01 file1.txt\n02 file2.txt\n03 file3.txt\n";
        String actualOutput = controller.listFiles();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void showFile() {
       int index = 1;
       String keyPath = "src/test/resources/testdata/key.txt";
       String expectedOutput = "Decrypted content of file2.txt";
       /* Replace this with mockito later
        FileCatalog catalog = new FileCatalog(dataPath);
        FileReaderService reader = new FileReaderService();
        CipherService cipher = new CipherService(); */
        ProgramController controller = new ProgramController(catalog, reader, cipher);
        String actualOutput = controller.showFile(index, Optional.of(keyPath));
        assertEquals(expectedOutput,actualOutput,"showFile should return decrypted content for encrypted files");
    }
}