package no.srib.app.client.view;

import no.srib.app.client.R;
import no.srib.app.client.imageloader.UrlImageLoaderProvider;
import no.srib.app.client.imageloader.UrlImageLoader;
import no.srib.app.client.model.Podcast;
import no.srib.app.client.util.FontFactory;
import no.srib.app.client.util.ImageUtil;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import butterknife.ButterKnife;
import butterknife.InjectView;
import no.srib.app.client.util.Logger;

import static android.support.v4.app.ActivityCompat.startActivity;

public class PodcastView extends LinearLayout {

    private static final int DEFAULT_IMAGE_ID = R.drawable.podcast_default_art;
    private static final int FONT_ID = R.string.font_clairehandbold;

    @InjectView(R.id.imageView_podcastItem_art) ImageView imageView;
    @InjectView(R.id.textView_podcastItem_date) TextView dateTextView;
    @InjectView(R.id.textView_podcastItem_programname) TextView programNameTextView;
    @InjectView(R.id.download_podcast) ImageView button;
	@InjectView(R.id.podcastDownloadProgressBar) ProgressBar progressBar;

    private String DownloadUrl = "http://podcast.srib.no:8080/podcast";
    private String fileName = "SRIB.podcast";
    private int viewWidth;
    private Drawable defaultImage;

        public PodcastView(final Context context) {

            super(context);
        }

        public PodcastView(final Context context, final AttributeSet attrs) {

            super(context, attrs);
        }

        public void init(final int viewWidth) {

            this.viewWidth = viewWidth;

            final Context context = getContext();


            setOrientation(VERTICAL);


            LayoutInflater.from(context).inflate(R.layout.griditem_podcast, this,
                    true);
            ButterKnife.inject(this);
            //button.setOnClickListener();

            Resources resources = context.getResources();
            defaultImage = resources.getDrawable(DEFAULT_IMAGE_ID);
            defaultImage = ImageUtil.resize(defaultImage, viewWidth, viewWidth,
                    resources);

            Typeface font = FontFactory.INSTANCE.getFont(context, FONT_ID);
            dateTextView.setTypeface(font);
            programNameTextView.setTypeface(font);
        }

        public void showPodcast(final Podcast podcast, final String formattedDate) {
            dateTextView.setText(formattedDate);
            programNameTextView.setText(podcast.getProgram());

			// TODO: if this is downloading... register with the podcast downloader somehow
//			if(podcast.isLocal() && !podcast.isCompletelyDownloaded()) {
			Logger.i("inital update progressbar for: " + podcast.getProgram());
			updateProgressBar(podcast.getPercentDownloaded());
			podcast.registerProgressBar(this);
//			}

			button.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    podcast.download();
                }

            });


			final String imageUrl = podcast.getImageUrl();

            if (imageUrl == null || imageUrl.trim().isEmpty()) {
                imageView.setImageDrawable(defaultImage);
            } else {
                UrlImageLoader urlImageLoader = UrlImageLoaderProvider.INSTANCE
                        .get();
                urlImageLoader.loadFromUrl(imageView, viewWidth, viewWidth,
                        imageUrl, defaultImage);
            }
        }

		public void updateProgressBar(int percent) {
			//TODO: update the progress bar
			Logger.i("updating progressbar: " + percent);
			progressBar.setProgress(percent);
		}
    }


