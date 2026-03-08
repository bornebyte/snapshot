package snapshot;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws Exception {

        Repository repo = new Repository();

        if (args.length == 0) {
            System.out.println("Usage: snapshot <command>");
            return;
        }

        Path currentPath = Paths.get(".").toAbsolutePath().normalize();
        Path snapshotPath = currentPath.resolve(".snapshot");

        switch (args[0]) {

            case "init":
                repo.init(snapshotPath);
                break;

            case "status":
                repo.status(snapshotPath);
                break;

            case "add":
                try {
                    Index.add(args[1]);
                } catch (Exception e) {
                    System.out.println("Error adding file: " + e.getMessage());
                }
                break;

            case "commit":
                try {
                    Commit.commit(args[1]);
                } catch (Exception e) {
                    System.out.println("Error committing: " + e.getMessage());
                }
                break;

            case "log":
                try {
                    Log.show();
                } catch (Exception e) {
                    System.out.println("Error showing log: " + e.getMessage());
                }
                break;

            default:
                System.out.println("Unknown command: " + args[0]);
        }
    }
}