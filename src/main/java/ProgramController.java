import java.util.Optional;

public class ProgramController {
    private FileCatalog catalog;
    private FileReaderService reader;
    private CipherService cipher;

   public ProgramController(FileCatalog catalog, FileReaderService reader, CipherService cipher) {
        this.catalog = catalog;
        this.reader = reader;
        this.cipher = cipher;
    }

    public String listFiles() {
        return "Listing files...";
    }
    public String showFile(int index, Optional<String> keyPath) {
        return "Uploading file...";
    }

    // Getters and Setters
    public FileCatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(FileCatalog catalog) {
        this.catalog = catalog;
    }

    public FileReaderService getReader() {
        return reader;
    }

    public void setReader(FileReaderService reader) {
        this.reader = reader;
    }

    public CipherService getCipher() {
        return cipher;
    }

    public void setCipher(CipherService cipher) {
        this.cipher = cipher;
    }
}
