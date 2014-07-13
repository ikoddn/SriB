package no.srib.app.client.util;

import com.squareup.otto.Bus;

/**
 * A singleton for obtaining the Otto event bus.
 * 
 * @author Sveinung
 * 
 */
public enum BusProvider {
	INSTANCE;

	private Bus bus;

	private BusProvider() {
		bus = new Bus();
	}

	public Bus get() {
		return bus;
	}
}
