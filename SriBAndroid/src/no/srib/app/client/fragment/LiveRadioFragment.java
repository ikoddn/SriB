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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class LiveRadioFragment extends Fragment {

	private static final String PREFS_NAME = "prefsLiveRadio";
	private static final String KEY_IS_PLAYING = "isPlaying";
	private static final String KEY_STATUS = "status";
	private static final String KEY_STREAM = "stream";

	private boolean playing;
	private OnClickListener infoClickListener;
	private OnLiveRadioClickListener liveRadioClickListener;
	private TextView statusTextView;
	private TextView streamTextView;
	private TextView programNameTextView;
	private ImageButton playButton;
	private DTImageView background;
	private View rootView;

	public static LiveRadioFragment newInstance(
			OnClickListener infoClickListener) {

		LiveRadioFragment fragment = new LiveRadioFragment();
		fragment.setOnInfoClickListener(infoClickListener);

		return fragment;
	}

	public LiveRadioFragment() {
		playing = false;
	}

	private void setOnInfoClickListener(OnClickListener infoClickListener) {
		this.infoClickListener = infoClickListener;
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

		rootView = inflater.inflate(R.layout.fragment_liveradio, container,
				false);

		statusTextView = (TextView) rootView
				.findViewById(R.id.textview_liveradio_status);
		streamTextView = (TextView) rootView
				.findViewById(R.id.textview_liveradio_stream);
		programNameTextView = (TextView) rootView
				.findViewById(R.id.textview_liveradio_programname);

		Typeface font = Typeface.createFromAsset(rootView.getContext()
				.getAssets(), "fonts/clairehandbold.ttf");
		statusTextView.setTypeface(font);
		streamTextView.setTypeface(font);
		programNameTextView.setTypeface(font);

		playButton = (ImageButton) rootView
				.findViewById(R.id.button_liveradio_play);
		ImageButton stopButton = (ImageButton) rootView
				.findViewById(R.id.button_liveradio_stop);
		ImageButton infoButton = (ImageButton) rootView
				.findViewById(R.id.button_liveradio_info);
		ImageButton twitterButton = (ImageButton) rootView
				.findViewById(R.id.button_liveradio_twitter);
		ImageButton instagramButton = (ImageButton) rootView
				.findViewById(R.id.button_liveradio_instagram);

		playButton.setOnClickListener(new PlayPauseButtonListener());
		stopButton.setOnClickListener(new StopButtonListener());
		twitterButton.setOnClickListener(new TwitterButtonListener());
		instagramButton.setOnClickListener(new InstagramButtonListener());
		infoButton.setOnClickListener(infoClickListener);

		ViewTreeObserver observer = rootView.getViewTreeObserver();
		if (observer.isAlive()) {
			observer.addOnGlobalLayoutListener(new LayoutReadyListener());
		}

		float smallButtonWeight = 67.0f;

		// Main vertical LinearLayout
		ViewUtil.setWeight(R.id.view_liveradio_vspace1, rootView, 27.0f);
		ViewUtil.setWeight(R.id.linearlayout_liveradio_info, rootView,
				smallButtonWeight);
		ViewUtil.setWeight(R.id.view_liveradio_vspace2, rootView, 234.0f);
		ViewUtil.setWeight(programNameTextView, 40.0f);
		ViewUtil.setWeight(R.id.view_liveradio_vspace3, rootView, 226.0f);
		ViewUtil.setWeight(playButton, 203.0f);
		ViewUtil.setWeight(R.id.view_liveradio_vspace4, rootView, 68.0f);
		ViewUtil.setWeight(stopButton, smallButtonWeight);
		ViewUtil.setWeight(R.id.view_liveradio_vspace5, rootView, 251.0f);
		ViewUtil.setWeight(R.id.linearlayout_liveradio_social, rootView,
				smallButtonWeight);
		ViewUtil.setWeight(R.id.view_liveradio_vspace6, rootView, 50.0f);

		// Horizontal LinearLayout for info button
		ViewUtil.setWeight(R.id.view_liveradio_info_hspace1, rootView, 678.0f);
		ViewUtil.setWeight(infoButton, smallButtonWeight);
		ViewUtil.setWeight(R.id.view_liveradio_info_hspace2, rootView, 35.0f);

		// Horizontal LinearLayout for social media buttons
		ViewUtil.setWeight(R.id.view_liveradio_social_hspace1, rootView, 289.0f);
		ViewUtil.setWeight(instagramButton, smallButtonWeight);
		ViewUtil.setWeight(R.id.view_liveradio_social_hspace2, rootView, 68.0f);
		ViewUtil.setWeight(twitterButton, smallButtonWeight);
		ViewUtil.setWeight(R.id.view_liveradio_social_hspace3, rootView, 289.0f);

		return rootView;
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

		Log.d("SriB", "onActivityCreated");

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

		Log.d("SriB", "onStop");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		if (background != null) {
			background.cleanup();
		}

		Log.d("SriB", "onDestroyView");
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

	public interface OnLiveRadioClickListener {
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
		public void onClick(View view) {
			liveRadioClickListener.onTwitterClicked();
		}
	}

	private class LayoutReadyListener implements OnGlobalLayoutListener {

		@Override
		public void onGlobalLayout() {
			ViewUtil.removeOnGlobalLayoutListener(rootView, this);

			int height = rootView.getHeight();
			int width = rootView.getWidth();

			background = (DTImageView) rootView
					.findViewById(R.id.dtImageView_liveradio_background);

			Activity activity = getParentFragment().getActivity();
			Bitmap bitmap = BitmapUtil.decodeSampledBitmapFromResource(
					activity.getResources(), R.drawable.layout, width, height);

			background.setBitmap(Bitmap.createScaledBitmap(bitmap, width,
					height, true));
		}
	}
}
