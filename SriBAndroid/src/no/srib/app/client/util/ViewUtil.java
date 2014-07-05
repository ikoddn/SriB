package no.srib.app.client.util;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
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

	public static int dpiToPixels(final Resources resources, final int dpi) {
		float scale = resources.getDisplayMetrics().density;
		return (int) (dpi * scale + 0.5f);
	}
}
