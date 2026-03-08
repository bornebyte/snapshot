package snapshot;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

public class Log {

    public static void show() throws Exception {

        Utils utils = new Utils();
        List<String> filePaths = utils.filePathsInDirectory(".snapshot/objects/refs/");

        for (String file : filePaths) {
            System.out.println("Commit  : " + file.replace("refs", "commit"));
            String content = Files.readString(Path.of(file));
            System.out.println(
                    "Date    : " + Instant.ofEpochMilli(Long.parseLong(content.split("\n")[0].replace("Time : ", "")))
                            .atZone(java.time.ZoneId.systemDefault()));
            System.out.println(content.split("\n")[1]);
            String hashes = List.of(content.split("\n")).get(2).replace("[", "").replace("]", "").replace(" ", "");
            List<String> hashList = List.of(hashes.split(","));
            for (String hash : hashList) {
                String correspondFile = utils.hashCorrespondingFile(hash);
                System.out.println(correspondFile);
            }
            System.out.println("--------------------------------------------------");
        }
    }
}