package com.ilves.gbsgarn;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GbsNotesDbHelper extends SQLiteOpenHelper {

	public static final String	TABLE_NOTES			= "notes";
	public static final String	COLUMN_ID			= "_id";
	public static final String	COLUMN_NOTE			= "note";

	private static final String	DATABASE_NAME		= "commments.db";
	private static final int	DATABASE_VERSION	= 1;

	// Database creation sql statement
	private static final String	DATABASE_CREATE		= "create table "
															+ TABLE_NOTES
															+ "("
															+ COLUMN_ID
															+ " integer primary key autoincrement, "
															+ COLUMN_NOTE
															+ " text not null);";

	public GbsNotesDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public GbsNotesDbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(GbsNotesDbHelper.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
		onCreate(db);
	}

}
