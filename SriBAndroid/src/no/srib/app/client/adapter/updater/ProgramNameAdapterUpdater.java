package no.srib.app.client.adapter.updater;

import java.util.LinkedList;
import java.util.List;

import no.srib.app.client.adapter.BaseListAdapter;
import no.srib.app.client.model.PodcastPrograms;
import no.srib.app.client.model.ProgramName;

public class ProgramNameAdapterUpdater implements
		AdapterUpdater<ProgramName, PodcastPrograms> {

	private final BaseListAdapter<ProgramName> ADAPTER;
	private boolean didStoreData;
	private List<ProgramName> storedData;
	private ProgramName defaultValue;

	public ProgramNameAdapterUpdater(final BaseListAdapter<ProgramName> adapter) {
		this.ADAPTER = adapter;
		didStoreData = false;
		storedData = null;
		defaultValue = null;
	}

	@Override
	public void restoreStoredData() {
		if (!didStoreData) {
			ADAPTER.setList(storedData);
			ADAPTER.notifyDataSetChanged();
		}
	}

	@Override
	public void setDefaultValue(final ProgramName defaultValue) {
		this.defaultValue = defaultValue;

	}

	@Override
	public void updateFrom(final PodcastPrograms source, final boolean store) {
		List<ProgramName> result = new LinkedList<ProgramName>();

		if (defaultValue != null) {
			result.add(defaultValue);
		}

		result.addAll(source.getNewer());
		result.addAll(source.getOlder());

		if (store) {
			storedData = result;
		}

		didStoreData = store;

		ADAPTER.setList(result);
		ADAPTER.notifyDataSetChanged();
	}
}
