package snapshot;

import java.io.FileWriter;

public class HashObject {

    public static void store(String content) throws Exception {

        String hash = Utils.sha1(content);

        FileWriter fw = new FileWriter(".snapshot/objects/" + hash);
        fw.write(content);
        fw.close();

        System.out.println("Stored object " + hash);
    }
}
