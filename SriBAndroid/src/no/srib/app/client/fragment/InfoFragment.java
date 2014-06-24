package no.srib.app.client.fragment;

import no.srib.app.client.R;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Configuration conf = getParentFragment().getResources()
				.getConfiguration();
		if (conf.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			return null;
		}

		View rootView = inflater.inflate(R.layout.fragment_info, container,
				false);

		TextView textView1 = (TextView) rootView
				.findViewById(R.id.textview_info1);

		textView1.setText(Html.fromHtml(getString(R.string.textView_info1)));

		ImageView image = (ImageView) rootView
				.findViewById(R.id.imageView_infofragment);
		image.setImageDrawable(getResources().getDrawable(R.drawable.appikon));

		TextView textView2 = (TextView) rootView
				.findViewById(R.id.textview_info2);

		textView2.setText(Html.fromHtml(getString(R.string.textView_info2)));

		return rootView;
	}
}
