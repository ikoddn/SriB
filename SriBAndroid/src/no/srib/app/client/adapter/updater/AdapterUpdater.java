package no.srib.app.client.adapter.updater;

public interface AdapterUpdater<T, U> {
	void restoreStoredData();

	void updateFrom(U source, boolean store);
}
