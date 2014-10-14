package no.srib.app.client.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import no.srib.app.client.R;
import no.srib.app.client.db.PodcastDataSource;
import no.srib.app.client.model.Podcast;

/**
 * Created by morits on 12/10/14.
 */
//public class PodcastDownloader {
//	static private Context context;
//
//	static public void Download(Context context, Podcast podcast) {
//		PodcastDownloader.context = context;
////		new DownloadPodcastTask().execute(podcast);
//	}
//
//
//}
