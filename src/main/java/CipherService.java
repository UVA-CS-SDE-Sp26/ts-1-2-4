import java.io.File;
import java.io.IOException;
import java.util.Map;
import interfaces.CipherServiceInterface;

public class CipherService implements CipherServiceInterface {
    private static final String DEFAULT_KEY_PATH = "ciphers/key.txt";
    private final FileReaderService fileReader;

    public CipherService() {
        this.fileReader = new FileReaderService();
    }

    public CipherService(FileReaderService fileReader) {
        this.fileReader = fileReader;
    }

    public String loadKey() {
        return readAndValidate(resolveDefaultKeyPath());
    }

    public String loadKey(String keyPath) {
        if (keyPath == null || keyPath.isBlank()) {
            return null;
        }
        return readAndValidate(keyPath);
    }

    public String decrypt(String input, String keyContent) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        CipherKey key = new CipherKey(keyContent);
        if (!key.validate()) {
            return "Invalid key";
        }

        return applyMapping(input, key.getMapping());
    }

    private String applyMapping(String input, Map<Character, Character> mapping) {
        StringBuilder output = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            Character mapped = mapping.get(ch);
            output.append(mapped != null ? mapped : ch);
        }
        return output.toString();
    }

    private String resolveDefaultKeyPath() {
        String[] candidates = new String[] {
            "./ciphers/key.txt",
            "./build/../ciphers/key.txt",
            "ciphers/key.txt"
        };

        for (String candidate : candidates) {
            File file = new File(candidate);
            if (file.exists() && file.isFile()) {
                return candidate;
            }
        }

        return DEFAULT_KEY_PATH;
    }

    private String readAndValidate(String path) {
        if (path == null || path.isBlank()) {
            return null;
        }
        String content;
        try {
            content = fileReader.readFile(path);
        } catch (IOException e) {
            return null;
        }
        CipherKey key = new CipherKey(content);
        return key.validate() ? content : null;
    }
}
