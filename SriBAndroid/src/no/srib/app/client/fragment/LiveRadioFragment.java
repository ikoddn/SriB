package no.srib.app.client.fragment;

import no.srib.app.client.R;
import no.srib.app.client.event.handler.OnOffSeekBarChangeHandler;
import no.srib.app.client.event.listener.OnOffSwitchListener;
import no.srib.app.client.util.FontFactory;
import no.srib.app.client.util.ImageUtil;
import no.srib.app.client.util.ViewUtil;
import no.srib.app.client.view.DTImageView;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LiveRadioFragment extends BaseFragment {

	@InjectView(R.id.dtimageview_liveradio_background) DTImageView background;
	@InjectView(R.id.button_liveradio_info) ImageButton infoButton;
	@InjectView(R.id.button_liveradio_playpause) ImageButton playPauseButton;
	@InjectView(R.id.button_liveradio_stop) ImageButton stopButton;
	@InjectView(R.id.button_liveradio_instagram) ImageButton instagramButton;
	@InjectView(R.id.button_liveradio_soundcloud) ImageButton soundCloudButton;
	@InjectView(R.id.button_liveradio_twitter) ImageButton twitterButton;
	@InjectView(R.id.seekbar_liveradio) SeekBar seekbar;
	@InjectView(R.id.seekbar_liveradio_radiopodcast) SeekBar radioPodcastSwitch;
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
	private CharSequence status;
	private CharSequence stream;
	private CharSequence time;

	public LiveRadioFragment() {
		pauseIcon = false;
		podcastMode = false;
		programName = null;
		status = null;
		stream = null;
		time = null;
	}

	public void setSeekBarOnChangeListener(
			OnSeekBarChangeListener seekBarListener) {
		seekbar.setOnSeekBarChangeListener(seekBarListener);
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

	public void setStatusText(final CharSequence text) {
		status = text;
		
		if (statusTextView != null) {
			statusTextView.setText(text);
		}
	}

	public void setStreamText(final CharSequence text) {
		stream = text;
		
		if (streamTextView != null) {
			streamTextView.setText(text);
		}
	}

	public void setTimeText(final CharSequence text) {
		time = text;
		
		if (timeTextView != null) {
			timeTextView.setText(text);
		}
	}

	@Override
	public View onBaseCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Configuration conf = getParentFragment().getResources()
				.getConfiguration();
		if (conf.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			return null;
		}

		rootView = inflater.inflate(R.layout.fragment_liveradio, container,
				false);
		ButterKnife.inject(this, rootView);

		Typeface font = FontFactory.INSTANCE.getFont(getActivity(),
				R.string.font_clairehandbold);
		statusTextView.setTypeface(font);
		streamTextView.setTypeface(font);
		programNameTextView.setTypeface(font);
		timeTextView.setTypeface(font);

		if (programName != null) {
			programNameTextView.setText(programName);
		}
		
		if (status != null) {
			statusTextView.setText(status);
		}
		
		if (stream != null) {
			streamTextView.setText(stream);
		}
		
		if (time != null) {
			timeTextView.setText(time);
		}

		if (pauseIcon) {
			setPauseIcon();
		}

		radioPodcastSwitch
				.setOnSeekBarChangeListener(new OnOffSeekBarChangeHandler(
						new RadioPodcastSwitchToggledHandler()));

		if (podcastMode) {
			radioPodcastSwitch.setProgress(radioPodcastSwitch.getMax());
		}

		ViewTreeObserver observer = rootView.getViewTreeObserver();
		if (observer.isAlive()) {
			observer.addOnGlobalLayoutListener(new LayoutReadyListener());
		}

		setLayoutWeights();

		return rootView;
	}

	private void setLayoutWeights() {
		final float verticalWeightSum = 1300.0f;
		final float horizontalWeightSum = 780.0f;
		final float smallButtonSize = 75.0f;

		final float infoButtonTop = 27.0f;
		final float infoButtonLeft = 678.0f;

		final float textFieldsTop = 345.0f;
		final float textFieldsBottom = 392.0f;
		final float textProgramLeft = 145.0f;
		final float textProgramRight = 537.0f;
		final float textTimeLeft = 559.0f;
		final float textTimeRight = 656.0f;

		final float seekBarTop = 326.0f;
		final float seekBarBottom = 407.0f;
		final float seekBarLeft = 133.0f;
		final float seekBarRight = 550.0f;

		final float playButtonTop = 613.0f;
		final float playButtonLeft = 293.0f;
		final float playButtonSize = 214.0f;

		final float stopButtonTop = 884.0f;
		final float stopButtonLeft = 366.0f;

		final float radioPodcastSwitchTop = 1080.0f;
		final float radioPodcastSwitchBottom = 1122.0f;
		final float radioPodcastSwitchLeft = 319.0f;
		final float radioPodcastSwitchRight = 489.0f;

		final float socialButtonsTop = 1190.0f;

		ViewUtil viewUtil = new ViewUtil(rootView);
		LinearLayout layout;

		// Main vertical LinearLayout
		layout = (LinearLayout) rootView.findViewById(R.id.vlayout_liveradio);
		layout.setWeightSum(verticalWeightSum);
		final float vspace1Weight = infoButtonTop;
		final float vinfoWeight = smallButtonSize;
		final float vspace2Weight = textFieldsTop - infoButtonTop
				- smallButtonSize;
		final float vtextFieldsWeight = textFieldsBottom - textFieldsTop;
		final float vspace3Weight = playButtonTop - textFieldsBottom;
		final float vplayWeight = playButtonSize;
		final float vspace4Weight = stopButtonTop - playButtonTop
				- playButtonSize;
		final float vstopWeight = smallButtonSize;
		final float vspace5Weight = radioPodcastSwitchTop - stopButtonTop
				- smallButtonSize;
		final float vradioPodcastWeight = radioPodcastSwitchBottom
				- radioPodcastSwitchTop;
		final float vspace6Weight = socialButtonsTop - radioPodcastSwitchBottom;
		final float vsocialWeight = smallButtonSize;
		final float vspace7Weight = verticalWeightSum - socialButtonsTop
				- smallButtonSize;
		viewUtil.setWeight(R.id.view_liveradio_vspace1, vspace1Weight);
		viewUtil.setWeight(R.id.hlayout_liveradio_info, vinfoWeight);
		viewUtil.setWeight(R.id.view_liveradio_vspace2, vspace2Weight);
		viewUtil.setWeight(R.id.hlayout_liveradio_textfields, vtextFieldsWeight);
		viewUtil.setWeight(R.id.view_liveradio_vspace3, vspace3Weight);
		viewUtil.setWeight(R.id.hlayout_liveradio_playpause, vplayWeight);
		viewUtil.setWeight(R.id.view_liveradio_vspace4, vspace4Weight);
		viewUtil.setWeight(R.id.hlayout_liveradio_stop, vstopWeight);
		viewUtil.setWeight(R.id.view_liveradio_vspace5, vspace5Weight);
		viewUtil.setWeight(R.id.hlayout_liveradio_radiopodcastswitch,
				vradioPodcastWeight);
		viewUtil.setWeight(R.id.view_liveradio_vspace6, vspace6Weight);
		viewUtil.setWeight(R.id.hlayout_liveradio_social, vsocialWeight);
		viewUtil.setWeight(R.id.view_liveradio_vspace7, vspace7Weight);

		// Horizontal LinearLayout for info button
		layout = (LinearLayout) rootView
				.findViewById(R.id.hlayout_liveradio_info);
		layout.setWeightSum(horizontalWeightSum);
		final float hInfoSpace1Weight = infoButtonLeft;
		final float hInfoWeight = smallButtonSize;
		final float hInfoSpace2Weight = horizontalWeightSum - infoButtonLeft
				- smallButtonSize;
		viewUtil.setWeight(R.id.view_liveradio_info_hspace1, hInfoSpace1Weight);
		viewUtil.setWeight(infoButton, hInfoWeight);
		viewUtil.setWeight(R.id.view_liveradio_info_hspace2, hInfoSpace2Weight);

		// Horizontal LinearLayout for text fields
		layout = (LinearLayout) rootView
				.findViewById(R.id.hlayout_liveradio_textfields);
		layout.setWeightSum(horizontalWeightSum);
		final float hTextFieldsSpace1Weight = textProgramLeft;
		final float hTextProgramWeight = textProgramRight - textProgramLeft;
		final float hTextFieldsSpace2Weight = textTimeLeft - textProgramRight;
		final float hTextTimeWeight = textTimeRight - textTimeLeft;
		final float hTextFieldsSpace3Weight = horizontalWeightSum
				- textTimeRight;
		viewUtil.setWeight(R.id.view_liveradio_textfields_hspace1,
				hTextFieldsSpace1Weight);
		viewUtil.setWeight(programNameTextView, hTextProgramWeight);
		viewUtil.setWeight(R.id.view_liveradio_textfields_hspace2,
				hTextFieldsSpace2Weight);
		viewUtil.setWeight(timeTextView, hTextTimeWeight);
		viewUtil.setWeight(R.id.view_liveradio_textfields_hspace3,
				hTextFieldsSpace3Weight);

		// Horizontal LinearLayout for play button
		layout = (LinearLayout) rootView
				.findViewById(R.id.hlayout_liveradio_playpause);
		layout.setWeightSum(horizontalWeightSum);
		final float hPlaySpace1Weight = playButtonLeft;
		final float hPlayWeight = playButtonSize;
		final float hPlaySpace2Weight = horizontalWeightSum - playButtonLeft
				- playButtonSize;
		viewUtil.setWeight(R.id.view_liveradio_playpause_hspace1,
				hPlaySpace1Weight);
		viewUtil.setWeight(playPauseButton, hPlayWeight);
		viewUtil.setWeight(R.id.view_liveradio_playpause_hspace2,
				hPlaySpace2Weight);

		// Horizontal LinearLayout for stop button
		layout = (LinearLayout) rootView
				.findViewById(R.id.hlayout_liveradio_stop);
		layout.setWeightSum(horizontalWeightSum);
		final float hStopSpace1Weight = stopButtonLeft;
		final float hStopWeight = smallButtonSize;
		final float hStopSpace2Weight = horizontalWeightSum - stopButtonLeft
				- smallButtonSize;
		viewUtil.setWeight(R.id.view_liveradio_stop_hspace1, hStopSpace1Weight);
		viewUtil.setWeight(stopButton, hStopWeight);
		viewUtil.setWeight(R.id.view_liveradio_stop_hspace2, hStopSpace2Weight);

		// Horizontal LinearLayout for radio/podcast switch
		layout = (LinearLayout) rootView
				.findViewById(R.id.hlayout_liveradio_radiopodcastswitch);
		layout.setWeightSum(horizontalWeightSum);
		final float hSwitchSpace1Weight = radioPodcastSwitchLeft;
		final float hSwitchWeight = radioPodcastSwitchRight
				- radioPodcastSwitchLeft;
		final float hSwitchSpace2Weight = horizontalWeightSum
				- radioPodcastSwitchRight;
		viewUtil.setWeight(R.id.view_liveradio_switch_hspace1,
				hSwitchSpace1Weight);
		viewUtil.setWeight(R.id.seekbar_liveradio_radiopodcast, hSwitchWeight);
		viewUtil.setWeight(R.id.view_liveradio_switch_hspace2,
				hSwitchSpace2Weight);

		// Horizontal LinearLayout for social media buttons
		final float socialSpacing = (horizontalWeightSum - 5 * smallButtonSize) / 2;
		layout = (LinearLayout) rootView
				.findViewById(R.id.hlayout_liveradio_social);
		layout.setWeightSum(horizontalWeightSum);
		viewUtil.setWeight(R.id.view_liveradio_social_hspace1, socialSpacing);
		viewUtil.setWeight(instagramButton, smallButtonSize);
		viewUtil.setWeight(R.id.view_liveradio_social_hspace2, smallButtonSize);
		viewUtil.setWeight(soundCloudButton, smallButtonSize);
		viewUtil.setWeight(R.id.view_liveradio_social_hspace3, smallButtonSize);
		viewUtil.setWeight(twitterButton, smallButtonSize);
		viewUtil.setWeight(R.id.view_liveradio_social_hspace4, socialSpacing);

		// Vertical LinearLayout for SeekBar
		layout = (LinearLayout) rootView
				.findViewById(R.id.vlayout_liveradio_seekbar);
		layout.setWeightSum(verticalWeightSum);
		final float vSeekBarSpace1Weight = seekBarTop;
		final float vSeekBarWeight = seekBarBottom - seekBarTop;
		final float vSeekBarSpace2Weight = verticalWeightSum - seekBarBottom;
		viewUtil.setWeight(R.id.view_liveradio_seekbar_vspace1,
				vSeekBarSpace1Weight);
		viewUtil.setWeight(R.id.hlayout_liveradio_seekbar, vSeekBarWeight);
		viewUtil.setWeight(R.id.view_liveradio_seekbar_vspace2,
				vSeekBarSpace2Weight);

		// Horizontal layout for SeekBar
		layout = (LinearLayout) rootView
				.findViewById(R.id.hlayout_liveradio_seekbar);
		layout.setWeightSum(horizontalWeightSum);
		final float hSeekBarSpace1Weight = seekBarLeft;
		final float hSeekBarWeight = seekBarRight - seekBarLeft;
		final float hSeekBarSpace2Weight = horizontalWeightSum - seekBarRight;
		viewUtil.setWeight(R.id.view_liveradio_seekbar_hspace1,
				hSeekBarSpace1Weight);
		viewUtil.setWeight(R.id.seekbar_liveradio, hSeekBarWeight);
		viewUtil.setWeight(R.id.view_liveradio_seekbar_hspace2,
				hSeekBarSpace2Weight);
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
			radioPodcastSwitch.setProgress(radioPodcastSwitch.getMax());
		}

		if (seekbar != null) {
			seekbar.setVisibility(View.VISIBLE);
		}
	}

	public void setLiveRadioMode() {
		podcastMode = false;

		if (radioPodcastSwitch != null) {
			radioPodcastSwitch.setProgress(0);
		}

		if (seekbar != null) {
			seekbar.setVisibility(View.INVISIBLE);
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

	private class RadioPodcastSwitchToggledHandler implements
			OnOffSwitchListener {

		@Override
		public void onToggled(final boolean on) {
			liveRadioClickListener.onRadioPodcastSwitchToggled(on);
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
						R.drawable.liveradio_background, width, height);
				backgroundBitmap = Bitmap.createScaledBitmap(bitmap, width,
						height, true);
			}

			background.setBitmap(backgroundBitmap);

			View seekbarLayout = rootView
					.findViewById(R.id.hlayout_liveradio_seekbar);
			final float seekbarWidthFactor = 0.023f;
			final float radioPodcastWidthFactor = seekbarWidthFactor * 3;
			int seekbarWidth = (int) (seekbarWidthFactor * width);

			Drawable thumb = res.getDrawable(R.drawable.liveradio_spoleslider);
			Drawable thumbScaled = ImageUtil.resize(thumb, seekbarWidth,
					seekbarLayout.getHeight(), res);
			seekbar.setThumb(thumbScaled);
			seekbar.setThumbOffset(0);
			seekbar.setPadding(0, 0, 0, 0);

			seekbarLayout = rootView
					.findViewById(R.id.hlayout_liveradio_radiopodcastswitch);
			seekbarWidth = (int) (radioPodcastWidthFactor * width);
			thumb = res.getDrawable(R.drawable.liveradio_npslider);
			thumbScaled = ImageUtil.resize(thumb, seekbarWidth,
					seekbarLayout.getHeight(), res);
			radioPodcastSwitch.setThumb(thumbScaled);
			radioPodcastSwitch.setThumbOffset(0);
			radioPodcastSwitch.setPadding(0, 0, 0, 0);
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

		void onRadioPodcastSwitchToggled(boolean value);
	}
}
