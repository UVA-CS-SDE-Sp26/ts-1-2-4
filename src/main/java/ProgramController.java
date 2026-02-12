import java.util.Optional;
import java.util.List;
import interfaces.ProgramControllerInterface;

public class ProgramController implements ProgramControllerInterface {
    private FileCatalog catalog;
    private FileReaderService reader;
    private CipherService cipher;

   public ProgramController(FileCatalog catalog, FileReaderService reader, CipherService cipher) {
        this.catalog = catalog;
        this.reader = reader;
        this.cipher = cipher;
    }

    public String listFiles() {
        List<String> files = catalog.listFiles();
        int counter = 1;
        String outputString;
        if  (files.isEmpty()) {
            outputString = "No files found.";
        } else {
            outputString = "";
            for (String file : files) {
                String twoDigitString = String.format("%02d", counter);
                String newLine = twoDigitString + " " + file+ "\n";
                outputString += newLine;
                counter++;
            }
        }
        return outputString;
    }
    public String showFile(int index, Optional<String> keyFilename) {
        Optional<String> fileContent = catalog.getByIndex(index);
        if (fileContent.isEmpty()) {
            return "File not found";
        }
        String content = fileContent.get();
        String key = keyFilename.isPresent() ? cipher.loadKey(keyFilename.get()) : cipher.loadKey();
        if (key == null) {
            return "Invalid key";
        }
        try {
            return cipher.decrypt(content, key);
        } catch (Exception e) {
            return "Decryption failed";
        }
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
