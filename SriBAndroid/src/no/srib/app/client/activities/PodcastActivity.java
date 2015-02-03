package no.srib.app.client.activities;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
import no.srib.app.client.service.PodcastManager;
import no.srib.app.client.service.audioplayer.AudioPlayerService;
import no.srib.app.client.util.DeviceUtil;
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
    @InjectView(R.id.buttonPodcastPause)            ImageView buttonPause;
//    @InjectView(R.id.Microphone)                    ImageView Microphone;
//    @InjectView(R.id.Articles)                      ImageView Articles;
    @InjectView(R.id.buttonBack)                    ImageView buttonBack;

	private Drawable defaultPodcastImage;
	private Podcast podcast;
	private PodcastManager podcastManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		LayoutInflater inflater = LayoutInflater.from(this);
		View rootView = inflater.inflate(R.layout.screen_podcast_info, null,
				false);

		ButterKnife.inject(this, rootView);
		setContentView(rootView);

		podcast = (Podcast) getIntent().getExtras().get("podcast");

		if(podcast == null)
			return;

		int viewWidth = DeviceUtil.getDisplayProperties(this).width;

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

			podcastManager = PodcastManager.getInstance();
			registerDatachangeListener();
            buttonPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.setPodcastAudioSource(podcast);
                    buttonPlay.setVisibility(View.INVISIBLE);
                    buttonPause.setVisibility(View.VISIBLE);
                }
            });

            buttonBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            buttonPause.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Logger.d("Logger inn");
                    AudioPlayerService.getService().pause();
                    buttonPlay.setVisibility(View.VISIBLE);
                    buttonPause.setVisibility(View.INVISIBLE);
                } });



            buttonDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    podcastManager.downloadPodcast(podcast);
                }
            });

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    podcastManager.deletePodcast(podcast);
                }
            });

            buttonStopDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    podcastManager.cancelDownload(podcast);
                }
            });
        }
    }

	private void registerDatachangeListener() {
		PodcastManager.PodcastLocalInfo podcastInfo = podcastManager
				.getLocalInfo(podcast);

		podcastInfo.setDatachangeListener(new PodcastManager.OnDataChangeListener() {
			@Override
			public void dataChanged(PodcastManager.PodcastLocalInfo podcastInfo) {
				updateUI(podcastInfo);
			}
		});

		updateUI(podcastInfo);
	}

	private void unregisterDatachangeListener() {
		PodcastManager.PodcastLocalInfo podcastInfo = podcastManager
				.getLocalInfo(podcast);

		// update ui before releasing the datachange listener
		updateUI(podcastInfo);
		podcastInfo.removeDatachangeListener();
	}

	@Override
	public void onStop() {
		super.onStop();
		Logger.d("onStop called");
		unregisterDatachangeListener();
	}

	@Override
	public void onRestart() {
		super.onRestart();
		Logger.d("onRestart called");
		registerDatachangeListener();
	}


	public void updateUI(final PodcastManager.PodcastLocalInfo podcastInfo) {
		Logger.i("updating UI");

		UI.runOnUI(new Runnable() {
			@Override
			public void run() {
				// if it is queued, queued message should show
				float downloadedPercent = podcastInfo.getDownloadedPercent();

				if(podcastInfo.isQueued()) {
					if(queText.getVisibility() != View.VISIBLE) {
						queText.setVisibility(View.VISIBLE);
						Logger.i("Showing queue text");
					}

					if(buttonDownload.getVisibility() != View.GONE) {
						Logger.i("Hiding download button");
						buttonDownload.setVisibility(View.GONE);
					}

					if(buttonStopDownload.getVisibility() != View.VISIBLE) {
						Logger.i("Showing cancel button");
						buttonStopDownload.setVisibility(View.VISIBLE);
					}
				}
				// else queued message should be hidden
				else if(!podcastInfo.isQueued() && queText.getVisibility() != View.GONE) {
					Logger.i("Hiding queue text");
					queText.setVisibility(View.GONE);
				}

				// if downloaded percent is more than 0 you should show; delete button and progress bar
				if(downloadedPercent > 0) {
					if(buttonDelete.getVisibility() != View.VISIBLE) {
						Logger.i("Showing delete button");
						buttonDelete.setVisibility(View.VISIBLE);
					}

					if(progressBar.getVisibility() != View.VISIBLE) {
						Logger.i("Showing progress bar");
						progressBar.setVisibility(View.VISIBLE);
					}
				}
				// else; hide delete button and progress bar
				else {
					if(progressBar.getVisibility() != View.GONE) {
						Logger.i("Hiding progress bar");
						progressBar.setVisibility(View.GONE);
					}

					if(buttonDelete.getVisibility() != View.GONE) {
						Logger.i("Hiding delete button");
						buttonDelete.setVisibility(View.GONE);
					}
				}
				// if fully downloaded hide download button
				if(downloadedPercent >= 100 && buttonDownload.getVisibility() != View.GONE) {
					Logger.i("Hiding download button");
					buttonDownload.setVisibility(View.GONE);
				}

				// if podcast is downloading show cancel button and hide download button
				if(podcastInfo.isDownloading()) {
					if(buttonDownload.getVisibility() != View.GONE) {
						Logger.i("Hiding download button");
						buttonDownload.setVisibility(View.GONE);
					}

					if(buttonStopDownload.getVisibility() != View.VISIBLE) {
						Logger.i("Showing cancel button");
						buttonStopDownload.setVisibility(View.VISIBLE);
					}
				}
				//else hide cancel button and show download button
				else if(!podcastInfo.isQueued()){
					if(buttonDownload.getVisibility() != View.VISIBLE
							&& downloadedPercent < 100) {
						Logger.i("Showing download button");
						buttonDownload.setVisibility(View.VISIBLE);
					}

					if(buttonStopDownload.getVisibility() != View.GONE) {
						Logger.i("Hiding cancel button");
						buttonStopDownload.setVisibility(View.GONE);
					}
				}

				Logger.i("Setting progress bar to: " + podcastInfo.getDownloadedPercent());
				progressBar.setProgress((int) podcastInfo.getDownloadedPercent());
			}
		});
	}
}
