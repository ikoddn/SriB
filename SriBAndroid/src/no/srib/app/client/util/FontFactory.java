package no.srib.app.client.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseArray;

/**
 * A singleton for loading custom fonts in Android.
 * 
 * @author Sveinung
 * 
 */
public enum FontFactory {
	INSTANCE;

	private SparseArray<Typeface> fontMap;

	private FontFactory() {
		fontMap = new SparseArray<Typeface>();
	}

	public Typeface getFont(final Context context, final int fontId) {
		Typeface typeface = fontMap.get(fontId);

		if (typeface == null) {
			String font = context.getResources().getString(fontId);
			typeface = Typeface.createFromAsset(context.getAssets(), font);
			fontMap.put(fontId, typeface);
		}

		return typeface;
	}
}
