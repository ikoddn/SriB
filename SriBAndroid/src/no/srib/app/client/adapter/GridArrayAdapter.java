package no.srib.app.client.adapter;

import java.util.ArrayList;
import java.util.List;

import no.srib.app.client.R;
import no.srib.app.client.model.Podcast;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

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
		Podcast podcast = podcastList.get(position);
		TextView programNameTextView = (TextView) convertView.findViewById(R.id.label_gridViewItem_programname);
		String programName = podcast.getProgram();
		if(programName != null){
			Spanned safeText = Html.fromHtml(programName);
			programNameTextView.setText(safeText);
		}else{
			programNameTextView.setText(programName);
		}
		Log.i("GetArrayAdapter _ getView", programName + " " + podcast.getProgramId() );
		
		
		TextView programNameDate = (TextView) convertView.findViewById(R.id.label_gridViewItem_date);
		int date = podcast.getCreatedate();
		Typeface myTypeface = Typeface.createFromAsset(convertView.getContext().getAssets(), "fonts/ROBOTS.ttf");
		programNameDate.setTypeface(myTypeface);
		programNameDate.setText(String.valueOf(date));
		
		ImageView image = (ImageView) convertView.findViewById(R.id.imageView1);
		String url = podcast.getImageUrl();
		UrlImageViewHelper.setUrlDrawable(image, url, R.drawable.frank);
		//UrlImageViewHelper.setUrlDrawable(image, url);
		
		//ImageButton imageButton = (ImageButton) convertView.findViewById(R.id.imageButton1);
		//Drawable draw = convertView.getResources().getDrawable(R.drawable.down);
		//imageButton.setImageDrawable(draw);
		
		
		return convertView;
	}
	
}
