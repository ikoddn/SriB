package no.srib.app.client.view;

import no.srib.app.client.R;
import butterknife.ButterKnife;
import butterknife.InjectView;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ArticleDividerView extends LinearLayout {

	private static final int[] DIVIDERS = { R.drawable.list_divider_1,
			R.drawable.list_divider_2, R.drawable.list_divider_3,
			R.drawable.list_divider_4, R.drawable.list_divider_5 };

	@InjectView(R.id.imageView_articleItem_divider) ImageView image;

	public ArticleDividerView(final Context context) {
		super(context);
		init(context);
	}

	public ArticleDividerView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(final Context context) {
		setOrientation(HORIZONTAL);

		LayoutInflater.from(context).inflate(R.layout.listitem_article_divider,
				this, true);
		ButterKnife.inject(this);
	}
	
	public void showDivider(final int number) {
		int divider = DIVIDERS[number % DIVIDERS.length];
		image.setImageResource(divider);
	}
}
