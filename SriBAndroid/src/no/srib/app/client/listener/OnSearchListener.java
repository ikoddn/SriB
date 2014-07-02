package no.srib.app.client.listener;

public interface OnSearchListener {
	void onSearch(String query);

	void restorePreSearchData();
}
