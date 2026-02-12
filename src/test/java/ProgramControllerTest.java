import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProgramControllerTest {

    @Test
    void listFiles_formatsNumberedOutput() {
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
    void listFiles_returnsNoFilesMessageWhenCatalogEmpty() {
        FileCatalog catalog = mock(FileCatalog.class);
        FileReaderService reader = mock(FileReaderService.class);
        CipherService cipher = mock(CipherService.class);
        when(catalog.listFiles()).thenReturn(List.of());

        ProgramController controller = new ProgramController(catalog, reader, cipher);
        assertEquals("No files found.", controller.listFiles());
    }

    @Test
    void showFile_returnsFileNotFoundWhenIndexMissing() {
        FileCatalog catalog = mock(FileCatalog.class);
        FileReaderService reader = mock(FileReaderService.class);
        CipherService cipher = mock(CipherService.class);
        when(catalog.getByIndex(9)).thenReturn(Optional.empty());

        ProgramController controller = new ProgramController(catalog, reader, cipher);
        assertEquals("File not found", controller.showFile(9, Optional.empty()));
        verify(cipher, never()).decrypt(anyString(), anyString());
    }

    @Test
    void showFile_usesDefaultKeyWhenNoKeyPathProvided() {
        int index = 1;
        String encryptedContent = "ENCRYPTED";
        String expectedOutput = "DECRYPTED";
        FileCatalog catalog = mock(FileCatalog.class);
        FileReaderService reader = mock(FileReaderService.class);
        CipherService cipher = mock(CipherService.class);

        when(catalog.getByIndex(index)).thenReturn(Optional.of(encryptedContent));
        when(cipher.loadKey()).thenReturn("DEFAULT_KEY");
        when(cipher.decrypt(encryptedContent, "DEFAULT_KEY")).thenReturn(expectedOutput);

        ProgramController controller = new ProgramController(catalog, reader, cipher);
        assertEquals(expectedOutput, controller.showFile(index, Optional.empty()));
        verify(cipher).loadKey();
        verify(cipher, never()).loadKey(anyString());
    }

    @Test
    void showFile_usesProvidedKeyPathWhenPresent() {
        int index = 1;
        String keyPath = "ciphers/alternate.txt";
        String encryptedContent = "ENCRYPTED";
        String expectedOutput = "DECRYPTED";
        FileCatalog catalog = mock(FileCatalog.class);
        FileReaderService reader = mock(FileReaderService.class);
        CipherService cipher = mock(CipherService.class);

        when(catalog.getByIndex(index)).thenReturn(Optional.of(encryptedContent));
        when(cipher.loadKey(keyPath)).thenReturn("KEY");
        when(cipher.decrypt(encryptedContent, "KEY")).thenReturn(expectedOutput);

        ProgramController controller = new ProgramController(catalog, reader, cipher);
        assertEquals(expectedOutput, controller.showFile(index, Optional.of(keyPath)));
        verify(cipher).loadKey(keyPath);
        verify(cipher, never()).loadKey();
    }

    @Test
    void showFile_returnsInvalidKeyWhenLoadedKeyIsNull() {
        FileCatalog catalog = mock(FileCatalog.class);
        FileReaderService reader = mock(FileReaderService.class);
        CipherService cipher = mock(CipherService.class);

        when(catalog.getByIndex(1)).thenReturn(Optional.of("abc"));
        when(cipher.loadKey("bad-key-path")).thenReturn(null);

        ProgramController controller = new ProgramController(catalog, reader, cipher);
        assertEquals("Invalid key", controller.showFile(1, Optional.of("bad-key-path")));
        verify(cipher, never()).decrypt(anyString(), anyString());
    }

    @Test
    void showFile_returnsDecryptionFailedWhenCipherThrows() {
        FileCatalog catalog = mock(FileCatalog.class);
        FileReaderService reader = mock(FileReaderService.class);
        CipherService cipher = mock(CipherService.class);

        when(catalog.getByIndex(1)).thenReturn(Optional.of("abc"));
        when(cipher.loadKey()).thenReturn("KEY");
        when(cipher.decrypt("abc", "KEY")).thenThrow(new RuntimeException("boom"));

        ProgramController controller = new ProgramController(catalog, reader, cipher);
        assertEquals("Decryption failed", controller.showFile(1, Optional.empty()));
    }
}
