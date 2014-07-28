package no.srib.app.client.event.listener;

import no.srib.app.client.model.Podcast;
import no.srib.app.client.model.StreamSchedule;

public interface AudioPlayerListener {

	void onSwitchToPodcast(Podcast podcast);

	void onSwitchToStreamSchedule(StreamSchedule streamSchedule);
}
