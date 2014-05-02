package no.srib.app.client.fragment;

import no.srib.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class LiveRadioFragment extends Fragment {

	private static final String BUNDLE_STATUS = "status";
	private static final String BUNDLE_STREAM = "stream";

	private OnLiveRadioClickListener liveRadioClickListener;
	private TextView statusTextView;
	private TextView streamTextView;

	public void setOnLiveRadioClickListener(
			OnLiveRadioClickListener liveRadioClickListener) {
		this.liveRadioClickListener = liveRadioClickListener;
	}

	public void setStatusText(CharSequence text) {
		statusTextView.setText(text);
	}

	public void setStreamText(CharSequence text) {
		streamTextView.setText(text);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_liveradio,
				container, false);

		statusTextView = (TextView) rootView
				.findViewById(R.id.textview_liveradio_status);
		streamTextView = (TextView) rootView
				.findViewById(R.id.textview_liveradio_stream);

		if (savedInstanceState != null) {
			statusTextView.setText(savedInstanceState
					.getCharSequence(BUNDLE_STATUS));
			streamTextView.setText(savedInstanceState
					.getCharSequence(BUNDLE_STREAM));
		} else {
			statusTextView.setText("Live radio fragment");
			streamTextView.setText("PLACEHOLDER TEXT");
		}

		Button playButton = (Button) rootView
				.findViewById(R.id.button_liveradio_play);
		playButton.setOnClickListener(new PlayButtonListener());

		Button pauseButton = (Button) rootView
				.findViewById(R.id.button_liveradio_pause);
		pauseButton.setOnClickListener(new PauseButtonListener());

		return rootView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putCharSequence(BUNDLE_STATUS, statusTextView.getText());
		outState.putCharSequence(BUNDLE_STREAM, streamTextView.getText());
	}

	public interface OnLiveRadioClickListener {
		void onPlayClicked();

		void onPauseClicked();
	}

	private class PlayButtonListener implements OnClickListener {

		@Override
		public void onClick(View button) {
			liveRadioClickListener.onPlayClicked();
		}
	}

	private class PauseButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			liveRadioClickListener.onPauseClicked();
		}
	}
}
