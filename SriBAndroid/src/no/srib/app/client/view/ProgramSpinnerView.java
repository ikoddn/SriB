package no.srib.app.client.view;

import no.srib.app.client.R;
import no.srib.app.client.util.FontFactory;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

public abstract class ProgramSpinnerView extends LinearLayout {

	private static final int FONT_ID = R.string.font_clairehandbold;

	@InjectView(R.id.textView_programItem) TextView textView;

	public ProgramSpinnerView(final Context context) {
		super(context);
	}

	public ProgramSpinnerView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	protected void init(final Context context, final int layoutId) {
		LayoutInflater.from(context).inflate(layoutId, this, true);
		ButterKnife.inject(this);

		Typeface font = FontFactory.INSTANCE.getFont(context, FONT_ID);
		textView.setTypeface(font);
	}

	public void showText(final String text) {
		textView.setText(text);
	}
}
