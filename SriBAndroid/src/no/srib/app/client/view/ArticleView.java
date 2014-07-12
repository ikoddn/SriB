package no.srib.app.client.view;

import java.util.List;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import no.srib.app.client.R;
import no.srib.app.client.model.Article;
import no.srib.app.client.model.ArticleImage;
import no.srib.app.client.model.ArticleMedia;
import no.srib.app.client.util.URLUtil;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ArticleView extends LinearLayout {

	@InjectView(R.id.imageview_articleitem) ImageView image;
	@InjectView(R.id.textview_articleitem_title) TextView title;
	@InjectView(R.id.textview_articleitem_excerpt) TextView excerpt;

	public ArticleView(final Context context) {
		super(context);
		init(context);
	}

	public ArticleView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(final Context context) {
		setOrientation(HORIZONTAL);

		LayoutInflater.from(context).inflate(R.layout.listitem_article, this,
				true);
		ButterKnife.inject(this);
	}

	public void showArticle(final Article article) {
		List<ArticleMedia> articleMedia = article.getMedia();

		if (!(articleMedia == null || articleMedia.isEmpty())) {
			String url = null;

			List<ArticleImage> articleImageSizes = articleMedia.get(0)
					.getSizes();

			if (articleImageSizes != null) {
				if (articleImageSizes.size() > 1) {
					url = articleImageSizes.get(1).getUrl();
				} else if (!articleImageSizes.isEmpty()) {
					url = articleImageSizes.get(0).getUrl();
				}

				if (url != null) {
					url = URLUtil.urlEncodeFilename(url);
					UrlImageViewHelper.setUrlDrawable(image, url);
				}
			}
		}

		title.setText(article.getTitle());
		excerpt.setText(article.getExcerptDisplay());
	}
}
