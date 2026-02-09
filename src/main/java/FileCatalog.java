import interfaces.FileCatalogInterface;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FileCatalog implements FileCatalogInterface {

    private final FileReaderService fileReader;
    private final String dataDirectoryPath;
    private List<String> cachedFileList;

    public FileCatalog() {
        this.fileReader = new FileReaderService();
        this.dataDirectoryPath = resolveDataDirectory();
        this.cachedFileList = new ArrayList<>();
        refreshFileList();
    }

    // Tries to locate the 'data' folder based on the priority list in the plan.
    private String resolveDataDirectory() {
        // ./data (Project root)
        File rootData = new File("./data");
        if (rootData.exists() && rootData.isDirectory()) {
            return "./data";
        }

        // ./build/../data (Fallback for Gradle build/run contexts)
        File buildData = new File("./build/../data");
        if (buildData.exists() && buildData.isDirectory()) {
            return "./build/../data";
        }

        // Working-directory/data (Generic fallback)
        File currentDirData = new File("data");
        if (currentDirData.exists() && currentDirData.isDirectory()) {
            return "data";
        }

        System.err.println("Warning: Could not locate 'data' directory.");
        return "./data";
    }

    // Scans the data directory and updates the internal list of files.
    private void refreshFileList() {
        File folder = new File(this.dataDirectoryPath);
        File[] listOfFiles = folder.listFiles();
        cachedFileList.clear();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && !file.isHidden()) {
                    cachedFileList.add(file.getName());
                }
            }
            Collections.sort(cachedFileList, String::compareToIgnoreCase);
        }
    }

    @Override
    public List<String> listFiles() {
        // We refresh every time list is called to ensure we see new files,
        // or we could just return the cached list if performance was a concern.
        refreshFileList();
        return new ArrayList<>(cachedFileList);
    }

    @Override
    public Optional<String> getByIndex(int index) {
        int listIndex = index - 1;

        if (listIndex >= 0 && listIndex < cachedFileList.size()) {
            String filename = cachedFileList.get(listIndex);
            return getByName(filename);
        }

        return Optional.empty();
    }

    @Override
    public Optional<String> getByName(String filename) {
        String fullPath = this.dataDirectoryPath + File.separator + filename;

        try {
            String content = fileReader.readFile(fullPath);
            return Optional.of(content);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return Optional.empty();
        }
    }
}