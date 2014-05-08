package no.srib.app.client.fragment;

import no.srib.R;
import no.srib.app.client.adapter.ArticleListAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class ArticlesFragment extends Fragment {

	private ListView listView;
	private TextView label;
	private OnArticlesFragmentReadyListener readyListener;

	public ArticlesFragment() {
		listView = null;
		label = null;
		readyListener = null;
	}

	public void setArticleListAdapter(ArticleListAdapter adapter) {
		if (listView != null) {
			listView.setAdapter(adapter);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_articles, container,
				false);

		label = (TextView) rootView.findViewById(R.id.label_articles);
		label.setText("Articles fragment");

		listView = (ListView) rootView.findViewById(R.id.listview_articles);

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
