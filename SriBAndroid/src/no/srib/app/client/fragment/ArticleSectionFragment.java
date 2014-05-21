package no.srib.app.client.fragment;

import no.srib.app.client.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class ArticleSectionFragment extends SectionFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_articlesection,
				container, false);

		setRetainInstance(true);

		return rootView;
	}

	@Override
	public int getFrameLayoutID() {
		return R.id.framelayout_articlesection;
	}

	@Override
	public Fragment getBaseFragment() {
		return ArticleListFragment.newInstance(new ArticleClickedListener());
	}

	private class ArticleClickedListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			String url = (String) view.getTag(R.id.key_article_url);
			pushFragment(ArticleFragment.newInstance(url));
		}
	}
}
