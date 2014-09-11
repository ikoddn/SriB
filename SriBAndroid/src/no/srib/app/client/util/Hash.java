package no.srib.app.client.util;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
	static public String md5(@NotNull String str) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// This will never ever happen... if it does you need to eterminate your gremlins
		}

		md.reset();
		try {
			md.update(str.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// This will never ever happen... if it does you need to eterminate your gremlins better
		}
		byte[] digest = md.digest();
		BigInteger bigInt = new BigInteger(1,digest);
		String hashtext = bigInt.toString(16);
		// Now we need to zero pad it if you actually want the full 32 chars.
		while(hashtext.length() < 32 ){
			hashtext = "0"+hashtext;
		}
		return hashtext;
	}
}
