package no.srib.app.client.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import no.srib.app.client.R;

public class TextViewPlus extends TextView {
	private static Map<String, Typeface> typefaces = new HashMap<>();

	public TextViewPlus(Context context) {
		super(context);
	}

	public TextViewPlus(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCustomFont(context, attrs);
	}

	public TextViewPlus(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setCustomFont(context, attrs);
	}

	private void setCustomFont(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextViewPlus);
		String customFont = a.getString(R.styleable.TextViewPlus_assetFont);
		setCustomFont(context, customFont);
		a.recycle();
	}

	public boolean setCustomFont(Context context, String asset) {
		if(isInEditMode())
			return true;
		try {
			if(!typefaces.containsKey(asset))
				typefaces.put(asset, Typeface.createFromAsset(context.getAssets(), "fonts/" + asset));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		setTypeface(typefaces.get(asset));
		return true;
	}
}
