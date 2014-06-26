package no.srib.app.client.fragment;

import no.srib.app.client.R;
import no.srib.app.client.util.ImageUtil;
import no.srib.app.client.util.ViewUtil;
import no.srib.app.client.view.DTImageView;
import no.srib.app.client.view.SribSeekBar;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class LiveRadioFragment extends BaseFragment {

	private static final String PREFS_NAME = "prefsLiveRadio";
	private static final String KEY_STATUS = "status";
	private static final String KEY_STREAM = "stream";

	private OnLiveRadioClickListener liveRadioClickListener;
	private TextView statusTextView;
	private TextView streamTextView;
	private TextView programNameTextView;
	private TextView timeTextView;
	private ImageButton playButton;
	private CheckBox switchButton;

	private SribSeekBar seekbar;
	private SeekBarInterface seekBarListener;

	private DTImageView background;
	private View rootView;

	public LiveRadioFragment() {
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

	public void setSeekBarProgress(int value) {
		if (seekbar != null) {
			seekbar.setProgress(value);
		}
	}

	public void setOnLiveRadioClickListener(
			OnLiveRadioClickListener liveRadioClickListener) {
		this.liveRadioClickListener = liveRadioClickListener;
	}

	public void setSeekBarListener(SeekBarInterface seekBar) {
		this.seekBarListener = seekBar;
	}

	public void setProgramNameText(CharSequence text) {
		if (text != null) {
			programNameTextView.setText(text);
		} else {
			programNameTextView.setText("");
		}
	}

	public TextView getProgramNameTextView() {
		return programNameTextView;
	}

	public CharSequence getProgramNameText() {

		return programNameTextView.getText();
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Configuration conf = getParentFragment().getResources()
				.getConfiguration();
		if (conf.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			return null;
		}

		rootView = inflater.inflate(R.layout.fragment_liveradio, container,
				false);

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

		seekbar = (SribSeekBar) rootView.findViewById(R.id.sribSeekBar);

		timeTextView.setTypeface(font);

		// TODO Remove when time functionality works
		timeTextView.setText("00:00");

		playButton = (ImageButton) rootView
				.findViewById(R.id.button_liveradio_play);
		ImageButton stopButton = (ImageButton) rootView
				.findViewById(R.id.button_liveradio_stop);
		switchButton = (CheckBox) rootView
				.findViewById(R.id.button_liveradiopodcastswitch);
		ImageButton infoButton = (ImageButton) rootView
				.findViewById(R.id.button_liveradio_info);
		ImageButton instagramButton = (ImageButton) rootView
				.findViewById(R.id.button_liveradio_instagram);
		ImageButton soundCloudButton = (ImageButton) rootView
				.findViewById(R.id.button_liveradio_soundcloud);
		ImageButton twitterButton = (ImageButton) rootView
				.findViewById(R.id.button_liveradio_twitter);

		playButton.setOnClickListener(new PlayPauseButtonListener());
		stopButton.setOnClickListener(new StopButtonListener());

		twitterButton.setOnClickListener(new TwitterButtonListener());
		switchButton.setOnCheckedChangeListener(new SwitchButtonListener());
		instagramButton.setOnClickListener(new InstagramButtonListener());

		infoButton.setOnClickListener(new InfoButtonListener());
		soundCloudButton.setOnClickListener(new SoundCloudButtonListener());

		ViewTreeObserver observer = rootView.getViewTreeObserver();
		if (observer.isAlive()) {
			observer.addOnGlobalLayoutListener(new LayoutReadyListener());
		}

		final float verticalWeightSum = 1300.0f;
		final float horizontalWeightSum = 780.0f;
		final float smallButtonWeight = 67.0f;
		final float playButtonWeight = 203.0f;
		ViewUtil viewUtil = new ViewUtil(rootView);
		LinearLayout layout;

		// Main vertical LinearLayout
		layout = (LinearLayout) rootView
				.findViewById(R.id.linearlayout_liveradio);
		layout.setWeightSum(verticalWeightSum);
		viewUtil.setWeight(R.id.view_liveradio_vspace1, 27.0f);
		viewUtil.setWeight(R.id.linearlayout_liveradio_info, smallButtonWeight);
		viewUtil.setWeight(R.id.view_liveradio_vspace2, 214.0f);
		viewUtil.setWeight(R.id.relativelayout_liveradio_textfields, 75.0f);
		viewUtil.setWeight(R.id.view_liveradio_vspace3, 211.0f);
		viewUtil.setWeight(R.id.linearlayout_liveradio_play, playButtonWeight);
		viewUtil.setWeight(R.id.view_liveradio_vspace4, 68.0f);
		viewUtil.setWeight(R.id.linearlayout_liveradio_stop, smallButtonWeight);

		viewUtil.setWeight(R.id.view_liveradio_vspace5, 115.0f);
		viewUtil.setWeight(R.id.linearlayout_liveradio_livePodSwitch,
				smallButtonWeight);

		viewUtil.setWeight(R.id.view_liveradio_vspace6, 69.0f);
		viewUtil.setWeight(R.id.linearlayout_liveradio_social,
				smallButtonWeight);
		viewUtil.setWeight(R.id.view_liveradio_vspace7, 50.0f);

		// Horizontal LinearLayout for info button
		layout = (LinearLayout) rootView
				.findViewById(R.id.linearlayout_liveradio_info);
		layout.setWeightSum(horizontalWeightSum);
		viewUtil.setWeight(R.id.view_liveradio_info_hspace1, 678.0f);
		viewUtil.setWeight(infoButton, smallButtonWeight);
		viewUtil.setWeight(R.id.view_liveradio_info_hspace2, 35.0f);

		// Horizontal LinearLayout for text fields
		layout = (LinearLayout) rootView
				.findViewById(R.id.linearlayout_liveradio_textfields);
		layout.setWeightSum(horizontalWeightSum);
		viewUtil.setWeight(R.id.view_liveradio_textfields_hspace1, 130.0f);
		viewUtil.setWeight(programNameTextView, 395.0f);
		viewUtil.setWeight(R.id.view_liveradio_textfields_hspace2, 15.0f);
		viewUtil.setWeight(timeTextView, 104.0f);
		viewUtil.setWeight(R.id.view_liveradio_textfields_hspace3, 136.0f);

		// Horizontal LinearLayout for play button
		final float playSpacing = (horizontalWeightSum - playButtonWeight) / 2;
		layout = (LinearLayout) rootView
				.findViewById(R.id.linearlayout_liveradio_play);
		layout.setWeightSum(horizontalWeightSum);
		viewUtil.setWeight(R.id.view_liveradio_play_hspace1, playSpacing);
		viewUtil.setWeight(playButton, playButtonWeight);
		viewUtil.setWeight(R.id.view_liveradio_play_hspace2, playSpacing);

		// Horizontal LinearLayout for stop button
		final float stopSpacing = (horizontalWeightSum - smallButtonWeight) / 2;
		layout = (LinearLayout) rootView
				.findViewById(R.id.linearlayout_liveradio_stop);
		layout.setWeightSum(horizontalWeightSum);
		viewUtil.setWeight(R.id.view_liveradio_stop_hspace1, stopSpacing);
		viewUtil.setWeight(stopButton, smallButtonWeight);
		viewUtil.setWeight(R.id.view_liveradio_stop_hspace2, stopSpacing);

		// Horixontal LineaLayout for Liveradio/Podcast switch
		final float switchspacing = (horizontalWeightSum - smallButtonWeight) / 2;
		layout = (LinearLayout) rootView
				.findViewById(R.id.linearlayout_liveradio_livePodSwitch);
		layout.setWeightSum(horizontalWeightSum);
		viewUtil.setWeight(R.id.view_liveradio_switch_hspace1, switchspacing);
		viewUtil.setWeight(switchButton, smallButtonWeight);
		viewUtil.setWeight(R.id.view_liveradio_switch_hspace2, switchspacing);

		// Horizontal LinearLayout for social media buttons
		final float socialSpacing = (horizontalWeightSum - 5 * smallButtonWeight) / 2;
		layout = (LinearLayout) rootView
				.findViewById(R.id.linearlayout_liveradio_social);
		layout.setWeightSum(horizontalWeightSum);
		viewUtil.setWeight(R.id.view_liveradio_social_hspace1, socialSpacing);
		viewUtil.setWeight(instagramButton, smallButtonWeight);
		viewUtil.setWeight(R.id.view_liveradio_social_hspace2,
				smallButtonWeight);
		viewUtil.setWeight(soundCloudButton, smallButtonWeight);
		viewUtil.setWeight(R.id.view_liveradio_social_hspace3,
				smallButtonWeight);
		viewUtil.setWeight(twitterButton, smallButtonWeight);
		viewUtil.setWeight(R.id.view_liveradio_social_hspace4, socialSpacing);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		SharedPreferences prefs = getActivity().getSharedPreferences(
				PREFS_NAME, 0);

		String status = prefs.getString(KEY_STATUS, null);
		String stream = prefs.getString(KEY_STREAM, null);

		if (status != null) {
			setStatusText(status);
		}

		if (stream != null) {
			setStreamText(stream);
		}
	}

	@Override
	public void onStop() {
		super.onStop();

		if (statusTextView != null && streamTextView != null) {
			SharedPreferences prefs = getActivity().getSharedPreferences(
					PREFS_NAME, 0);
			SharedPreferences.Editor editor = prefs.edit();

			editor.putString(KEY_STATUS, statusTextView.getText().toString());
			editor.putString(KEY_STREAM, streamTextView.getText().toString());

			editor.commit();
		}
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
	}

	public void setPlayIcon() {
		Drawable icon = getResources().getDrawable(R.drawable.play);
		playButton.setImageDrawable(icon);
	}

	public void setPodcastMode() {
		switchButton.setChecked(true);
	}

	public void setLiveRadioMode() {
		switchButton.setChecked(false);
	}

	private class InfoButtonListener implements OnClickListener {

		@Override
		public void onClick(View button) {
			liveRadioClickListener.onInfoClicked();
		}
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

	private class SoundCloudButtonListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			liveRadioClickListener.onSoundCloudClicked();
		}
	}

	private class TwitterButtonListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			liveRadioClickListener.onTwitterClicked();
		}
	}

	private class SwitchButtonListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			liveRadioClickListener.onSwitchPodcastSelected(isChecked);
		}
	}

	private class LayoutReadyListener implements OnGlobalLayoutListener {

		@Override
		public void onGlobalLayout() {
			ViewUtil.removeOnGlobalLayoutListener(rootView, this);

			int height = rootView.getHeight();
			int width = rootView.getWidth();

			Resources res = getResources();

			Bitmap bitmap = ImageUtil.decodeSampledBitmapFromResource(res,
					R.drawable.layout, width, height);
			background = (DTImageView) rootView
					.findViewById(R.id.dtImageView_liveradio_background);
			background.setBitmap(Bitmap.createScaledBitmap(bitmap, width,
					height, true));

			View textFieldLayout = rootView
					.findViewById(R.id.relativelayout_liveradio_textfields);
			final float seekbarWidthFactor = 0.023f;
			int seekbarWidth = (int) (seekbarWidthFactor * width);

			Drawable thumb = res.getDrawable(R.drawable.spoleslider);
			Drawable thumbScaled = ImageUtil.resize(thumb, seekbarWidth,
					textFieldLayout.getHeight(), res);
			seekbar.setThumb(thumbScaled);
			seekbar.setThumbOffset(0);
			seekbar.setPadding(0, 0, 0, 0);
		}
	}

	public void setMaxOnSeekBar(int max) {
		if (seekbar != null) {
			seekbar.setMax(max);
		}
	}

	public interface OnLiveRadioClickListener {
		void onInfoClicked();

		void onInstagramClicked();

		void onPlayPauseClicked();

		void onSoundCloudClicked();

		void onStopClicked();

		void onTwitterClicked();

		void onSwitchPodcastSelected(boolean value);
	}

	public interface SeekBarInterface {
		void updateSeekBar();
	}
}
