package no.srib.app.client.fragment;

import no.srib.app.client.R;
import no.srib.app.client.util.DTImageView;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

	public LiveRadioFragment() {
		playing = false;
	}

	public void setOnLiveRadioClickListener(
			OnLiveRadioClickListener liveRadioClickListener) {
		this.liveRadioClickListener = liveRadioClickListener;
	}

	public void setProgramNameText(CharSequence text) {
		if (text != null) {
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
		final View rootView = inflater.inflate(R.layout.fragment_liveradio,
				container, false);

		rootView.post(new Runnable() {
			public void run() {
				int height = rootView.getHeight();
				int width = rootView.getWidth();

				DTImageView bgImageView = (DTImageView) rootView
						.findViewById(R.id.dtImageView_liveradio_background);

				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.layout);

				bgImageView.setBitmap(Bitmap.createScaledBitmap(bitmap, width,
						height, true));
			}
		});

		statusTextView = (TextView) rootView
				.findViewById(R.id.textview_liveradio_status);
		streamTextView = (TextView) rootView
				.findViewById(R.id.textview_liveradio_stream);
		programNameTextView = (TextView) rootView
				.findViewById(R.id.textview_liveradio_programname);

		statusTextView.setText("Live radio fragment");
		streamTextView.setText("PLACEHOLDER TEXT");

		Typeface appFont = Typeface.createFromAsset(rootView.getContext()
				.getAssets(), "fonts/clairehandbold.ttf");
		statusTextView.setTypeface(appFont);
		streamTextView.setTypeface(appFont);
		programNameTextView.setTypeface(appFont);

		playButton = (ImageButton) rootView
				.findViewById(R.id.button_liveradio_play);
		playButton.setOnClickListener(new PlayPauseButtonListener());

		ImageButton stopButton = (ImageButton) rootView
				.findViewById(R.id.button_liveradio_stop);
		stopButton.setOnClickListener(new StopButtonListener());

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

	public interface OnLiveRadioClickListener {
		void onPlayPauseClicked();

		void onStopClicked();
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
