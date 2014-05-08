package no.srib.app.client.adapter;

import java.util.List;

import no.srib.R;
import no.srib.app.client.model.NewsArticle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class ArticleListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<NewsArticle> list;

	public ArticleListAdapter(LayoutInflater inflater) {
		this.inflater = inflater;
		list = null;
	}

	public void setList(List<NewsArticle> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public NewsArticle getItem(int position) {
		return list == null ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return list == null ? 0 : list.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.listitem_article, null);

		ImageView image = (ImageView) convertView
				.findViewById(R.id.imageview_article);
		TextView title = (TextView) convertView
				.findViewById(R.id.textview_article_title);
		TextView excerpt = (TextView) convertView
				.findViewById(R.id.textview_article_excerpt);

		NewsArticle newsArticle = list.get(position);

		String url = newsArticle.getMedia().get(0).getSizes().get(1).getUrl();
		UrlImageViewHelper.setUrlDrawable(image, url);

		title.setText(newsArticle.getTitle());
		excerpt.setText(newsArticle.getExcerptDisplay());

		return convertView;
	}
}
