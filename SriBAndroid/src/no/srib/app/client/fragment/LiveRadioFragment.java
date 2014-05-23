package no.srib.app.client.fragment;

import no.srib.app.client.R;
import no.srib.app.client.util.BitmapUtil;
import no.srib.app.client.util.DTImageView;
import no.srib.app.client.util.ViewUtil;
import no.srib.app.client.view.SribSeekBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar.OnSeekBarChangeListener;
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
	private TextView timeTextView;
	private ImageButton playButton;

	private SribSeekBar seekbar;
	private Button devButton;
	private OnLiveRadioFragmentReadyListener liveRadioReadyListener;

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
		statusTextView = null;
		streamTextView = null;
		programNameTextView = null;
		timeTextView = null;
		playButton = null;
	}

	public void setSeekBarOnChangeListener(
			OnSeekBarChangeListener seekBarListener) {
		if (seekbar != null) {
			seekbar.setOnSeekBarChangeListener(seekBarListener);
		}
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

	public void setTimeText(CharSequence text) {
		if (timeTextView != null) {
			timeTextView.setText(text);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			liveRadioReadyListener = (OnLiveRadioFragmentReadyListener) getActivity();
		} catch (ClassCastException e) {
			liveRadioReadyListener = null;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_liveradio, container,
				false);

		// seekbar = (SribSeekBar) rootView.findViewById(R.id.sribSeekBar);
		statusTextView = (TextView) rootView
				.findViewById(R.id.textview_liveradio_status);
		streamTextView = (TextView) rootView
				.findViewById(R.id.textview_liveradio_stream);
		programNameTextView = (TextView) rootView
				.findViewById(R.id.textview_liveradio_programname);
		timeTextView = (TextView) rootView
				.findViewById(R.id.textview_liveradio_time);

		Typeface font = Typeface.createFromAsset(rootView.getContext()
				.getAssets(), "fonts/clairehandbold.ttf");
		statusTextView.setTypeface(font);
		streamTextView.setTypeface(font);
		programNameTextView.setTypeface(font);
		timeTextView.setTypeface(font);

		// TODO Remove when time functionality works
		timeTextView.setText("23:57");

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

		final float smallButtonWeight = 67.0f;
		ViewUtil viewUtil = new ViewUtil(rootView);

		// Main vertical LinearLayout
		viewUtil.setWeight(R.id.view_liveradio_vspace1, 27.0f);
		viewUtil.setWeight(R.id.linearlayout_liveradio_info, smallButtonWeight);
		viewUtil.setWeight(R.id.view_liveradio_vspace2, 234.0f);
		viewUtil.setWeight(R.id.linearlayout_liveradio_textfields, 40.0f);
		viewUtil.setWeight(R.id.view_liveradio_vspace3, 226.0f);
		ViewUtil.setWeight(playButton, 203.0f);
		viewUtil.setWeight(R.id.view_liveradio_vspace4, 68.0f);
		ViewUtil.setWeight(stopButton, smallButtonWeight);
		viewUtil.setWeight(R.id.view_liveradio_vspace5, 251.0f);
		viewUtil.setWeight(R.id.linearlayout_liveradio_social,
				smallButtonWeight);
		viewUtil.setWeight(R.id.view_liveradio_vspace6, 50.0f);

		// Horizontal LinearLayout for text fields
		viewUtil.setWeight(R.id.view_liveradio_textfields_hspace1, 130.0f);
		ViewUtil.setWeight(programNameTextView, 395.0f);
		viewUtil.setWeight(R.id.view_liveradio_textfields_hspace2, 15.0f);
		ViewUtil.setWeight(timeTextView, 104.0f);
		viewUtil.setWeight(R.id.view_liveradio_textfields_hspace3, 136.0f);

		// Horizontal LinearLayout for info button
		viewUtil.setWeight(R.id.view_liveradio_info_hspace1, 678.0f);
		ViewUtil.setWeight(infoButton, smallButtonWeight);
		viewUtil.setWeight(R.id.view_liveradio_info_hspace2, 35.0f);

		// Horizontal LinearLayout for social media buttons
		viewUtil.setWeight(R.id.view_liveradio_social_hspace1, 289.0f);
		ViewUtil.setWeight(instagramButton, smallButtonWeight);
		viewUtil.setWeight(R.id.view_liveradio_social_hspace2, 68.0f);
		ViewUtil.setWeight(twitterButton, smallButtonWeight);
		viewUtil.setWeight(R.id.view_liveradio_social_hspace3, 289.0f);

		// TEST AV MEDIAPLAYER SEEKTO
		// devButton = (Button) rootView.findViewById(R.id.devButton);

		if (liveRadioReadyListener != null) {
			liveRadioReadyListener.onLiveRadioFragmentReady();
		}

		return rootView;
	}

	public void setOnClickListenerDEVBUTTON(OnClickListener click) {
		if (devButton != null) {
			devButton.setOnClickListener(click);
		}
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

	public void setPauseIcon() {
		Drawable icon = getResources().getDrawable(R.drawable.pause);
		playButton.setImageDrawable(icon);
		playing = true;
	}

	public void setPlayIcon() {
		Drawable icon = getResources().getDrawable(R.drawable.play);
		playButton.setImageDrawable(icon);
		playing = false;
	}

	public interface OnLiveRadioClickListener {
		void onPlayPauseClicked();

		void onStopClicked();

		void onInstagramClicked();

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

	public void setMaxOnSeekBar(int max) {
		if (seekbar != null) {
			seekbar.setMax(max);
		}
	}

	public interface OnLiveRadioFragmentReadyListener {
		void onLiveRadioFragmentReady();
	}
}
