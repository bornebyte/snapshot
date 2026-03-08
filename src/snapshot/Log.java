package snapshot;

import java.nio.file.Files;
import java.nio.file.Path;

public class Log {

    public static void show() throws Exception {

        String head = Files.readString(Path.of(".snapshot/HEAD"));

        String commit = Files.readString(Path.of(".snapshot/objects/" + head));

        System.out.println(commit);
    }
}