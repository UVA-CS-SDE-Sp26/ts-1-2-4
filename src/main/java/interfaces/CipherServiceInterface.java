package interfaces;

public interface CipherServiceInterface {
    String loadKey();
    String loadKey(String keyPath);
    String decrypt(String input, String keyContent);
}
