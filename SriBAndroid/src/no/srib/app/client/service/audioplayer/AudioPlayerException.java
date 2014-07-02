package no.srib.app.client.service.audioplayer;

public class AudioPlayerException extends Exception {

	private static final long serialVersionUID = 1L;

	public AudioPlayerException() {
		super();
	}

	public AudioPlayerException(String msg) {
		super(msg);
	}

	public AudioPlayerException(Throwable cause) {
		super(cause);
	}
}
