import interfaces.CipherKeyInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CipherKey implements CipherKeyInterface {
    private final Map<Character, Character> mapping = new LinkedHashMap<>();
    private boolean parseError;

    public CipherKey(String content) {
        parse(content);
    }

    @Override
    public boolean validate() {
        if (parseError || mapping.isEmpty()) {
            return false;
        }

        Set<Character> cipherSeen = new HashSet<>();
        Set<Character> plainSeen = new HashSet<>();
        for (Map.Entry<Character, Character> entry : mapping.entrySet()) {
            Character cipher = entry.getKey();
            Character plain = entry.getValue();
            if (cipher == null || plain == null) {
                return false;
            }
            if (!cipherSeen.add(cipher)) {
                return false;
            }
            if (!plainSeen.add(plain)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Map<Character, Character> getMapping() {
        return Collections.unmodifiableMap(mapping);
    }

    private void parse(String content) {
        if (content == null) {
            parseError = true;
            return;
        }

        String[] rawLines = content.split("\\R", -1);
        List<String> lines = new ArrayList<>();
        for (String line : rawLines) {
            if (!line.isEmpty()) {
                lines.add(line);
            }
        }

        if (lines.size() != 2) {
            parseError = true;
            return;
        }

        String plainLine = lines.get(0);
        String cipherLine = lines.get(1);
        if (plainLine.isEmpty() || cipherLine.isEmpty() || plainLine.length() != cipherLine.length()) {
            parseError = true;
            return;
        }

        Set<Character> cipherSeen = new HashSet<>();
        Set<Character> plainSeen = new HashSet<>();
        for (int i = 0; i < plainLine.length(); i++) {
            char plain = plainLine.charAt(i);
            char cipher = cipherLine.charAt(i);

            if (!cipherSeen.add(cipher) || !plainSeen.add(plain)) {
                parseError = true;
                mapping.clear();
                return;
            }

            mapping.put(cipher, plain);
        }
    }
}
