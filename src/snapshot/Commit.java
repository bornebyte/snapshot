package snapshot;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Commit {
    public static void commit(String message) throws Exception {

        Path stagingPath = Paths.get(".snapshot/objects/staging/");
        List<Path> files;
        List<String> hashes;

        try (Stream<Path> pathStream = Files.walk(stagingPath)) {
            files = pathStream
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());

            hashes = files.stream()
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());
        }

        String commitContent = "Time : " + System.currentTimeMillis() + "\n" +
                "Message : " + message + "\n" +
                hashes + "\n";

        String commitHash = Utils.sha1(commitContent);

        FileWriter fw = new FileWriter(".snapshot/objects/refs/" + commitHash);
        fw.write(commitContent);
        fw.close();

        Files.writeString(Path.of(".snapshot/HEAD"), commitHash);

        Path commitDir = Paths.get(".snapshot/objects/commits/" + commitHash);
        Files.createDirectories(commitDir);

        try (Stream<Path> paths = Files.walk(stagingPath)) {
            paths.filter(Files::isRegularFile)
                    .forEach(source -> {
                        try {
                            Path target = commitDir.resolve(source.getFileName());
                            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }

        System.out.println("Commit created: " + commitHash);
    }
}