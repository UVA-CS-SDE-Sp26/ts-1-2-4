package interfaces;
import java.util.Map;

public interface CipherKeyInterface {
    /**
     * Validates that the key is properly formatted and usable.
     * @return true if the key is valid, false otherwise.
     */
    boolean validate();

    /**
     * Retrieves the mapping used for substitution.
     * @return A map of characters to their replacement characters.
     */
    Map<Character, Character> getMapping();
}
