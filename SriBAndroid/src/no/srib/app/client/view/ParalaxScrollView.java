package no.srib.app.client.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import no.srib.app.client.R;
import no.srib.app.client.util.Logger;

public class ParalaxScrollView extends ScrollView {
	private float factor;
	private float contentPaddingFactor;
	public ParalaxScrollView(Context context) {
		super(context);
	}

	public ParalaxScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundImage(context, attrs);
	}

	public ParalaxScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setBackgroundImage(context, attrs);
	}

	private void setBackgroundImage(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ParalaxScrollView);

		paralaxImageId = a.getResourceId(R.styleable.ParalaxScrollView_paralaxBackgroundImage, -1);
		factor = a.getFloat(R.styleable.ParalaxScrollView_paralaxFactor, 1);
		contentPaddingFactor = a.getFloat(R.styleable.ParalaxScrollView_paddingFactor, 1);
		a.recycle();
	}

	private ImageView paralaxImage;
	private int paralaxImageId;
	private void setParalaxBackgroundImage(int backgroundImageViewId) {
		Logger.d("bgImage: " + backgroundImageViewId);
		if(backgroundImageViewId != -1) {
			ViewGroup tmp = this;
			boolean c = true;

			while(true) {
				try {
					tmp = (ViewGroup) tmp.getParent();
				}
				catch (ClassCastException e) {
					break;
				}
				if((paralaxImage = (ImageView) tmp.findViewById(backgroundImageViewId)) != null)
					break;
			}

//			if(root != null)
//				paralaxImage = (ImageView) root.findViewById(backgroundImageViewId);

			if(paralaxImage != null && Build.VERSION.SDK_INT < 11)
				params = (RelativeLayout.LayoutParams) paralaxImage.getLayoutParams();
			RelativeLayout content = (RelativeLayout) findViewById(R.id.contentContainer);

			DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
			int viewWidth = metrics.widthPixels;

			int paddingTop = (int)(viewWidth / contentPaddingFactor);
			Logger.d("use padding factor: " + (viewWidth / (float)content.getPaddingTop()));
			Logger.d("stored padding factor: " + contentPaddingFactor);
			Logger.d("paddingTop: " + content.getPaddingTop() + " width: " + viewWidth + " paddingTop: " + paddingTop);//1.41481481481481
			content.setPadding(content.getPaddingLeft(), paddingTop, content.getPaddingRight(), content.getPaddingBottom());
		}

	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		setParalaxBackgroundImage(paralaxImageId);
		// fix offsets and paddings
		//padding top = width * 1,41481481481481
//		197
//		270dp
	}

	private RelativeLayout.LayoutParams params;
	@Override
	protected void onScrollChanged(int horOrig, int vertOrig, int oldHorOrig, int oldVertOrig) {
		super.onScrollChanged(horOrig, vertOrig, oldHorOrig, oldVertOrig);
		if(vertOrig != oldVertOrig && paralaxImage != null) {
			int newTop = (int) (vertOrig * factor)*-1;
			if(Build.VERSION.SDK_INT < 11) {
				params.setMargins(0, newTop, 0, 0);
				paralaxImage.setLayoutParams(params);
			}
			else
				paralaxImage.setTop(newTop);

		}
	}
}
