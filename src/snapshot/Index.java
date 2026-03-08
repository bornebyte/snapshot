package snapshot;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Manages the staging area (index).
 * Supports adding single files, multiple files, or entire directory trees.
 */
public final class Index {

    private Index() {
    }

    /**
     * Stage one or more targets. Each target can be:
     * <ul>
     * <li>{@code "."} — recursively stage all text files in the working
     * directory</li>
     * <li>A file path</li>
     * <li>A directory path — recursively stages all text files inside it</li>
     * </ul>
     */
    public static void add(String... targets) throws Exception {
        ensureRepo();
        List<Path> toAdd = new ArrayList<>();

        for (String target : targets) {
            Path resolved = target.equals(".")
                    ? Utils.ROOT
                    : Utils.ROOT.resolve(target).normalize();

            if (!Files.exists(resolved)) {
                System.err.println("fatal: '" + target + "' did not match any files");
                continue;
            }

            if (Files.isDirectory(resolved)) {
                try (var stream = Files.walk(resolved)) {
                    stream.filter(Files::isRegularFile)
                            .filter(p -> !isInsideSnapshot(p))
                            .forEach(toAdd::add);
                }
            } else {
                toAdd.add(resolved);
            }
        }

        if (toAdd.isEmpty())
            return;

        Map<String, String> index = Utils.readIndex();
        int added = 0, skipped = 0;

        for (Path file : toAdd) {
            if (!Utils.isTextFile(file)) {
                System.out.println("skip (binary): " + Utils.ROOT.relativize(file));
                skipped++;
                continue;
            }
            String content = Files.readString(file, StandardCharsets.UTF_8);
            String hash = Utils.sha1(content);
            String key = file.toAbsolutePath().toString();

            Utils.writeText(Utils.STAGING.resolve(hash), content);
            index.put(key, hash);
            System.out.println("staged: " + Utils.ROOT.relativize(file));
            added++;
        }

        Utils.writeIndex(index);
        System.out.println("\n" + added + " file(s) staged"
                + (skipped > 0 ? ", " + skipped + " skipped (binary)" : "") + ".");
    }

    /**
     * Remove a file from the staging index (does not delete it from disk).
     */
    public static void remove(String target) throws IOException {
        ensureRepo();
        Path p = Utils.ROOT.resolve(target).normalize().toAbsolutePath();
        Map<String, String> index = Utils.readIndex();
        if (index.remove(p.toString()) != null) {
            Utils.writeIndex(index);
            System.out.println("unstaged: " + target);
        } else {
            System.err.println("'" + target + "' is not staged");
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static boolean isInsideSnapshot(Path p) {
        return p.toAbsolutePath().startsWith(Utils.SNAPSHOT);
    }

    private static void ensureRepo() {
        if (!Files.isDirectory(Utils.SNAPSHOT)) {
            System.err.println("fatal: not a snapshot repository (run 'snapshot init' first)");
            System.exit(1);
        }
    }
}
