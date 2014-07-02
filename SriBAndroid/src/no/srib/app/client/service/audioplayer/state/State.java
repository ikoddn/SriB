package no.srib.app.client.service.audioplayer.state;

/**
 * The different states of the audio player.
 * 
 * @author Sveinung
 * 
 */
public enum State {
	UNINITIALIZED,
	STOPPED,
	PREPARING,
	STARTED,
	PAUSED,
	COMPLETED
}
