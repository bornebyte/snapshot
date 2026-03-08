package snapshot;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Log {

    public static void show() throws Exception {

        Utils utils = new Utils();
        List<String> filePaths = utils.filePathsInDirectory(".snapshot/objects/refs/");
        for (String file : filePaths) {
            String content = Files.readString(Path.of(file));
            String correspondFile = utils.hashCorrespondingFile(content);
            System.out.println(correspondFile);
        }
    }
}