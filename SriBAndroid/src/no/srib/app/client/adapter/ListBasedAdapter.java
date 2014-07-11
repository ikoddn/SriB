package no.srib.app.client.adapter;

import java.util.List;

import android.widget.BaseAdapter;

public abstract class ListBasedAdapter<T> extends BaseAdapter {

	private List<T> list;

	public ListBasedAdapter() {
		list = null;
	}

	public ListBasedAdapter(final List<T> list) {
		this.list = list;
	}

	public void setList(final List<T> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public T getItem(final int position) {
		return list == null ? null : list.get(position);
	}
}
