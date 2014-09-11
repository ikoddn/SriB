package no.srib.app.client.service.audioplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import no.srib.app.client.MainActivity;
import no.srib.app.client.service.audioplayer.state.State;

/**
* @author Jostein Eriksen
*/
public class AudioPlayerBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		switch(intent.getAction()) {
			case "no.srib.app.client.PLAY_PAUSE":
				State state = AudioPlayerService.getService().getState();
				if(state == State.PAUSED || state == State.STOPPED)
					AudioPlayerService.getService().start();
				else if(state == State.STARTED)
					AudioPlayerService.getService().pause();

				Toast.makeText(context, "Play / Pause", Toast.LENGTH_LONG).show();
				break;

			case "no.srib.app.client.EXIT":
				((MainActivity) context.getApplicationContext()).finish();
				Toast.makeText(context, "Close", Toast.LENGTH_LONG).show();
				break;
		}
	}
}
