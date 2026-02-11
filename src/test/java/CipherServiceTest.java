import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
}
