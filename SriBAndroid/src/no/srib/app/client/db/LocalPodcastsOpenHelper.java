package no.srib.app.client.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.jetbrains.annotations.NotNull;

/**
 * Created by morits on 12/10/14.
 */
public class LocalPodcastsOpenHelper extends SQLiteOpenHelper {
	static private final String DB_NAME = "podcasts.db";
	static private final int DB_VERSION = 2;

	public LocalPodcastsOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(@NotNull SQLiteDatabase database) {
		database.execSQL("CREATE TABLE `podcasts` (" +
				"`id` TEXT PRIMARY KEY," +
				"`podcast` TEXT NOT NULL, " +
				"`size` INTEGER NOT NULL, " +
				"`created` DATETIME DEFAULT CURRENT_TIMESTAMP)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(oldVersion <= 1) {
			// SORRY you will loose all your podcasts
			db.execSQL("DROP TABLE `podcasts`");
			onCreate(db);
		}
	}
}
