package no.srib.app.client.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridArrayAdapter extends BaseAdapter{

	List<Podcast> podcastList = new ArrayList<Podcast>();
	LayoutInflater inflater;
	Context context;

	
	public GridArrayAdapter( Context context){
		
		this.context = context;
		inflater = LayoutInflater.from(context);
	}
	
	public void setList(List<Podcast> list){
		this.podcastList = list;
	}
	
	@Override
	public int getCount() {
	
		return podcastList.size();
	}

	@Override
	public Podcast getItem(int position) {
		
		return podcastList.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return podcastList.get(position).getRefnr();
	}
	
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		
		convertView = inflater.inflate(R.layout.podcast_grid_item, null);
		Typeface appFont = Typeface.createFromAsset(convertView.getContext().getAssets(), "fonts/clairehandbold.ttf");
		Podcast podcast = podcastList.get(position);
		TextView programNameTextView = (TextView) convertView.findViewById(R.id.label_gridViewItem_programname);
		programNameTextView.setTypeface(appFont);
		String programName = podcast.getProgram();
		
		if(programName != null){
			Spanned safeText = Html.fromHtml(programName);
			programNameTextView.setText(safeText);
		}else{
			programNameTextView.setText(programName);
		}
		TextView programNameDate = (TextView) convertView.findViewById(R.id.label_gridViewItem_date);
		programNameDate.setTypeface(appFont);
		int date = podcast.getCreatedate();
		Calendar cal = TimeUtil.parseIntDate(date);
		String format = "dd-MM-yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		programNameDate.setText(sdf.format(cal.getTime()));
		
		final ImageView image = (ImageView) convertView.findViewById(R.id.imageView1);
		final String url = podcast.getImageUrl();
	
		        	UrlImageViewHelper.setUrlDrawable(image, url, R.drawable.frank);
		

		
		
		
		
		convertView.setTag(R.id.podcast_url, podcast.getFilename());
		
		
		return convertView;
	}
	
}


