package interfaces;

import java.util.List;
import java.util.Optional;

public interface FileCatalogInterface {
    List<String> listFiles();
    Optional<String> getByIndex(int index);
    Optional<String> getByName(String filename);
}