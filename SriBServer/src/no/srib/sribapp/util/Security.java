package no.srib.sribapp.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class Security {

    
    public static String toSHA512(String input, String username){
       
        String salt = "sribbackendsalt";
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String pwWithSaltAndUsername = salt + input + username;
        digest.update(pwWithSaltAndUsername.getBytes());
        byte[] raw = digest.digest();
        String result = Hex.encodeHexString(raw);
        
        return result;
        
    }
    
    
    
}
