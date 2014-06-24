package no.srib.app.client.adapter.updater;

public interface AdapterUpdater<T, U> {

	void setDefaultValue(T defaultValue);

	void updateFrom(U source);
}
