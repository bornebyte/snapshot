package snapshot;

import java.security.MessageDigest;

public class Utils {

    public static String sha1(String data) throws Exception {

        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] hash = md.digest(data.getBytes());

        StringBuilder hex = new StringBuilder();

        for (byte b : hash) {
            hex.append(String.format("%02x", b));
        }

        return hex.toString();
    }
}
