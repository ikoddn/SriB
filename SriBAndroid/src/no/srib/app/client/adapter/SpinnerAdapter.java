package no.srib.app.client.adapter;

import java.util.ArrayList;
import java.util.List;

import no.srib.app.client.R;
import no.srib.app.client.model.ProgramName;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SpinnerAdapter extends BaseAdapter {

	private List<ProgramName> programList = new ArrayList<ProgramName>();
	private LayoutInflater inflater;
	private Context context;

	public SpinnerAdapter(Context context) {

		this.context = context;
		inflater = LayoutInflater.from(this.context);

	}

	public void setList(List<ProgramName> list) {
		this.programList = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (programList == null) {
			return 0;
		}

		return programList.size();
	}

	@Override
	public ProgramName getItem(int position) {
		if (programList == null) {
			return null;
		}

		return programList.get(position);
	}

	@Override
	public long getItemId(int position) {
		if (programList == null) {
			return 0;
		}
		return programList.get(position).getDefnr();
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
		ProgramName prog = programList.get(position);
		String name = prog.getName();
		if (name != null) {
			Spanned safeName = Html.fromHtml(name);
			text.setText(safeName);
		}

		return view;
	}

}
