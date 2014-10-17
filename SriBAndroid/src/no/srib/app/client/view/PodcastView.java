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
import java.util.logging.Logger;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.support.v4.app.ActivityCompat.startActivity;

public class PodcastView extends LinearLayout {

    private static final int DEFAULT_IMAGE_ID = R.drawable.podcast_default_art;
    private static final int FONT_ID = R.string.font_clairehandbold;

    @InjectView(R.id.imageView_podcastItem_art)
    ImageView imageView;
    @InjectView(R.id.textView_podcastItem_date)
    TextView dateTextView;
    @InjectView(R.id.textView_podcastItem_programname)
    TextView programNameTextView;
    @InjectView(R.id.download_podcast)
    ImageView button;
    private String DownloadUrl = "http://podcast.srib.no:8080/podcast";
    private String fileName = "SRIB.podcast";
    private int viewWidth;
    private Drawable defaultImage;
    //DownloadDatabase(DownloadUrl,fileName);

    private class Background extends AsyncTask<URL, Void, Void> {

        @Override
        protected Void doInBackground(URL... urls) {
            Log.i("doInBackground", "does");
            try {
                File root = android.os.Environment.getExternalStorageDirectory();
                File dir = new File(root.getAbsolutePath() + "/mnt/sdcard/SRIB/podcast");

                if (dir.exists() == false) {
                    dir.mkdirs();
                }
                URL url = new URL("http://podcast.srib.no:8080/podcast.txt");
                File file = new File(dir, fileName);

                long startTime = System.currentTimeMillis();
                Log.i("DownloadManager", "URL" + url);
                Log.i("DownloadManager", "File name" + fileName);
                URLConnection urlConnect = url.openConnection();
                //urlconnect.setReadTimeout(TIMEOUT_CONNECTION);
                InputStream input = urlConnect.getInputStream();
                BufferedInputStream buffinst = new BufferedInputStream(input);

                ByteArrayBuffer baf = new ByteArrayBuffer(5000);
                int current = 0;
                while ((current = buffinst.read()) != -1) {
                    baf.append((byte) current);
                }


                FileOutputStream fileOut = new FileOutputStream(file);
                fileOut.write(baf.toByteArray());
                fileOut.flush();
                fileOut.close();
                buffinst.close();
                Log.i("DownloadManager", "download is ready in" + (System.currentTimeMillis() - startTime) / 1000 + "seconds");
                int index = fileName.lastIndexOf('.');
                if (index >= 0) {
                    fileName = fileName.substring(0, index);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            //Log.i("DownloadManager", "Error" + e);
            //e.printStackTrace();
            Log.i("help" , "help");
            return null;

        }
    }

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
            button.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    ((Podcast)getTag()).download();


                }

            });
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
    }


