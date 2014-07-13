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

public class ProgramSpinnerView extends LinearLayout {

	private static final int FONT_ID = R.string.font_clairehandbold;

	@InjectView(R.id.textView_programItem) TextView textView;

	public ProgramSpinnerView(final Context context) {
		super(context);
	}

	public ProgramSpinnerView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	public void init(final int layoutId) {
		LayoutInflater.from(getContext()).inflate(layoutId, this, true);
		ButterKnife.inject(this);

		Typeface font = FontFactory.INSTANCE.getFont(getContext(), FONT_ID);
		textView.setTypeface(font);
	}

	public void showText(final String text) {
		textView.setText(text);
	}
}
