package no.srib.app.client.fragment;

import no.srib.app.client.R;
import no.srib.app.client.util.ImageUtil;
import no.srib.app.client.util.ViewUtil;
import no.srib.app.client.view.DTImageView;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class LiveRadioFragment extends BaseFragment {

	private static final String PREFS_NAME = "prefsLiveRadio";
	private static final String KEY_STATUS = "status";
	private static final String KEY_STREAM = "stream";

	@InjectView(R.id.dtImageView_liveradio_background) DTImageView background;
	@InjectView(R.id.button_liveradio_info) ImageButton infoButton;
	@InjectView(R.id.button_liveradio_playpause) ImageButton playPauseButton;
	@InjectView(R.id.button_liveradio_stop) ImageButton stopButton;
	@InjectView(R.id.button_liveradio_radiopodcastswitch) CheckBox radioPodcastSwitch;
	@InjectView(R.id.button_liveradio_instagram) ImageButton instagramButton;
	@InjectView(R.id.button_liveradio_soundcloud) ImageButton soundCloudButton;
	@InjectView(R.id.button_liveradio_twitter) ImageButton twitterButton;
	@InjectView(R.id.seekBar_liveradio) SeekBar seekbar;
	@InjectView(R.id.textview_liveradio_status) TextView statusTextView;
	@InjectView(R.id.textview_liveradio_stream) TextView streamTextView;
	@InjectView(R.id.textview_liveradio_programname) TextView programNameTextView;
	@InjectView(R.id.textview_liveradio_time) TextView timeTextView;

	private OnLiveRadioClickListener liveRadioClickListener;
	private Bitmap backgroundBitmap;
	private View rootView;

	private boolean pauseIcon;
	private boolean podcastMode;
	private CharSequence programName;

	public LiveRadioFragment() {
		pauseIcon = false;
		podcastMode = false;
		programName = null;
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

	public void setProgramNameText(final CharSequence text) {
		programName = text;

		if (programNameTextView != null) {
			programNameTextView.setText(text);
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
		ButterKnife.inject(this, rootView);

		Typeface font = Typeface.createFromAsset(rootView.getContext()
				.getAssets(), "fonts/clairehandbold.ttf");
		statusTextView.setTypeface(font);
		streamTextView.setTypeface(font);
		programNameTextView.setTypeface(font);
		timeTextView.setTypeface(font);

		if (programName != null) {
			programNameTextView.setText(programName);
		}

		// TODO Remove when time functionality works
		timeTextView.setText("00:00");

		if (pauseIcon) {
			setPauseIcon();
		}

		if (podcastMode) {
			radioPodcastSwitch.setChecked(true);
		}

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
		viewUtil.setWeight(R.id.linearlayout_liveradio_playpause,
				playButtonWeight);
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
				.findViewById(R.id.linearlayout_liveradio_playpause);
		layout.setWeightSum(horizontalWeightSum);
		viewUtil.setWeight(R.id.view_liveradio_playpause_hspace1, playSpacing);
		viewUtil.setWeight(playPauseButton, playButtonWeight);
		viewUtil.setWeight(R.id.view_liveradio_playpause_hspace2, playSpacing);

		// Horizontal LinearLayout for stop button
		final float stopSpacing = (horizontalWeightSum - smallButtonWeight) / 2;
		layout = (LinearLayout) rootView
				.findViewById(R.id.linearlayout_liveradio_stop);
		layout.setWeightSum(horizontalWeightSum);
		viewUtil.setWeight(R.id.view_liveradio_stop_hspace1, stopSpacing);
		viewUtil.setWeight(stopButton, smallButtonWeight);
		viewUtil.setWeight(R.id.view_liveradio_stop_hspace2, stopSpacing);

		// Horizontal LinearLayout for radio/podcast switch
		final float switchspacing = (horizontalWeightSum - smallButtonWeight) / 2;
		layout = (LinearLayout) rootView
				.findViewById(R.id.linearlayout_liveradio_livePodSwitch);
		layout.setWeightSum(horizontalWeightSum);
		viewUtil.setWeight(R.id.view_liveradio_switch_hspace1, switchspacing);
		viewUtil.setWeight(radioPodcastSwitch, smallButtonWeight);
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
	public void onDestroy() {
		super.onDestroy();

		if (background != null) {
			background.cleanup();
			backgroundBitmap = null;
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
	}

	public void setPauseIcon() {
		pauseIcon = true;

		if (playPauseButton != null) {
			Drawable icon = getResources().getDrawable(
					R.drawable.liveradio_pause);
			playPauseButton.setImageDrawable(icon);
		}
	}

	public void setPlayIcon() {
		pauseIcon = false;

		if (playPauseButton != null) {
			Drawable icon = getResources().getDrawable(
					R.drawable.liveradio_play);
			playPauseButton.setImageDrawable(icon);
		}
	}

	public void setPodcastMode() {
		podcastMode = true;

		if (radioPodcastSwitch != null) {
			radioPodcastSwitch.setChecked(true);
		}
	}

	public void setLiveRadioMode() {
		podcastMode = false;

		if (radioPodcastSwitch != null) {
			radioPodcastSwitch.setChecked(false);
		}
	}

	@OnClick(R.id.button_liveradio_info)
	void infoButtonClicked() {
		liveRadioClickListener.onInfoClicked();
	}

	@OnClick(R.id.button_liveradio_playpause)
	void playPauseButtonClicked() {
		liveRadioClickListener.onPlayPauseClicked();
	}

	@OnClick(R.id.button_liveradio_stop)
	void stopButtonClicked() {
		liveRadioClickListener.onStopClicked();
	}

	@OnClick(R.id.button_liveradio_instagram)
	void instagramButtonClicked() {
		liveRadioClickListener.onInstagramClicked();
	}

	@OnClick(R.id.button_liveradio_soundcloud)
	void soundCloudButtonClicked() {
		liveRadioClickListener.onSoundCloudClicked();
	}

	@OnClick(R.id.button_liveradio_twitter)
	void twitterButtonClicked() {
		liveRadioClickListener.onTwitterClicked();
	}

	@OnCheckedChanged(R.id.button_liveradio_radiopodcastswitch)
	void radioPodcastSwitchCheckedChanged(boolean checked) {
		liveRadioClickListener.onSwitchPodcastSelected(checked);
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
						R.drawable.liveradio_background, width, height);
				backgroundBitmap = Bitmap.createScaledBitmap(bitmap, width,
						height, true);
			}

			background.setBitmap(backgroundBitmap);

			View textFieldLayout = rootView
					.findViewById(R.id.relativelayout_liveradio_textfields);
			final float seekbarWidthFactor = 0.023f;
			int seekbarWidth = (int) (seekbarWidthFactor * width);

			Drawable thumb = res.getDrawable(R.drawable.liveradio_spoleslider);
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
