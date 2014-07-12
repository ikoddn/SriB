package no.srib.app.client.adapter;

import no.srib.app.client.model.Article;
import no.srib.app.client.view.ArticleDividerView;
import no.srib.app.client.view.ArticleView;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class ArticleListAdapter extends ListBasedAdapter<Article> {

	private final Context context;

	public ArticleListAdapter(final Context context) {
		this.context = context;
	}

	private Article getItemInPosition(final int position) {
		return super.getItem(position / 2);
	}

	private boolean isDivider(final int position) {
		return position % 2 != 0;
	}

	@Override
	public final int getCount() {
		int listSize = super.getCount();
		return listSize == 0 ? 0 : listSize * 2 - 1;
	}

	@Override
	public final Article getItem(final int position) {
		if (isDivider(position)) {
			return null;
		}

		return getItemInPosition(position);
	}

	@Override
	public final long getItemId(final int position) {
		if (isDivider(position)) {
			return 0;
		}

		return getItemInPosition(position).getId();
	}

	@Override
	public final boolean isEnabled(final int position) {
		return !isDivider(position);
	}

	@Override
	public final int getViewTypeCount() {
		return 2;
	}

	@Override
	public final int getItemViewType(final int position) {
		// 0 = Article list item
		// 1 = List divider
		return position % 2;
	}

	@Override
	public final View getView(final int position, final View convertView,
			final ViewGroup parent) {

		if (isDivider(position)) {
			ArticleDividerView view;

			if (convertView == null) {
				view = new ArticleDividerView(context);
			} else {
				view = (ArticleDividerView) convertView;
			}

			view.showDivider(position / 2);

			return view;
		} else {
			ArticleView view;

			if (convertView == null) {
				view = new ArticleView(context);
			} else {
				view = (ArticleView) convertView;
			}

			Article item = getItemInPosition(position);
			view.showArticle(item);

			return view;
		}
	}
}
