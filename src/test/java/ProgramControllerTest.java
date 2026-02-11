import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProgramControllerTest {

    @Test
    void listFiles() {
        List<String> files = List.of("file1.txt", "file2.txt", "file3.txt");
        FileCatalog catalog = mock(FileCatalog.class);
        FileReaderService reader = mock(FileReaderService.class);
        CipherService cipher = mock(CipherService.class);
        when(catalog.listFiles()).thenReturn(files);
        ProgramController controller = new ProgramController(catalog, reader, cipher);
        String expectedOutput = "01 file1.txt\n02 file2.txt\n03 file3.txt\n";
        String actualOutput = controller.listFiles();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void showFile() {
       int index = 1;
       String keyPath = "src/test/resources/testdata/key.txt";
       String encryptedContent = "ENCRYPTED";
       String expectedOutput = "Decrypted content of file2.txt";
       FileCatalog catalog = mock(FileCatalog.class);
       FileReaderService reader = mock(FileReaderService.class);
       CipherService cipher = mock(CipherService.class);
       when(catalog.getByIndex(index)).thenReturn(Optional.of(encryptedContent));
       when(cipher.loadKey(keyPath)).thenReturn("KEY");
       when(cipher.decrypt(encryptedContent, "KEY")).thenReturn(expectedOutput);
       ProgramController controller = new ProgramController(catalog, reader, cipher);
       String actualOutput = controller.showFile(index, Optional.of(keyPath));
       assertEquals(expectedOutput,actualOutput,"showFile should return decrypted content for encrypted files");
    }
}
