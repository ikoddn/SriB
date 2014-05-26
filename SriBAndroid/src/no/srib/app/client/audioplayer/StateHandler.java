package no.srib.app.client.audioplayer;

import no.srib.app.client.audioplayer.AudioPlayer.State;
import no.srib.app.client.audioplayer.AudioPlayer.StateListener;

/**
 * This class prevents the assignment of {@code state} without calling the
 * {@code onStateChanged} on the {@code StateListener}.
 * 
 * @author Sveinung
 * 
 */
public class StateHandler {
	
	private State state;
	private StateListener stateListener;

	public StateHandler() {
		state = State.UNINITIALIZED;
		stateListener = null;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;

		if (stateListener != null) {
			stateListener.onStateChanged(state);
		}
	}

	public void setStateListener(StateListener stateListener) {
		this.stateListener = stateListener;
	}
}
