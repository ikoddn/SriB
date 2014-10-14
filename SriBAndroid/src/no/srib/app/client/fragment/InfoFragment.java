package no.srib.app.client.fragment;

import no.srib.app.client.R;
import no.srib.app.client.event.listener.OnInfoClickListener;
import no.srib.app.client.util.ImageUtil;
import no.srib.app.client.util.ViewUtil;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class InfoFragment extends BaseFragment {

	@InjectView(R.id.dtimageview_info) ImageView image;
	@InjectView(R.id.button_info_facebook) ImageButton facebookButton;
	@InjectView(R.id.button_info_spotify) ImageButton spotifyButton;
	@InjectView(R.id.button_info_sribwebsite) ImageButton sribWebsiteButton;
	@InjectView(R.id.textview_info1) TextView infoText1;
	@InjectView(R.id.textview_info2) TextView infoText2;

	private Bitmap imageBitmap;
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
		ButterKnife.inject(this, rootView);

		infoText1.setText(Html.fromHtml(getString(R.string.textView_info_1)));
		infoText2.setText(Html.fromHtml(getString(R.string.textView_info_2)));

		if (imageBitmap == null) {
			final int imageSizeDIP = 200;
			int imageSizePixels = ViewUtil.dipToPixels(res, imageSizeDIP);
			Bitmap bitmap = ImageUtil.decodeSampledBitmapFromResource(res,
					R.drawable.app_icon_design, imageSizePixels, imageSizePixels);
			imageBitmap = Bitmap.createScaledBitmap(bitmap, imageSizePixels,
					imageSizePixels, true);
		}

		image.setImageBitmap(imageBitmap);

		return rootView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

//		if (image != null) {
//			image.cleanup();
//			imageBitmap = null;
//		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
	}

	@OnClick(R.id.button_info_facebook)
	void facebookButtonClicked() {
		infoClickListener.onFacebookClicked();
	}

	@OnClick(R.id.button_info_spotify)
	void spotifyButtonClicked() {
		infoClickListener.onSpotifyClicked();
	}

	@OnClick(R.id.button_info_sribwebsite)
	void sribWebsiteButtonClicked() {
		infoClickListener.onSribWebsiteClicked();
	}
}
