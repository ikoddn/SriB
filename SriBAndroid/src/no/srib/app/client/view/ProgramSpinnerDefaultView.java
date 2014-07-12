package no.srib.app.client.view;

import no.srib.app.client.R;
import android.content.Context;
import android.util.AttributeSet;

public class ProgramSpinnerDefaultView extends ProgramSpinnerView {

	private static final int LAYOUT_ID = R.layout.spinneritem_podcast_default;

	public ProgramSpinnerDefaultView(final Context context) {
		super(context);
		init(context, LAYOUT_ID);
	}

	public ProgramSpinnerDefaultView(final Context context,
			final AttributeSet attrs) {
		super(context, attrs);
		init(context, LAYOUT_ID);
	}
}
