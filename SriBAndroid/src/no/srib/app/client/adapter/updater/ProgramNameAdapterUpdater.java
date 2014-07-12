package no.srib.app.client.adapter.updater;

import java.util.LinkedList;
import java.util.List;

import no.srib.app.client.adapter.ProgramSpinnerAdapter;
import no.srib.app.client.model.PodcastPrograms;
import no.srib.app.client.model.ProgramName;

public class ProgramNameAdapterUpdater implements
		AdapterUpdater<ProgramName, PodcastPrograms> {

	private final ProgramSpinnerAdapter ADAPTER;
	private boolean didStoreData;
	private List<ProgramName> storedData;

	public ProgramNameAdapterUpdater(final ProgramSpinnerAdapter adapter) {
		this.ADAPTER = adapter;
		didStoreData = false;
		storedData = null;
	}

	@Override
	public void restoreStoredData() {
		if (!didStoreData) {
			ADAPTER.setList(storedData);
			ADAPTER.notifyDataSetChanged();
		}
	}

	@Override
	public void updateFrom(final PodcastPrograms source, final boolean store) {
		List<ProgramName> result = new LinkedList<ProgramName>();

		List<ProgramName> newer = source.getNewer();

		result.addAll(newer);
		result.addAll(source.getOlder());

		if (store) {
			storedData = result;
		}

		didStoreData = store;

		ADAPTER.setList(result);
		ADAPTER.setNewerCount(newer.size());
		ADAPTER.notifyDataSetChanged();
	}
}
