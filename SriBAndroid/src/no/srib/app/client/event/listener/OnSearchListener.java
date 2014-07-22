package no.srib.app.client.event.listener;

public interface OnSearchListener {
	void onSearch(String query);

	void restorePreSearchData();
}
