package no.srib.app.client.adapter;

import no.srib.app.client.R;
import no.srib.app.client.model.Article;
import no.srib.app.client.util.URLUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class ArticleListAdapter extends BaseListAdapter<Article> {

	private static final int[] DIVIDERS = { R.drawable.list_divider_1,
			R.drawable.list_divider_2, R.drawable.list_divider_3,
			R.drawable.list_divider_4, R.drawable.list_divider_5 };

	private LayoutInflater inflater;

	public ArticleListAdapter(final LayoutInflater inflater) {
		this.inflater = inflater;
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
		View view = convertView;

		if (isDivider(position)) {
			if (view == null) {
				view = inflater.inflate(R.layout.listitem_articlelist_divider,
						null);
			}

			int divider = DIVIDERS[position / 2 % DIVIDERS.length];

			ImageView imageView = (ImageView) view
					.findViewById(R.id.imageView_articleItem_divider);
			imageView.setImageResource(divider);
		} else {
			if (view == null) {
				view = inflater.inflate(R.layout.listitem_articlelist, null);
			}

			ImageView image = (ImageView) view
					.findViewById(R.id.imageview_articleitem);
			TextView title = (TextView) view
					.findViewById(R.id.textview_articleitem_title);
			TextView excerpt = (TextView) view
					.findViewById(R.id.textview_articleitem_excerpt);

			Article newsArticle = getItemInPosition(position);

			String url = newsArticle.getMedia().get(0).getSizes().get(1)
					.getUrl();
			url = URLUtil.urlEncodeFilename(url);

			UrlImageViewHelper.setUrlDrawable(image, url);

			title.setText(newsArticle.getTitle());
			excerpt.setText(newsArticle.getExcerptDisplay());

			view.setTag(R.id.key_article_url, newsArticle.getPermalink());
		}

		return view;
	}
}
