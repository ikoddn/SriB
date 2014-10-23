package no.srib.app.client.db;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.srib.app.client.R;
import no.srib.app.client.model.Podcast;
import no.srib.app.client.util.Logger;
import no.srib.app.client.util.SerializationHelper;

/**
 * @author Jostein
 */
public class PodcastDataSource {

	@NotNull private final SQLiteDatabase database;
	@NotNull private final LocalPodcastsOpenHelper dbHelper;
	@NotNull private final Context context;

	public PodcastDataSource(@NotNull Context context) {
		this.context = context;
		dbHelper = new LocalPodcastsOpenHelper(context);
		database = dbHelper.getWritableDatabase();
	}

	public File getLocalDir() {
		return context.getExternalFilesDir("podcasts");
	}

	public String getNasUrl() {
		return context.getString(R.string.url_podcast_nas);
	}

//	public void open() throws SQLException {
//		dbHelper = new LocalPodcastsOpenHelper(context);
//		database = dbHelper.getWritableDatabase();
//	}

	public void close() {
		database.close();
		dbHelper.close();
	}

	public void addPodcast(Podcast podcast, long size) {
		database.execSQL("INSERT OR IGNORE INTO `podcasts` (`id`, `podcast`, `size`) VALUES (?, ?, ?);",
				new String[] {Integer.toString(podcast.getRefnr()),
						SerializationHelper.serialize(podcast),
						Long.toString(size)});
	}

	public void delete(Podcast podcast) {
		database.execSQL("DELETE FROM `podcasts` WHERE id LIKE ?;",
				new String[]{Integer.toString(podcast.getRefnr())});
	}

	public boolean exists(Podcast podcast) {
		Cursor cursor = database.rawQuery("SELECT TRUE FROM `podcasts` WHERE id LIKE ?",
				new String[]{Integer.toString(podcast.getRefnr())});

		boolean exists = false;
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			exists = true;
			cursor.moveToNext();
		}

		// Make sure to close the cursor
		cursor.close();

		return exists;
	}

	public long getFileSize(Podcast podcast) {
		Cursor cursor = database.rawQuery("SELECT `size` FROM `podcasts` WHERE id LIKE ? LIMIT 1",
				new String[]{Integer.toString(podcast.getRefnr())});

		cursor.moveToFirst();
		long size = -1;
		while (!cursor.isAfterLast()) {
			size = cursor.getLong(0);
			cursor.moveToNext();
		}

		// Make sure to close the cursor
		cursor.close();

		return size;
	}

	@NotNull
	public List<Podcast> getAllLocalPodcasts() {
		List<Podcast> values = new ArrayList<>();

		Cursor cursor = database.rawQuery("SELECT `podcast` FROM `podcasts` ORDER BY `created` DESC", new String[0]);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String podcast = cursor.getString(0);
			try {
				values.add((Podcast) SerializationHelper.deserialize(podcast));
			}
			catch (Exception e) {
				Logger.e("could not deserialize podcast");
				e.printStackTrace();
			}
			cursor.moveToNext();
		}

		// Make sure to close the cursor
		cursor.close();

		return values;
	}

	public Object getValue(String key) {
		Cursor cursor = database.rawQuery("SELECT `key`, `value` FROM `config` WHERE `key` LIKE ?", new String[]{key});
		cursor.moveToFirst();
		Object val = null;
		while (!cursor.isAfterLast()) {
//			String key = cursor.getString(0);
			val = cursor.getString(1);
//			try {
//				val = SerializationHelper.deserialize(str);
//			}
//			catch (Exception e) {
//				val = str;
//			}
			cursor.moveToNext();
		}

		// Make sure to close the cursor
		cursor.close();

		return val;
	}
}
