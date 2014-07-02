package no.srib.app.client.service.audioplayer.state;

/**
 * The audio player's state listener.
 * 
 * @author Sveinung
 * 
 */
public interface StateListener {
	/**
	 * Called when the audio player's state is changed.
	 * 
	 * @param state
	 *            - The audio player's new state.
	 */
	void onStateChanged(State state);
}
