package snapshot;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            printHelp();
            return;
        }

        try {
            switch (args[0]) {
                case "init" -> Repository.init();
                case "add" -> {
                    if (args.length < 2) {
                        System.err.println("Usage: snapshot add <file> [file2 ...]");
                        return;
                    }
                    Index.add(Arrays.copyOfRange(args, 1, args.length));
                }
                case "rm" -> {
                    if (args.length < 2) {
                        System.err.println("Usage: snapshot rm <file>");
                        return;
                    }
                    Index.remove(args[1]);
                }
                case "commit" -> {
                    if (args.length < 2) {
                        System.err.println("Usage: snapshot commit <message>");
                        return;
                    }
                    Commit.create(args[1]);
                }
                case "status" -> Repository.status();
                case "log" -> Log.show();
                default -> {
                    System.err.println("Unknown command: '" + args[0] + "'");
                    System.err.println("Run 'snapshot' without arguments for usage.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void printHelp() {
        System.out.println("Usage: snapshot <command> [args]");
        System.out.println();
        System.out.println("Commands:");
        System.out.println("  init                Initialize a new repository");
        System.out.println("  add <file> [...]    Stage files for commit (use '.' for all)");
        System.out.println("  rm  <file>          Remove a file from the staging area");
        System.out.println("  commit <message>    Commit staged changes");
        System.out.println("  status              Show working directory and staging status");
        System.out.println("  log                 Show commit history");
    }
}