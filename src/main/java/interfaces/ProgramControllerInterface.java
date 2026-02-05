package interfaces;

import java.util.Optional;

public interface ProgramControllerInterface {
    String listFiles();

    String showFile(int index, Optional<String> keyPath);

}
