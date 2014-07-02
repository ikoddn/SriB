package no.srib.app.client.service.audioplayer.state;

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
