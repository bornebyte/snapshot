package snapshot;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Repository {

    private Repository() {
    }

    public static void init() throws IOException {
        if (Files.isDirectory(Utils.SNAPSHOT)) {
            System.out.println("Snapshot repository already exists at " + Utils.SNAPSHOT);
            return;
        }
        Files.createDirectories(Utils.STAGING);
        Files.createDirectories(Utils.COMMITS_DIR);
        Files.createDirectories(Utils.REFS);
        Files.createFile(Utils.INDEX);
        Files.createFile(Utils.HEAD);
        System.out.println("Initialized empty snapshot repository in " + Utils.SNAPSHOT);
    }

    // ── Status ────────────────────────────────────────────────────────────────

    public static void status() throws Exception {
        if (!Files.isDirectory(Utils.SNAPSHOT)) {
            System.err.println("fatal: not a snapshot repository");
            return;
        }

        // filepath -> hash maps for each layer
        String head = Commit.getHead();
        Map<String, String> committed = head.isEmpty()
                ? new LinkedHashMap<>()
                : Commit.readCommitFiles(head);
        Map<String, String> staged = Utils.readIndex();
        Map<String, String> working = scanWorkingDirectory();

        // ── Changes to be committed ──────────────────────────────────────────
        System.out.println("Changes to be committed:");
        boolean hasStaged = false;
        for (Map.Entry<String, String> e : staged.entrySet()) {
            String fp = e.getKey();
            String committedHash = committed.get(fp);
            if (committedHash == null) {
                System.out.println("  new file:   " + rel(fp));
                hasStaged = true;
            } else if (!committedHash.equals(e.getValue())) {
                System.out.println("  modified:   " + rel(fp));
                hasStaged = true;
            }
        }
        if (!hasStaged)
            System.out.println("  (nothing staged)");

        // ── Changes not staged for commit ────────────────────────────────────
        System.out.println("\nChanges not staged for commit:");
        boolean hasUnstaged = false;
        // baseline: staged overrides committed — compare working dir against latest
        // known state
        Map<String, String> baseline = new LinkedHashMap<>(committed);
        baseline.putAll(staged);
        for (Map.Entry<String, String> e : baseline.entrySet()) {
            String fp = e.getKey();
            String workHash = working.get(fp);
            if (workHash == null) {
                System.out.println("  deleted:    " + rel(fp));
                hasUnstaged = true;
            } else if (!workHash.equals(e.getValue())) {
                System.out.println("  modified:   " + rel(fp));
                hasUnstaged = true;
            }
        }
        if (!hasUnstaged)
            System.out.println("  (nothing)");

        // ── Untracked files ──────────────────────────────────────────────────
        System.out.println("\nUntracked files:");
        boolean hasUntracked = false;
        for (String fp : working.keySet()) {
            if (!staged.containsKey(fp) && !committed.containsKey(fp)) {
                System.out.println("  " + rel(fp));
                hasUntracked = true;
            }
        }
        if (!hasUntracked)
            System.out.println("  (nothing)");

        if (head.isEmpty())
            System.out.println("\nNo commits yet.");
    }

    private static Map<String, String> scanWorkingDirectory() throws IOException {
        Map<String, String> map = new LinkedHashMap<>();
        try (var stream = Files.walk(Utils.ROOT)) {
            stream.filter(Files::isRegularFile)
                    .filter(p -> !p.toAbsolutePath().startsWith(Utils.SNAPSHOT))
                    .forEach(p -> {
                        try {
                            if (Utils.isTextFile(p)) {
                                String content = Files.readString(p, StandardCharsets.UTF_8);
                                map.put(p.toAbsolutePath().toString(), Utils.sha1(content));
                            }
                        } catch (IOException ignored) {
                        }
                    });
        }
        return map;
    }

    private static String rel(String absolutePath) {
        try {
            return Utils.ROOT.relativize(Path.of(absolutePath)).toString();
        } catch (Exception e) {
            return absolutePath;
        }
    }
}