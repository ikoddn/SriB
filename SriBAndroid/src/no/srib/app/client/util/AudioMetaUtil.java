package no.srib.app.client.util;

import no.srib.app.client.MainActivity;
import no.srib.app.client.service.audioplayer.AudioPlayerService;

/**
 *
 */
public class AudioMetaUtil {
	static public String getProgramName(AudioPlayerService service) {
		String programName = "";

		switch (service.getDataSourceType()) {
			case LIVE_RADIO:
				if (MainActivity.schedule == null || MainActivity.schedule.getProgram() == null
						|| MainActivity.schedule.getProgram().trim().isEmpty()) {
					programName = service.getCurrentStream().getName();
				} else {
					programName = MainActivity.schedule.getProgram();
				}
				break;

			case PODCAST:
				programName = service.getCurrentPodcast().getProgram();
				break;
			case NONE:
			default:
				break;
		}

		return programName;
	}
}
