package snapshot;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Static utility methods and shared path constants.
 * All methods are static — do not instantiate this class.
 */
public final class Utils {

    private Utils() {
    }

    // ── Path Constants ────────────────────────────────────────────────────────
    public static final Path ROOT = Paths.get("").toAbsolutePath();
    public static final Path SNAPSHOT = ROOT.resolve(".snapshot");
    public static final Path OBJECTS = SNAPSHOT.resolve("objects");
    public static final Path STAGING = OBJECTS.resolve("staging");
    public static final Path COMMITS_DIR = OBJECTS.resolve("commits");
    public static final Path REFS = OBJECTS.resolve("refs");
    public static final Path INDEX = SNAPSHOT.resolve("index");
    public static final Path HEAD = SNAPSHOT.resolve("HEAD");

    // ── Hashing ───────────────────────────────────────────────────────────────

    public static String sha1(String data) {
        return sha1(data.getBytes(StandardCharsets.UTF_8));
    }

    public static String sha1(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hash = md.digest(data);
            StringBuilder hex = new StringBuilder(40);
            for (byte b : hash)
                hex.append(String.format("%02x", b));
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 not available", e);
        }
    }

    // ── File I/O ─────────────────────────────────────────────────────────────

    /** Read a UTF-8 text file. Returns empty string if the file does not exist. */
    public static String readText(Path path) throws IOException {
        if (!Files.exists(path))
            return "";
        return Files.readString(path, StandardCharsets.UTF_8);
    }

    /** Write UTF-8 text to a file, creating parent directories as needed. */
    public static void writeText(Path path, String content) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, content, StandardCharsets.UTF_8);
    }

    /** Returns true if the file can be decoded as UTF-8 text. */
    public static boolean isTextFile(Path path) {
        try {
            Files.readString(path, StandardCharsets.UTF_8);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ── Index I/O ─────────────────────────────────────────────────────────────

    /**
     * Read the staging index.
     * 
     * @return mutable map of absolute-filepath -&gt; sha1-hash
     */
    public static Map<String, String> readIndex() throws IOException {
        Map<String, String> index = new LinkedHashMap<>();
        String content = readText(INDEX);
        if (content.isBlank())
            return index;
        for (String line : content.split("\n")) {
            line = line.strip();
            if (line.isEmpty())
                continue;
            int space = line.indexOf(' ');
            if (space < 0)
                continue;
            String hash = line.substring(0, space);
            String filepath = line.substring(space + 1);
            index.put(filepath, hash);
        }
        return index;
    }

    /**
     * Persist the staging index to disk.
     * 
     * @param index map of absolute-filepath -&gt; sha1-hash
     */
    public static void writeIndex(Map<String, String> index) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> e : index.entrySet()) {
            sb.append(e.getValue()).append(' ').append(e.getKey()).append('\n');
        }
        writeText(INDEX, sb.toString());
    }
}
