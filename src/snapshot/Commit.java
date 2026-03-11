package snapshot;

import java.io.IOException;
import java.nio.file.*;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Commit {

    private Commit() {
    }

    public static void create(String message) throws Exception {
        ensureRepo();
        Map<String, String> staged = Utils.readIndex();
        if (staged.isEmpty()) {
            System.out.println("Nothing to commit. Use 'snapshot add' to stage files.");
            return;
        }

        // Build commit content: metadata header + one "hash filepath" line per file
        StringBuilder sb = new StringBuilder();
        sb.append("time=").append(System.currentTimeMillis()).append('\n');
        sb.append("message=").append(message).append('\n');
        for (Map.Entry<String, String> e : staged.entrySet()) {
            sb.append(e.getValue()).append(' ').append(e.getKey()).append('\n');
        }
        String commitContent = sb.toString();
        String commitHash = Utils.sha1(commitContent);

        // Write the ref file
        Utils.writeText(Utils.REFS.resolve(commitHash), commitContent);

        // Update HEAD
        Utils.writeText(Utils.HEAD, commitHash);

        // Move staged objects into a per-commit directory
        Path commitDir = Utils.COMMITS_DIR.resolve(commitHash);
        Files.createDirectories(commitDir);
        for (String hash : staged.values()) {
            Path src = Utils.STAGING.resolve(hash);
            if (Files.exists(src)) {
                Files.move(src, commitDir.resolve(hash), StandardCopyOption.REPLACE_EXISTING);
            }
        }

        // Clear the staging index
        Utils.writeText(Utils.INDEX, "");

        System.out.println("commit " + commitHash);
        System.out.println("    " + message);
        System.out.println(staged.size() + " file(s) committed.");
    }

    public static Map<String, String> readCommitFiles(String commitHash) throws IOException {
        Map<String, String> files = new LinkedHashMap<>();
        Path refPath = Utils.REFS.resolve(commitHash);
        if (!Files.exists(refPath))
            return files;
        for (String line : Utils.readText(refPath).split("\n")) {
            if (line.startsWith("time=") || line.startsWith("message=") || line.isBlank())
                continue;
            int space = line.indexOf(' ');
            if (space < 0)
                continue;
            files.put(line.substring(space + 1), line.substring(0, space));
        }
        return files;
    }

    /** Read the commit message from a ref file. */
    public static String readMessage(String commitHash) throws IOException {
        for (String line : Utils.readText(Utils.REFS.resolve(commitHash)).split("\n")) {
            if (line.startsWith("message="))
                return line.substring("message=".length());
        }
        return "(no message)";
    }

    /** Read the timestamp (time millis) from a ref file. */
    public static long readTime(String commitHash) throws IOException {
        for (String line : Utils.readText(Utils.REFS.resolve(commitHash)).split("\n")) {
            if (line.startsWith("time=")) {
                try {
                    return Long.parseLong(line.substring("time=".length()));
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return 0L;
    }

    /** Returns the current HEAD commit hash, or empty string if none. */
    public static String getHead() throws IOException {
        return Utils.readText(Utils.HEAD).trim();
    }

    private static void ensureRepo() {
        if (!Files.isDirectory(Utils.SNAPSHOT)) {
            System.err.println("fatal: not a snapshot repository");
            System.exit(1);
        }
    }
}