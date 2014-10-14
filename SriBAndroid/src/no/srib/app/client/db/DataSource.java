package no.srib.app.client.db;

import android.content.Context;

/**
 * @author Jostein
 */
public class DataSource {
	private static PodcastDataSource podcastDS;

	static public void inst(Context context) {
		podcastDS = new PodcastDataSource(context);
	}

	static public PodcastDataSource podcast() {
		return podcastDS;
	}
}
