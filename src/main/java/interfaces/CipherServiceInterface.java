package interfaces;

public interface CipherServiceInterface {
    CipherKeyInterface loadKey(String keyPath);
    String decipher(String input, CipherKeyInterface key);
}