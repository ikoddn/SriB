package no.srib.app.server.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class Security {

    private static final String SALT = "sribbackendsalt";

    public static String toSHA512(String input, String username) {
        String result = null;
        
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            String pwWithSaltAndUsername = SALT + input + username;
            digest.update(pwWithSaltAndUsername.getBytes(StandardCharsets.UTF_8));
            byte[] raw = digest.digest();
            result = Hex.encodeHexString(raw);
        } catch (NoSuchAlgorithmException e) {
        }

        return result;

    }
}
