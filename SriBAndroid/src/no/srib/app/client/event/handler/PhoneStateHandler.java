package no.srib.app.client.event.handler;

import no.srib.app.client.service.audioplayer.AudioPlayerService;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class PhoneStateHandler extends PhoneStateListener {

	private AudioPlayerService audioPlayer;

	public PhoneStateHandler(final AudioPlayerService audioPlayer) {
		this.audioPlayer = audioPlayer;
	}

	@Override
	public void onCallStateChanged(final int state, final String incomingNumber) {
		super.onCallStateChanged(state, incomingNumber);

		switch (state) {
		case TelephonyManager.CALL_STATE_OFFHOOK:
		case TelephonyManager.CALL_STATE_RINGING:
			switch (audioPlayer.getDataSourceType()) {
			case LIVE_RADIO:
				audioPlayer.stop();
				break;
			case PODCAST:
				audioPlayer.pause();
				break;
			case NONE:
			default:
				break;
			}
			break;
		case TelephonyManager.CALL_STATE_IDLE:
		default:
			break;
		}
	}
}
