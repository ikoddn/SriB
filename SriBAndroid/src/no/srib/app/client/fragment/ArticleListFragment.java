package no.srib.app.client.fragment;

import no.srib.app.client.R;
import no.srib.app.client.adapter.ArticleListAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ArticleListFragment extends Fragment {

	private ListView listView;
	private TextView label;
	private OnArticlesFragmentReadyListener readyListener;
	private OnItemClickListener articleClickedListener;

	public static ArticleListFragment newInstance(
			OnItemClickListener onArticleClickedListener) {
		ArticleListFragment fragment = new ArticleListFragment();
		fragment.setArticleClickedListener(onArticleClickedListener);
		return fragment;
	}

	public ArticleListFragment() {
		listView = null;
		label = null;
		readyListener = null;
	}

	public void setArticleClickedListener(
			OnItemClickListener articleClickedListener) {
		this.articleClickedListener = articleClickedListener;
	}

	public void setArticleListAdapter(ArticleListAdapter adapter) {
		if (listView != null) {
			listView.setAdapter(adapter);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_articlelist,
				container, false);

		label = (TextView) rootView.findViewById(R.id.label_articlelist);
		label.setText("Articles fragment");

		listView = (ListView) rootView.findViewById(R.id.listview_articlelist);
		listView.setOnItemClickListener(articleClickedListener);

		if (readyListener != null) {
			readyListener.onArticlesFragmentReady();
		}

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			readyListener = (OnArticlesFragmentReadyListener) getActivity();
		} catch (ClassCastException e) {
			readyListener = null;
		}
	}

	public interface OnArticlesFragmentReadyListener {
		void onArticlesFragmentReady();
	}
}
