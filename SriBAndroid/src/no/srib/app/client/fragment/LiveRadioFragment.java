package no.srib.app.client.fragment;

import no.srib.app.client.R;
import no.srib.app.client.util.BitmapUtil;
import no.srib.app.client.util.DTImageView;
import no.srib.app.client.util.ViewUtil;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class LiveRadioFragment extends Fragment {

	private static final String PREFS_NAME = "prefsLiveRadio";
	private static final String KEY_IS_PLAYING = "isPlaying";
	private static final String KEY_STATUS = "status";
	private static final String KEY_STREAM = "stream";

	private boolean playing;
	private OnLiveRadioClickListener liveRadioClickListener;
	private TextView statusTextView;
	private TextView streamTextView;
	private TextView programNameTextView;
	private ImageButton playButton;
	private DTImageView background;

	public LiveRadioFragment() {
		playing = false;
	}

	public void setOnLiveRadioClickListener(
			OnLiveRadioClickListener liveRadioClickListener) {
		this.liveRadioClickListener = liveRadioClickListener;
	}

	public void setProgramNameText(CharSequence text) {
		if (programNameTextView != null) {
			programNameTextView.setText(text);
		}
	}

	public void setStatusText(CharSequence text) {
		if (statusTextView != null) {
			statusTextView.setText(text);
		}
	}

	public void setStreamText(CharSequence text) {
		if (streamTextView != null) {
			streamTextView.setText(text);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View root = inflater.inflate(R.layout.fragment_liveradio,
				container, false);

		root.post(new Runnable() {
			public void run() {
				int height = root.getHeight();
				int width = root.getWidth();

				background = (DTImageView) root
						.findViewById(R.id.dtImageView_liveradio_background);

				Activity activity = getParentFragment().getActivity();
				Bitmap bitmap = BitmapUtil.decodeSampledBitmapFromResource(
						activity.getResources(), R.drawable.layout, width,
						height);

				background.setBitmap(Bitmap.createScaledBitmap(bitmap, width,
						height, true));
			}
		});

		statusTextView = (TextView) root
				.findViewById(R.id.textview_liveradio_status);
		streamTextView = (TextView) root
				.findViewById(R.id.textview_liveradio_stream);
		programNameTextView = (TextView) root
				.findViewById(R.id.textview_liveradio_programname);

		Typeface appFont = Typeface.createFromAsset(root.getContext()
				.getAssets(), "fonts/clairehandbold.ttf");
		statusTextView.setTypeface(appFont);
		streamTextView.setTypeface(appFont);
		programNameTextView.setTypeface(appFont);

		playButton = (ImageButton) root
				.findViewById(R.id.button_liveradio_play);
		ImageButton stopButton = (ImageButton) root
				.findViewById(R.id.button_liveradio_stop);
		ImageButton infoButton = (ImageButton) root
				.findViewById(R.id.button_liveradio_info);
		ImageButton twitterButton = (ImageButton) root
				.findViewById(R.id.button_liveradio_twitter);
		ImageButton instagramButton = (ImageButton) root
				.findViewById(R.id.button_liveradio_instagram);

		playButton.setOnClickListener(new PlayPauseButtonListener());
		stopButton.setOnClickListener(new StopButtonListener());
		twitterButton.setOnClickListener(new TwitterButtonListener());
		instagramButton.setOnClickListener(new InstagramButtonListener());

		float smallButtonWeight = 67.0f;

		// Main vertical LinearLayout
		ViewUtil.setWeight(R.id.view_liveradio_vspace1, root, 27.0f);
		ViewUtil.setWeight(R.id.linearlayout_liveradio_info, root,
				smallButtonWeight);
		ViewUtil.setWeight(R.id.view_liveradio_vspace2, root, 234.0f);
		ViewUtil.setWeight(programNameTextView, 40.0f);
		ViewUtil.setWeight(R.id.view_liveradio_vspace3, root, 226.0f);
		ViewUtil.setWeight(playButton, 203.0f);
		ViewUtil.setWeight(R.id.view_liveradio_vspace4, root, 68.0f);
		ViewUtil.setWeight(stopButton, smallButtonWeight);
		ViewUtil.setWeight(R.id.view_liveradio_vspace5, root, 251.0f);
		ViewUtil.setWeight(R.id.linearlayout_liveradio_social, root,
				smallButtonWeight);
		ViewUtil.setWeight(R.id.view_liveradio_vspace6, root, 50.0f);

		// Horizontal LinearLayout for info button
		ViewUtil.setWeight(R.id.view_liveradio_info_hspace1, root, 678.0f);
		ViewUtil.setWeight(infoButton, smallButtonWeight);
		ViewUtil.setWeight(R.id.view_liveradio_info_hspace2, root, 35.0f);

		// Horizontal LinearLayout for social media buttons
		ViewUtil.setWeight(R.id.view_liveradio_social_hspace1, root, 289.0f);
		ViewUtil.setWeight(instagramButton, smallButtonWeight);
		ViewUtil.setWeight(R.id.view_liveradio_social_hspace2, root, 68.0f);
		ViewUtil.setWeight(twitterButton, smallButtonWeight);
		ViewUtil.setWeight(R.id.view_liveradio_social_hspace3, root, 289.0f);

		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		SharedPreferences prefs = getActivity().getSharedPreferences(
				PREFS_NAME, 0);

		String status = prefs.getString(KEY_STATUS, null);
		String stream = prefs.getString(KEY_STREAM, null);
		playing = prefs.getBoolean(KEY_IS_PLAYING, false);

		if (status != null) {
			setStatusText(status);
		}

		if (stream != null) {
			setStreamText(stream);
		}

		if (playing) {
			setPauseIcon();
		}
	}

	@Override
	public void onStop() {
		super.onStop();

		SharedPreferences prefs = getActivity().getSharedPreferences(
				PREFS_NAME, 0);
		SharedPreferences.Editor editor = prefs.edit();

		editor.putString(KEY_STATUS, statusTextView.getText().toString());
		editor.putString(KEY_STREAM, streamTextView.getText().toString());
		editor.putBoolean(KEY_IS_PLAYING, playing);

		editor.commit();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		if (background != null) {
			background.cleanup();
		}
	}

	public interface OnLiveRadioClickListener {
		void onInfoClicked();

		void onInstagramClicked();

		void onPlayPauseClicked();

		void onStopClicked();

		void onTwitterClicked();
	}

	private class PlayPauseButtonListener implements OnClickListener {

		@Override
		public void onClick(View button) {

			liveRadioClickListener.onPlayPauseClicked();
		}
	}

	private class StopButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			liveRadioClickListener.onStopClicked();
		}
	}

	private class InstagramButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			liveRadioClickListener.onInstagramClicked();
		}
	}

	private class TwitterButtonListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			liveRadioClickListener.onTwitterClicked();
		}
	}

	public void setPauseIcon() {
		playButton.setImageDrawable(getResources()
				.getDrawable(R.drawable.pause));
		playing = true;
	}

	public void setPlayIcon() {
		playButton
				.setImageDrawable(getResources().getDrawable(R.drawable.play));
		playing = false;
	}
}
