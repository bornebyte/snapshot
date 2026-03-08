package snapshot;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.File;
import java.io.IOException;

public class Repository {

    public void init(Path snapshotPath) {

        File repo = snapshotPath.toFile();

        if (repo.exists()) {
            System.out.println("Snapshot repository already exists.");
            return;
        }

        repo.mkdir();
        new File(repo, "objects").mkdir();
        new File(repo, "objects/staging").mkdir();
        new File(repo, "objects/commits").mkdir();
        new File(repo, "objects/refs").mkdir();

        System.out.println("Initialized empty snapshot repository in " + snapshotPath);
    }

    public void status(Path snapshotPath) throws IOException {

        if (Files.isDirectory(snapshotPath)) {
            List<String> hashes = List.of();

            try (Stream<Path> pathStream = Files.walk(Utils.stagingPath)) {
                hashes = pathStream
                        .filter(Files::isRegularFile)
                        .map(path -> path.getFileName().toString())
                        .collect(Collectors.toList());
            }
            System.out.println("Staged files : ");
            for (String hash : hashes) {
                System.out.println(hash);
            }
        } else {
            System.out.println("Not a snapshot repository.");
        }
    }
}