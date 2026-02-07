package interfaces;
import java.util.Map;

public interface CipherKeyInterface {
    boolean validate();
    Map<Character, Character> getMapping();
}
