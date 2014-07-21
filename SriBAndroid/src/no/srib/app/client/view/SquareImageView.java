package no.srib.app.client.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SquareImageView extends ImageView {

	private boolean usingHeight;

	public SquareImageView(final Context context) {
		super(context);
	}

	public SquareImageView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public SquareImageView(final Context context, final AttributeSet attrs,
			final int defStyle) {

		super(context, attrs, defStyle);
		init(attrs);
	}

	private void init(final AttributeSet attrs) {
		if (attrs != null) {
			String namespace = "http://schemas.android.com/apk/res-auto";
			usingHeight = attrs.getAttributeBooleanValue(namespace,
					"usingHeight", false);
		} else {
			usingHeight = false;
		}
	}

	@Override
	protected void onMeasure(final int widthMeasureSpec,
			final int heightMeasureSpec) {

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int size = usingHeight ? getMeasuredHeight() : getMeasuredWidth();
		setMeasuredDimension(size, size);
	}
}
