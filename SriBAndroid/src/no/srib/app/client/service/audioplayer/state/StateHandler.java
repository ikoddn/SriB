package no.srib.app.client.service.audioplayer.state;

import java.util.ArrayList;
import java.util.List;

/**
 * This class prevents the assignment of {@code state} without calling the
 * {@code onStateChanged} on the {@code StateListener}.
 * 
 * @author Sveinung
 * 
 */
public class StateHandler {

	private State state;
	private List<StateListener> stateListener;

	public StateHandler() {
		state = State.UNINITIALIZED;
		stateListener = new ArrayList<>();
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;

		if (stateListener != null) {
			for(StateListener listener: stateListener) {
				listener.onStateChanged(state);
			}
		}
	}

	public void removeListeners() {
		stateListener.clear();
	}

	public void addStateListener(StateListener stateListener) {
		this.stateListener.add(stateListener);
	}
}
