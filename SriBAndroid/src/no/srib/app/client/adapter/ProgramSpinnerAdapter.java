package no.srib.app.client.adapter;

import no.srib.app.client.R;
import no.srib.app.client.model.ProgramName;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProgramSpinnerAdapter extends BaseListAdapter<ProgramName> {

	private LayoutInflater inflater;

	public ProgramSpinnerAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getDefnr();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;

		if (view == null) {
			view = inflater.inflate(R.layout.podcastlist, null);
		}

		Typeface appFont = Typeface.createFromAsset(view.getContext()
				.getAssets(), "fonts/clairehandbold.ttf");
		TextView text = (TextView) view
				.findViewById(R.id.label_spinnerItem_name);
		text.setTypeface(appFont);
		ProgramName prog = getItem(position);
		String name = prog.getName();

		if (name != null) {
			text.setText(name);
		}

		return view;
	}
}
