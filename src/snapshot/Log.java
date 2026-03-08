package snapshot;

import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Displays the commit history, newest commit first.
 */
public final class Log {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy Z")
            .withZone(ZoneId.systemDefault());
    private static final String SEP = "─".repeat(60);

    private Log() {
    }

    public static void show() throws Exception {
        if (!Files.isDirectory(Utils.SNAPSHOT)) {
            System.err.println("fatal: not a snapshot repository");
            return;
        }

        List<String> hashes = readAllCommitHashes();
        if (hashes.isEmpty()) {
            System.out.println("No commits yet.");
            return;
        }
        for (String hash : hashes) {
            printCommit(hash);
        }
    }

    private static void printCommit(String hash) throws IOException {
        long time = Commit.readTime(hash);
        String message = Commit.readMessage(hash);
        Map<String, String> files = Commit.readCommitFiles(hash);

        System.out.println(SEP);
        System.out.println("commit  " + hash);
        System.out.println("Date    " + DATE_FMT.format(Instant.ofEpochMilli(time)));
        System.out.println();
        System.out.println("    " + message);
        System.out.println();
        for (String fp : files.keySet()) {
            System.out.println("    " + rel(fp));
        }
        System.out.println();
    }

    /** Returns all commit hashes sorted newest-first by timestamp. */
    private static List<String> readAllCommitHashes() throws IOException {
        List<String> hashes = new ArrayList<>();
        if (!Files.isDirectory(Utils.REFS))
            return hashes;
        try (var stream = Files.list(Utils.REFS)) {
            stream.filter(Files::isRegularFile)
                    .sorted((a, b) -> {
                        try {
                            return Long.compare(
                                    Commit.readTime(b.getFileName().toString()),
                                    Commit.readTime(a.getFileName().toString()));
                        } catch (IOException e) {
                            return 0;
                        }
                    })
                    .forEach(p -> hashes.add(p.getFileName().toString()));
        }
        return hashes;
    }

    private static String rel(String absolutePath) {
        try {
            return Utils.ROOT.relativize(java.nio.file.Path.of(absolutePath)).toString();
        } catch (Exception e) {
            return absolutePath;
        }
    }
}