package no.srib.app.client.audioplayer;

/**
 * An audio player which can play, stop and pause audio.
 * 
 * @author Sveinung
 * 
 */
public interface AudioPlayer {

	/**
	 * Gets the current state of the audio player. The states are defined in
	 * {@code AudioPlayer.State}.
	 * 
	 * @return The current state
	 */
	State getState();

	/**
	 * Sets the state listener for the audio player. When the audio player
	 * changes state, {@code onStateChanged} on the listener will be called.
	 * 
	 * @param stateListener
	 *            - The {@code StateListener} implementation
	 */
	void setStateListener(StateListener stateListener);

	/**
	 * Sets the data source URI for the audio player.
	 * 
	 * @param dataSource
	 *            - The URI
	 * @throws AudioPlayerException
	 */
	void setDataSource(String dataSource) throws AudioPlayerException;

	/**
	 * Starts the audio player.
	 */
	void start();

	/**
	 * Stops the audio player.
	 */
	void stop();

	/**
	 * Pauses the audio player.
	 */
	void pause();

	/**
	 * Checks if the audio player is currently playing.
	 * 
	 * @return {@code true} if the audio player is playing, {@code false}
	 *         otherwise.
	 */
	boolean isPlaying();

	/**
	 * The different states of the audio player.
	 * 
	 * @author Sveinung
	 * 
	 */
	enum State {
		UNINITIALIZED,
		STOPPED,
		PREPARING,
		STARTED,
		PAUSED
	}

	/**
	 * The audio player's state listener.
	 * 
	 * @author Sveinung
	 * 
	 */
	interface StateListener {
		/**
		 * Called when the audio player's state is changed.
		 * 
		 * @param state
		 *            - The audio player's new state.
		 */
		void onStateChanged(State state);
	}
}
