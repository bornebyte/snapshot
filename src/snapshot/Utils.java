package snapshot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

    public static Path currentPath = Paths.get(".").toAbsolutePath().normalize();
    public static Path snapshotPath = currentPath.resolve(".snapshot");

    public static String sha1(String data) throws Exception {

        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] hash = md.digest(data.getBytes());

        StringBuilder hex = new StringBuilder();

        for (byte b : hash) {
            hex.append(String.format("%02x", b));
        }

        return hex.toString();
    }

    public List<String> filePathsInDirectoryRecursive(String directory) throws Exception {
        List<String> filePaths = List.of();
        try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
            filePaths = paths.filter(Files::isRegularFile)
                    .map(path -> path.toString())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePaths;
    }

    public List<String> filePathsInDirectory(String directory) throws Exception {
        List<String> filePaths = List.of();
        try (Stream<Path> paths = Files.list(Paths.get(directory))) {
            filePaths = paths.filter(Files::isRegularFile)
                    .map(path -> path.toString())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePaths;
    }

    public String hashCorrespondingFile(String fileHash) throws Exception {
        String content = Files.readString(Path.of(".snapshot/index"));
        String[] lines = content.split("\n");
        for (String line : lines) {
            String[] parts = line.split(" ", 2);
            if (parts.length == 2 && parts[0].equals(fileHash)) {
                return parts[1];
            }
        }
        return null;
    }
}
