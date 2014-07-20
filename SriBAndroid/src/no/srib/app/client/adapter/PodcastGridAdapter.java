package no.srib.app.client.adapter;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import no.srib.app.client.model.Podcast;
import no.srib.app.client.util.ViewUtil;
import no.srib.app.client.view.PodcastView;

import org.apache.commons.lang3.time.FastDateFormat;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class PodcastGridAdapter extends ListBasedAdapter<Podcast> {

	private static final String DATE_IN_FORMAT = "yyyyMMdd";
	private static final String DATE_OUT_FORMAT = "dd-MM-yyyy";

	private final Context context;
	private final FastDateFormat dateInFormatter;
	private final FastDateFormat dateOutFormatter;

	public PodcastGridAdapter(final Context context) {
		this.context = context;

		Locale locale = Locale.getDefault();
		dateInFormatter = FastDateFormat.getInstance(DATE_IN_FORMAT, locale);
		dateOutFormatter = FastDateFormat.getInstance(DATE_OUT_FORMAT, locale);
	}

	@Override
	public long getItemId(final int position) {
		return getItem(position).getRefnr();
	}

	@Override
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {

		PodcastView view;

		if (convertView == null) {
			GridView gridView = (GridView) parent;
			int columnWidth = ViewUtil.getColumnWidth(gridView);

			view = new PodcastView(context);
			view.init(columnWidth);
		} else {
			view = (PodcastView) convertView;
		}

		Podcast podcast = getItem(position);

		String formattedDate;
		try {
			Date date = dateInFormatter.parse(String.valueOf(podcast
					.getCreatedate()));
			formattedDate = dateOutFormatter.format(date);
		} catch (ParseException e) {
			formattedDate = "";
		}

		view.showPodcast(podcast, formattedDate);
		view.setTag(podcast);

		return view;
	}
}
