package no.srib.app.client.event.handler;

import no.srib.app.client.R;
import no.srib.app.client.event.listener.OnOffSwitchListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class OnOffSeekBarChangeHandler implements OnSeekBarChangeListener {

	private boolean currentlyOn;
	private int progress;
	private OnOffSwitchListener listener;

	public OnOffSeekBarChangeHandler(final OnOffSwitchListener listener) {
		progress = 0;
		currentlyOn = false;
		this.listener = listener;
	}

	@Override
	public void onProgressChanged(final SeekBar seekBar, final int progress,
			final boolean fromUser) {

		this.progress = progress;

		if (!fromUser) {
			if (currentlyOn) {
				if (progress == 0) {
					listener.onToggled(false);
					currentlyOn = false;
				}
			} else {
				if (progress == seekBar.getMax()) {
					listener.onToggled(true);
					currentlyOn = true;
				}
			}
		}
	}

	@Override
	public void onStartTrackingTouch(final SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(final SeekBar seekBar) {
		int max = seekBar.getMax();
		boolean overHalf = progress * 2 > max;

		if (currentlyOn) {
			seekBar.setProgress(overHalf ? max : 0);

			if (progress == 0) {
				onProgressChanged(seekBar, 0, false);
			}
		} else {
			if (overHalf) {
				Toast.makeText(seekBar.getContext(),
						R.string.toast_liveradio_switch_to_podcast_attempt,
						Toast.LENGTH_SHORT).show();
			}

			seekBar.setProgress(0);
		}
	}
}
