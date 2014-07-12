package no.srib.app.client.adapter;

import no.srib.app.client.R;
import no.srib.app.client.model.Article;
import no.srib.app.client.view.ArticleView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ArticleListAdapter extends ListBasedAdapter<Article> {

	private static final int[] DIVIDERS = { R.drawable.list_divider_1,
			R.drawable.list_divider_2, R.drawable.list_divider_3,
			R.drawable.list_divider_4, R.drawable.list_divider_5 };

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
			View view = convertView;

			if (view == null) {
				LayoutInflater inflater = LayoutInflater.from(context);
				view = inflater
						.inflate(R.layout.listitem_article_divider, null);
			}

			int divider = DIVIDERS[position / 2 % DIVIDERS.length];

			ImageView imageView = (ImageView) view
					.findViewById(R.id.imageView_articleItem_divider);
			imageView.setImageResource(divider);

			return view;
		} else {
			ArticleView articleView;

			if (convertView == null) {
				articleView = new ArticleView(context);
			} else {
				articleView = (ArticleView) convertView;
			}

			Article item = getItemInPosition(position);
			articleView.showArticle(item);

			return articleView;
		}
	}
}
