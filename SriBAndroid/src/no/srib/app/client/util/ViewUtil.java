package no.srib.app.client.util;

import android.view.View;
import android.widget.LinearLayout;

public class ViewUtil {

	public static void setWeight(int id, View rootView, float weight) {
		View view = rootView.findViewById(id);
		setWeight(view, weight);
	}

	public static void setWeight(View view, float weight) {
		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view
				.getLayoutParams();
		layoutParams.weight = weight;
	}
}
