package interfaces;

public interface CipherServiceInterface {
    String loadKey();
    String loadKey(String keyFilename);
    String decrypt(String input, String keyContent);
}
