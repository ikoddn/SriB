package no.srib.app.client.fragment;

import no.srib.app.client.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class ArticleSectionFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_articlesection,
				container, false);

		setRetainInstance(true);


		FragmentManager manager = getChildFragmentManager();

		if (manager.getBackStackEntryCount() == 0) {
			ArticleListFragment fragment = ArticleListFragment
					.newInstance(new ArticleClickedListener());

			FragmentTransaction transaction = manager.beginTransaction();
			transaction.replace(R.id.framelayout_articlesection, fragment);
			transaction.commit();
		}

		return rootView;
	}

	public boolean backPressed() {
		FragmentManager manager = getChildFragmentManager();

		if (manager.getBackStackEntryCount() > 0) {
			manager.popBackStack();
			return true;
		}

		return false;
	}

	private class ArticleClickedListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			String url = (String) view.getTag(R.id.key_article_url);

			FragmentManager manager = getChildFragmentManager();

			FragmentTransaction transaction = manager.beginTransaction();
			transaction.replace(R.id.framelayout_articlesection,
					ArticleFragment.newInstance(url));
			transaction.addToBackStack(null);
			transaction.commit();
		}
	}
}
