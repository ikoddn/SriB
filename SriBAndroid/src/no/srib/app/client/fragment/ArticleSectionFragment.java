package no.srib.app.client.fragment;

import no.srib.app.client.R;
import no.srib.app.client.model.Article;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class ArticleSectionFragment extends SectionFragment {

	private ArticleListFragment articleListFragment;

	public ArticleSectionFragment() {
		articleListFragment = null;
	}

	@Override
	public View onBaseCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_articlesection,
				container, false);

		setRetainInstance(true);

		if (articleListFragment == null) {
			articleListFragment = ArticleListFragment
					.newInstance(new ArticleClickedListener());
		}

		return rootView;
	}

	@Override
	public int getFrameLayoutID() {
		return R.id.framelayout_articlesection;
	}

	@Override
	public Fragment getBaseFragment() {
		return articleListFragment;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);

		if (isVisibleToUser) {
			Activity a = getActivity();
			if (a != null) {
				a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
			}
		}
	}

	private class ArticleClickedListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			Article article = (Article) view.getTag();
			String url = article.getPermalink();
			pushFragment(ArticleFragment.newInstance(url));
		}
	}
}
