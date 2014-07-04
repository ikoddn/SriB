package no.srib.app.client.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import no.srib.app.client.R;
import no.srib.app.client.model.Podcast;
import no.srib.app.client.util.TimeUtil;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class PodcastGridAdapter extends BaseListAdapter<Podcast> {

	private LayoutInflater inflater;
	private Typeface font;

	public PodcastGridAdapter(final LayoutInflater inflater, final Typeface font) {
		this.inflater = inflater;
		this.font = font;
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getRefnr();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;

		if (view == null) {
			view = inflater.inflate(R.layout.griditem_podcast, null);
		}

		Podcast podcast = getItem(position);
		TextView programNameTextView = (TextView) view
				.findViewById(R.id.textView_podcastItem_programname);
		programNameTextView.setTypeface(font);
		String programName = podcast.getProgram();

		if (programName != null) {
			programNameTextView.setText(programName);
		} else {
			programNameTextView.setText("");
		}

		TextView programNameDate = (TextView) view
				.findViewById(R.id.textView_podcastItem_date);
		programNameDate.setTypeface(font);
		int date = podcast.getCreatedate();
		Calendar cal = TimeUtil.parseIntDate(date);
		String format = "dd-MM-yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		programNameDate.setText(sdf.format(cal.getTime()));

		final ImageView image = (ImageView) view
				.findViewById(R.id.imageView_podcastItem_art);
		final String url = podcast.getImageUrl();

		UrlImageViewHelper.setUrlDrawable(image, url,
				R.drawable.podcast_default_art);

		view.setTag(R.id.podcast_url, podcast.getFilename());

		view.setTag(R.id.podcast_name, podcast.getProgram());
		view.setTag(R.id.podcast_date, podcast.getCreatedate());

		return view;
	}
}
