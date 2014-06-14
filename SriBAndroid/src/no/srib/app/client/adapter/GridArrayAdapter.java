package no.srib.app.client.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import no.srib.app.client.R;
import no.srib.app.client.model.Podcast;
import no.srib.app.client.util.TimeUtil;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class GridArrayAdapter extends BaseListAdapter<Podcast> {

	private LayoutInflater inflater;

	public GridArrayAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getRefnr();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;

		if (view == null) {
			view = inflater.inflate(R.layout.podcast_grid_item, null);
		}

		Typeface appFont = Typeface.createFromAsset(view.getContext()
				.getAssets(), "fonts/clairehandbold.ttf");
		Podcast podcast = getItem(position);
		TextView programNameTextView = (TextView) view
				.findViewById(R.id.label_gridViewItem_programname);
		programNameTextView.setTypeface(appFont);
		String programName = podcast.getProgram();

		if (programName != null) {
			Spanned safeText = Html.fromHtml(programName);
			programNameTextView.setText(safeText);
		} else {
			programNameTextView.setText("");
		}
		TextView programNameDate = (TextView) view
				.findViewById(R.id.label_gridViewItem_date);
		programNameDate.setTypeface(appFont);
		int date = podcast.getCreatedate();
		Calendar cal = TimeUtil.parseIntDate(date);
		String format = "dd-MM-yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		programNameDate.setText(sdf.format(cal.getTime()));

		final ImageView image = (ImageView) view.findViewById(R.id.imageView1);
		final String url = podcast.getImageUrl();

		UrlImageViewHelper.setUrlDrawable(image, url, R.drawable.frank);

		view.setTag(R.id.podcast_url, podcast.getFilename());

		view.setTag(R.id.podcast_name, podcast.getProgram());
		view.setTag(R.id.podcast_date, podcast.getCreatedate());

		return view;
	}
}
