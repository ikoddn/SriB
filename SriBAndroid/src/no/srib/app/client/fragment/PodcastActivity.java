package no.srib.app.client.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import no.srib.app.client.MainActivity;
import no.srib.app.client.R;
import no.srib.app.client.imageloader.UrlImageLoader;
import no.srib.app.client.imageloader.UrlImageLoaderProvider;
import no.srib.app.client.model.Podcast;
import no.srib.app.client.util.ImageUtil;
import no.srib.app.client.util.Logger;
import no.srib.app.client.util.UI;
import no.srib.app.client.view.PodcastView;

public class PodcastActivity extends Activity {

	@InjectView(R.id.imageviewPodcastArt) 			ImageView imagePodcastArt;
	@InjectView(R.id.progressbarPodcast) 			ProgressBar progressBar;
	@InjectView(R.id.buttonPodcastPlay) 			ImageView buttonPlay;
	@InjectView(R.id.buttonPodcastDownload) 		ImageView buttonDownload;
	@InjectView(R.id.buttonPodcastStopDownload) 	ImageView buttonStopDownload;
	@InjectView(R.id.buttonPodcastDelete) 			ImageView buttonDelete;
	@InjectView(R.id.textviewPodcastName) 			TextView textPodcastName;
	@InjectView(R.id.textviewPodcastDescription) 	TextView textPodcastDescription;
	@InjectView(R.id.queText) 						TextView queText;

	private Drawable defaultPodcastImage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		LayoutInflater inflater = LayoutInflater.from(this);
		View rootView = inflater.inflate(R.layout.screen_podcast_info, null,
				false);

		ButterKnife.inject(this, rootView);
		setContentView(rootView);

		final Podcast podcast = MainActivity.clickedPodcast;
		if(podcast == null)
			return;

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		int viewWidth;

		if(Build.VERSION.SDK_INT < 13)
			viewWidth = display.getWidth();
		else {
			display.getSize(size);
			viewWidth = size.x;
		}

		Resources resources = getResources();
		defaultPodcastImage = resources.getDrawable(PodcastView.DEFAULT_IMAGE_ID);
		defaultPodcastImage = ImageUtil.resize(defaultPodcastImage, viewWidth, viewWidth,
				resources);

		Logger.d("setting image width: " + viewWidth);
		String imageUrl = podcast.getImageUrl();
		if (podcast != null) {
			if (imageUrl == null || imageUrl.trim().isEmpty()) {
				imagePodcastArt.setImageDrawable(defaultPodcastImage);
			} else {
				UrlImageLoader urlImageLoader = UrlImageLoaderProvider.INSTANCE
						.get();
				urlImageLoader.loadFromUrl(imagePodcastArt, viewWidth, viewWidth,
						imageUrl, defaultPodcastImage);
			}

			textPodcastName.setText(podcast.getTitle());
			textPodcastDescription.setText(podcast.getRemark());

			updateProgressBar(podcast.getPercentDownloaded(), podcast);
			podcast.registerProgressBar(this);

			buttonPlay.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					MainActivity.setPodcastAudioSource(podcast);
				}
			});

			buttonDownload.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					podcast.download();
				}
			});

			buttonDelete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					podcast.deletePodcast();
				}
			});

			buttonStopDownload.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					podcast.pauseDownload();
				}
			});
		}
	}

	public void updateProgressBar(final float percent, final Podcast podcast) {
		//TODO: update the progress bar
		Logger.i("updating progressbar: " + percent);

		UI.runOnUI(new Runnable() {
			@Override
			public void run() {
				// it is downloading or fully downloaded
				if(podcast.isQueued()) {
					if(queText.getVisibility() != View.VISIBLE) {
						Logger.d("showing que text");
						queText.setVisibility(View.VISIBLE);
					}

					if(buttonDownload.getVisibility() != View.GONE) {
						Logger.d("hiding download");
						queText.setVisibility(View.GONE);
					}
					if(buttonStopDownload.getVisibility() != View.VISIBLE) {
						Logger.d("showing que text");
						queText.setVisibility(View.VISIBLE);
					}
				}
				if(percent > 0) {
					Logger.d("percent is more than 0");
					// show delete
					if(buttonDelete.getVisibility() != View.VISIBLE) {
						Logger.d("showing delete");
						buttonDelete.setVisibility(View.VISIBLE);
					}

					if(podcast.isDownloading() && queText.getVisibility() == View.VISIBLE) {
						Logger.d("hiding que text");
						queText.setVisibility(View.GONE);
					}

					// show stop downloading if downloading
					Logger.d("podcast.isDownloading: " + (podcast.isDownloading()? "true": "false"));
					if(podcast.isDownloading() && buttonStopDownload.getVisibility() != View.VISIBLE) {
						Logger.d("showing stop download");
						buttonStopDownload.setVisibility(View.VISIBLE);
					}
					else if(!podcast.isDownloading() && buttonStopDownload.getVisibility() != View.GONE) {
						Logger.d("hiding stop download");
						buttonStopDownload.setVisibility(View.GONE);

					}

					// hide download button if downloading
					if(podcast.isDownloading() && buttonDownload.getVisibility() != View.GONE) {
						Logger.d("hiding download button");
						buttonDownload.setVisibility(View.GONE);
					}
					else if(!podcast.isDownloading() && buttonDownload.getVisibility() != View.VISIBLE) {
						Logger.d("showing download button");
						buttonDownload.setVisibility(View.VISIBLE);
					}

					// if fully downloaded
					if(percent >= 100) {
						Logger.d("percent is 100");
						// hide download button
						if(buttonDownload.getVisibility() != View.GONE) {
							Logger.d("hiding download button");
							buttonDownload.setVisibility(View.GONE);
						}

						// hide stop download button
						if(buttonStopDownload.getVisibility() != View.GONE) {
							Logger.d("hiding stop download button");
							buttonStopDownload.setVisibility(View.GONE);
						}
					}

					if(progressBar.getVisibility() != View.VISIBLE) {
						Logger.d("showing progress bar");
						progressBar.setVisibility(View.VISIBLE);
					}
				}
				else if(percent <= 0) {
					Logger.d("percent is 0");
					// hide delete
					if(buttonDelete.getVisibility() != View.GONE) {
						Logger.d("hide delete");
						buttonDelete.setVisibility(View.GONE);
					}

					// show download button
					if(buttonDownload.getVisibility() != View.VISIBLE) {
						Logger.d("show download");
						buttonDownload.setVisibility(View.VISIBLE);
					}

					// hide stop download button
					if(buttonStopDownload.getVisibility() != View.GONE) {
						Logger.d("hide stop download");
						buttonStopDownload.setVisibility(View.GONE);
					}

					// hide progress bar
					if(progressBar.getVisibility() != View.GONE) {
						Logger.d("hide progressbar");
						progressBar.setVisibility(View.GONE);
					}
				}

				progressBar.setProgress((int) percent);
			}
		});
	}
}
