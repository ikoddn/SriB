package no.srib.app.client.fragment;

import no.srib.app.client.R;
import no.srib.app.client.util.ImageUtil;
import no.srib.app.client.util.ViewUtil;
import no.srib.app.client.view.DTImageView;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class InfoFragment extends BaseFragment {

	private Bitmap imageBitmap;
	private DTImageView image;
	private OnInfoClickListener infoClickListener;

	public InfoFragment() {
		infoClickListener = null;
	}

	public void setInfoClickListener(final OnInfoClickListener listener) {
		infoClickListener = listener;
	}

	@Override
	public View onBaseCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Resources res = getParentFragment().getResources();

		Configuration conf = res.getConfiguration();
		if (conf.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			return null;
		}

		View rootView = inflater.inflate(R.layout.fragment_info, container,
				false);

		ImageButton facebookButton = (ImageButton) rootView
				.findViewById(R.id.button_info_facebook);
		ImageButton spotifyButton = (ImageButton) rootView
				.findViewById(R.id.button_info_spotify);
		ImageButton sribwwwButton = (ImageButton) rootView
				.findViewById(R.id.button_info_sribwww);

		facebookButton.setOnClickListener(new FacebookButtonListener());
		spotifyButton.setOnClickListener(new SpotifyButtonListener());
		sribwwwButton.setOnClickListener(new SribwwwButtonListener());

		TextView textView1 = (TextView) rootView
				.findViewById(R.id.textview_info1);

		textView1.setText(Html.fromHtml(getString(R.string.textView_info_1)));

		TextView textView2 = (TextView) rootView
				.findViewById(R.id.textview_info2);

		textView2.setText(Html.fromHtml(getString(R.string.textView_info_2)));

		if (imageBitmap == null) {
			final int imageSizeDPI = 200;
			int imageSizePixels = ViewUtil.dpiToPixels(res, imageSizeDPI);
			Bitmap bitmap = ImageUtil.decodeSampledBitmapFromResource(res,
					R.drawable.app_icon, imageSizePixels, imageSizePixels);
			imageBitmap = Bitmap.createScaledBitmap(bitmap, imageSizePixels,
					imageSizePixels, true);
		}

		image = (DTImageView) rootView.findViewById(R.id.dtImageView_info);
		image.setBitmap(imageBitmap);

		return rootView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (image != null) {
			image.cleanup();
			imageBitmap = null;
		}
	}

	private class FacebookButtonListener implements OnClickListener {

		@Override
		public void onClick(View button) {
			if (infoClickListener != null) {
				infoClickListener.onFacebookClicked();
			}
		}
	}

	private class SpotifyButtonListener implements OnClickListener {

		@Override
		public void onClick(View button) {
			if (infoClickListener != null) {
				infoClickListener.onSpotifyClicked();
			}
		}
	}

	private class SribwwwButtonListener implements OnClickListener {

		@Override
		public void onClick(View button) {
			if (infoClickListener != null) {
				infoClickListener.onSribwwwClicked();
			}
		}
	}

	public interface OnInfoClickListener {
		void onFacebookClicked();

		void onSpotifyClicked();

		void onSribwwwClicked();
	}
}
