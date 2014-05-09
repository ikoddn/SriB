package no.srib.app.client.database;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class PodcastDatabase extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 2;
	private static final String DICTIONARY_TABLE_NAME = "podcast";
	private static final String PODCAST_TABLE_CREATE = "";
	
	
	
	public PodcastDatabase(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
