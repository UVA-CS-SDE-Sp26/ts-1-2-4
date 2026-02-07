package interfaces;
import java.util.List;
import java.util.Optional;

public interface FileCatalogInterface {
    /**
     * Lists the names of available files in the data directory.
     * @return A sorted list of filenames.
     */
    List<String> listFiles();

    /**
     * Retrieves the content of a file based on its 1-based index from listFiles().
     * @param index The 1-based index (e.g., 1 for the first file).
     * @return An Optional containing the file content, or empty if index is invalid.
     */
    Optional<String> getByIndex(int index);

    /**
     * Retrieves the content of a file based on its specific filename.
     * @param filename The exact name of the file.
     * @return An Optional containing the file content, or empty if file not found.
     */
    Optional<String> getByName(String filename);
}