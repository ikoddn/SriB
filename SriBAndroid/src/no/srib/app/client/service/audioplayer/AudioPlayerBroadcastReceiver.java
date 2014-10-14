package no.srib.app.client.service.audioplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import no.srib.app.client.MainActivity;
import no.srib.app.client.event.ManualExitEvent;
import no.srib.app.client.notification.NotificationService;
import no.srib.app.client.service.audioplayer.state.State;
import no.srib.app.client.util.BusProvider;

/**
* @author Jostein Eriksen
*/
public class AudioPlayerBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		switch(intent.getAction()) {
			case "no.srib.app.client.PLAY_PAUSE":
				// if app is not running, start it?
				AudioPlayerService audioService = AudioPlayerService.getService();
				if(audioService != null) {
					State state = AudioPlayerService.getService().getState();
					if (state == State.PAUSED || state == State.STOPPED)
						AudioPlayerService.getService().start();
					else if (state == State.STARTED)
						AudioPlayerService.getService().pause();
				}
				else {
					Intent startActivityIntent = new Intent(context, MainActivity.class);
					startActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(startActivityIntent);
				}

//				Toast.makeText(context, "Play / Pause", Toast.LENGTH_LONG).show();
				break;

			case "no.srib.app.client.EXIT":
				// destroy yhe notification
				NotificationService.hide(context);
				BusProvider.INSTANCE.get().post(new ManualExitEvent());
				break;
		}


	}
}
