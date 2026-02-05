import java.io.*;

public class FileReaderService {
    public String readFile(String path) throws IOException {
        File f = new File(path);
        if (f.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(f));
            StringBuilder content = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                content.append(line);
            }

            return content.toString();
        } else {
            return "File does not exist";
        }
    }
}