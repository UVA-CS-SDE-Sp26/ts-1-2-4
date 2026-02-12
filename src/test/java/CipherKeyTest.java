import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CipherKeyTest {

    @Test
    void validateSucceedsForTwoLineKey() {
        String content = "abc\nbcd";
        CipherKey key = new CipherKey(content);

        assertTrue(key.validate());
        assertEquals(Map.of('b', 'a', 'c', 'b', 'd', 'c'), key.getMapping());
    }

    @Test
    void validateFailsForNullOrEmptyContent() {
        CipherKey nullKey = new CipherKey(null);
        assertFalse(nullKey.validate());

        CipherKey emptyKey = new CipherKey("");
        assertFalse(emptyKey.validate());
    }

    @Test
    void validateFailsWhenLineCountIsNotTwo() {
        CipherKey oneLine = new CipherKey("abc");
        assertFalse(oneLine.validate());

        CipherKey threeLines = new CipherKey("abc\ndef\nghi");
        assertFalse(threeLines.validate());
    }

    @Test
    void validateFailsWhenLineLengthsDiffer() {
        CipherKey mismatch = new CipherKey("abc\nbc");
        assertFalse(mismatch.validate());
    }

    @Test
    void validateFailsWhenCipherCharactersDuplicate() {
        CipherKey duplicateCipher = new CipherKey("ab\ncc");
        assertFalse(duplicateCipher.validate());
    }

    @Test
    void validateFailsWhenPlainCharactersDuplicate() {
        CipherKey duplicatePlain = new CipherKey("aa\nbc");
        assertFalse(duplicatePlain.validate());
    }

    @Test
    void validateIgnoresEmptyLinesAroundKey() {
        CipherKey key = new CipherKey("\nabc\nbcd\n");
        assertTrue(key.validate());
    }

    @Test
    void getMapping_isUnmodifiableView() {
        CipherKey key = new CipherKey("abc\nbcd");
        Map<Character, Character> mapping = key.getMapping();

        assertThrows(UnsupportedOperationException.class, () -> mapping.put('x', 'y'));
    }
}
