import java.util.Optional;
import interfaces.TopSecretInterface;

public class TopSecret implements TopSecretInterface {

    private static ProgramController controller = new ProgramController(
            new FileCatalog(),
            new FileReaderService(),
            new CipherService()
    );

    // test hook (optional)
    static void setControllerForTests(ProgramController c) {
        controller = c;
    }

    @Override
    public void run(String[] args) {

        // 0 args: list files
        if (args.length == 0) {
            System.out.println(controller.listFiles());
            return;
        }

        // >2 args: error
        if (args.length > 2) {
            System.out.println("Too many arguments");
            return;
        }

        // parse file number (expects something like "01")
        String fileNumStr = args[0];
        int index;
        try {
            index = Integer.parseInt(fileNumStr);
        } catch (NumberFormatException e) {
            System.out.println("Invalid file number");
            return;
        }

        Optional<String> keyPath = Optional.empty();
        if (args.length == 2) {
            keyPath = Optional.of(args[1]);
        }

        System.out.println(controller.showFile(index, keyPath));
    }

    public static void main(String[] args) {
        new TopSecret().run(args);
    }
}
