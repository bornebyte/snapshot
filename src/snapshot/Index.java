package snapshot;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class Index {
    public static void add(String filename) throws Exception {
        String content = Files.readString(Path.of(filename));

        String hash = Utils.sha1(content);

        FileWriter fw = new FileWriter(".snapshot/index", true);
        fw.write(hash + " " + filename + "\n");
        fw.close();

        FileWriter addfw = new FileWriter(".snapshot/objects/staging/" + hash);
        addfw.write(content);
        addfw.close();

        System.out.println("Stored object " + hash);

        System.out.println("Added " + filename);
    }
}
