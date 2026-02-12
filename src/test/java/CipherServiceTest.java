import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

class CipherServiceTest {

    @Test
    void decryptReturnsEmptyForNullOrEmptyInput() {
        CipherService service = new CipherService();
        String key = "ab\nbc";

        assertEquals("", service.decrypt(null, key));
        assertEquals("", service.decrypt("", key));
    }

    @Test
    void decryptReturnsInvalidKeyWhenKeyIsBad() {
        CipherService service = new CipherService();

        assertEquals("Invalid key", service.decrypt("abc", null));
        assertEquals("Invalid key", service.decrypt("abc", ""));
        assertEquals("Invalid key", service.decrypt("abc", "a"));
        assertEquals("Invalid key", service.decrypt("abc", "ab\nb"));
        assertEquals("Invalid key", service.decrypt("abc", "aa\nbc"));
        assertEquals("Invalid key", service.decrypt("abc", "ab\ncc"));
    }

    @Test
    void decryptAppliesMappingAndPreservesOtherCharacters() {
        CipherService service = new CipherService();
        String key = "abc\nbcd"; // plain->cipher, so decipher maps b->a, c->b, d->c

        String input = "bcd-xy\n";
        String output = service.decrypt(input, key);

        assertEquals("abc-xy\n", output);
    }

    @Test
    void loadKey_defaultPathReturnsValidatedContent() {
        FileReaderService reader = mock(FileReaderService.class);
        CipherService service = new CipherService(reader);
        String keyContent = "abc\nbcd";

        try {
            when(reader.readFile("./ciphers/key.txt")).thenReturn(keyContent);
        } catch (IOException e) {
            fail(e);
        }

        assertEquals(keyContent, service.loadKey());
    }

    @Test
    void loadKey_customPathReturnsValidatedContent() {
        FileReaderService reader = mock(FileReaderService.class);
        CipherService service = new CipherService(reader);
        String keyContent = "abc\nbcd";

        try {
            when(reader.readFile("./ciphers/alternate.txt")).thenReturn(keyContent);
        } catch (IOException e) {
            fail(e);
        }

        assertEquals(keyContent, service.loadKey("alternate.txt"));
    }

    @Test
    void loadKey_customPathReturnsNullForNullBlankOrUnreadable() {
        FileReaderService reader = mock(FileReaderService.class);
        CipherService service = new CipherService(reader);

        assertNull(service.loadKey(null));
        assertNull(service.loadKey("   "));
        assertNull(service.loadKey("ciphers/key.txt"));

        try {
            when(reader.readFile("./ciphers/missing.txt")).thenThrow(new IOException("missing"));
        } catch (IOException e) {
            fail(e);
        }
        assertNull(service.loadKey("missing.txt"));
    }

    @Test
    void loadKey_returnsNullWhenKeyFormatIsInvalid() {
        FileReaderService reader = mock(FileReaderService.class);
        CipherService service = new CipherService(reader);

        try {
            when(reader.readFile("./ciphers/bad.txt")).thenReturn("abc\ncc");
        } catch (IOException e) {
            fail(e);
        }
        assertNull(service.loadKey("bad.txt"));
    }
}
