package no.srib.app.client.fragment;

import no.srib.app.client.R;
import no.srib.app.client.util.FontFactory;
import no.srib.app.client.util.ImageUtil;
import no.srib.app.client.util.ViewUtil;
import no.srib.app.client.view.DTImageView;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;

public class LoadingFragment extends BaseFragment {

	private Bitmap backgroundBitmap;
	private DTImageView background;
	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater
				.inflate(R.layout.fragment_loading, container, false);

		ViewTreeObserver observer = rootView.getViewTreeObserver();
		if (observer.isAlive()) {
			observer.addOnGlobalLayoutListener(new LayoutReadyListener());
		}

		TextView textView = (TextView) rootView
				.findViewById(R.id.textView_loading);
		Typeface font = FontFactory.INSTANCE.getFont(getActivity(),
				R.string.font_clairehandbold);
		textView.setTypeface(font);

		return rootView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (background != null) {
			background.cleanup();
			backgroundBitmap = null;
		}
	}

	private class LayoutReadyListener implements OnGlobalLayoutListener {

		@Override
		public void onGlobalLayout() {
			ViewUtil.removeOnGlobalLayoutListener(rootView, this);

			int height = rootView.getHeight();
			int width = rootView.getWidth();

			Resources res = getResources();

			if (backgroundBitmap == null) {
				Bitmap bitmap = ImageUtil.decodeSampledBitmapFromResource(res,
						R.drawable.loading_splash, width, height);
				backgroundBitmap = Bitmap.createScaledBitmap(bitmap, width,
						height, true);
			}

			background = (DTImageView) rootView
					.findViewById(R.id.dtImageView_loading_background);
			background.setBitmap(backgroundBitmap);
		}
	}
}
