package no.srib.app.client.util;

import java.util.HashMap;
import java.util.Map;

import android.content.res.AssetManager;
import android.graphics.Typeface;

/**
 * A singleton for loading custom fonts in Android.
 * 
 * @author Sveinung
 * 
 */
public enum FontFactory {
	INSTANCE;

	private Map<String, Typeface> fontMap;

	private FontFactory() {
		fontMap = new HashMap<String, Typeface>();
	}

	public Typeface getFont(final AssetManager assets, final String font) {
		Typeface typeface = fontMap.get(font);

		if (typeface == null) {
			typeface = Typeface.createFromAsset(assets, font);
			fontMap.put(font, typeface);
		}

		return typeface;
	}
}
