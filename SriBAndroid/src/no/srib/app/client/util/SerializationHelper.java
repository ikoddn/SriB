package no.srib.app.client.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializationHelper {
	/**
	 * Read the object from Base64 string.
	 */
	public static Object deserialize( String s ) {
		byte [] data = Base64Coder.decode( s );
		Object o = null;

		try {
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
			o = ois.readObject();
			ois.close();
		}
		catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		return o;
	}

	/**
	 * Write the object to a Base64 string.
	 */
	public static String serialize( Serializable o )  {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			oos.close();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}

		return new String(Base64Coder.encode(baos.toByteArray()));
	}
}
