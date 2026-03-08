package snapshot;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Index {
    public static void add(String filename) throws Exception {
        Utils utils = new Utils();
        if (filename.equals(".")) {
            List<String> filePaths = utils
                    .filePathsInDirectory(Paths.get(".").toAbsolutePath().normalize().toString());

            FileWriter fw = new FileWriter(".snapshot/index", true);

            for (String filePath : filePaths) {
                String content = Files.readString(Path.of(filePath));

                String hash = Utils.sha1(content);

                fw.write(hash + " " + filePath + "\n");

                FileWriter addfw = new FileWriter(".snapshot/objects/staging/" + hash);
                addfw.write(content);
                addfw.close();

                System.out.println("Added " + filePath);
            }
            fw.close();
            return;
        }

        String content = Files.readString(Path.of(filename));

        String hash = Utils.sha1(content);

        FileWriter fw = new FileWriter(".snapshot/index", true);
        fw.write(hash + " " + filename + "\n");
        fw.close();

        FileWriter addfw = new FileWriter(".snapshot/objects/staging/" + hash);
        addfw.write(content);
        addfw.close();

        System.out.println("Added " + filename);
    }
}
