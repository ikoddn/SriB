package no.srib.sribapp.util;
import java.io.IOException;
import java.net.URL;



import no.srib.fragment.LiveRadioFragment;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html.ImageGetter;
import android.util.Log;

public class GetNewsTask extends AsyncTask<Void, LiveRadioFragment, ImageGetter>{

	private LiveRadioFragment a;
	
	public GetNewsTask(LiveRadioFragment a) {
		this.a = a;
	}

	protected void onPostExecute(ImageGetter result) {
		a.addContentToNewsView(result);
		

		super.onPostExecute(result);
	}
	
	public ImageGetter getImageHTML() {
		ImageGetter imageGetter = new ImageGetter() {
			public Drawable getDrawable(String source) {
				try {
					Drawable drawable = Drawable.createFromStream(new URL(source).openStream(), "src name");
					drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
					return drawable;
				} catch(IOException exception) {
					Log.v("IOException",exception.getMessage());
					return null;
				}
			}
		};
		return imageGetter;
	}

	@Override
	protected ImageGetter doInBackground(Void... params) {
		ImageGetter image = getImageHTML();
		return image;
	}



}
