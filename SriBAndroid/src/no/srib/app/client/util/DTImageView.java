package no.srib.app.client.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class DTImageView extends View {

	private Bitmap bitmap;

	public DTImageView(Context context) {
		super(context);
		bitmap = null;
	}

	public DTImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		bitmap = null;
	}

	public DTImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		bitmap = null;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public void cleanup() {
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (bitmap != null) {
			canvas.drawBitmap(bitmap, 0, 0, null);
		}
	}
}
