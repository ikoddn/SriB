package no.srib.app.client.util;

import java.lang.reflect.Field;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridView;
import android.widget.LinearLayout;

public class ViewUtil {

	private final View rootView;

	public ViewUtil(final View rootView) {
		this.rootView = rootView;
	}

	public void setWeight(int id, float weight) {
		View view = rootView.findViewById(id);
		setWeight(view, weight);
	}

	public void setWeight(View view, float weight) {
		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view
				.getLayoutParams();
		layoutParams.weight = weight;
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public static void removeOnGlobalLayoutListener(View v,
			ViewTreeObserver.OnGlobalLayoutListener listener) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			v.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
		} else {
			v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
		}
	}

	public static int dipToPixels(final Resources resources, final int dip) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.getDisplayMetrics());
	}

	@SuppressLint("NewApi")
	public static int getColumnWidth(final GridView gridView) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			return gridView.getColumnWidth();
		} else {
			try {
				Field field = GridView.class.getDeclaredField("mColumnWidth");
				field.setAccessible(true);
				Integer value = (Integer) field.get(gridView);
				field.setAccessible(false);

				return value.intValue();
			} catch (NoSuchFieldException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
