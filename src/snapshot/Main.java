package snapshot;

public class Main {

    public static void main(String[] args) throws Exception {

        Repository repo = new Repository();

        if (args.length == 0) {
            System.out.println("Usage: snapshot <command>");
            return;
        }

        switch (args[0]) {

            case "init":
                repo.init(Utils.snapshotPath);
                break;

            case "status":
                repo.status(Utils.snapshotPath);
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