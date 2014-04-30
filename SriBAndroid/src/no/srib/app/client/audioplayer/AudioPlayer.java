package no.srib.app.client.audioplayer;

public interface AudioPlayer {

	State getState();

	void setStateListener(StateListener stateListener);

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

	enum State {
		UNINITIALIZED,
		STOPPED,
		PREPARING,
		STARTED,
		PAUSED
	}

	interface StateListener {
		void onStateChanged(State state);
	}
}
