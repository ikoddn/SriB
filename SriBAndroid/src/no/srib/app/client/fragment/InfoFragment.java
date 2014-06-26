package no.srib.app.client.fragment;

import no.srib.app.client.R;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoFragment extends BaseFragment {

	private OnInfoClickListener infoClickListener;

	public InfoFragment() {
		infoClickListener = null;
	}

	public void setInfoClickListener(final OnInfoClickListener listener) {
		infoClickListener = listener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Configuration conf = getParentFragment().getResources()
				.getConfiguration();
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

		textView1.setText(Html.fromHtml(getString(R.string.textView_info1)));

		ImageView image = (ImageView) rootView
				.findViewById(R.id.imageView_infofragment);
		image.setImageDrawable(getResources().getDrawable(R.drawable.appikon));

		TextView textView2 = (TextView) rootView
				.findViewById(R.id.textview_info2);

		textView2.setText(Html.fromHtml(getString(R.string.textView_info2)));

		return rootView;
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
