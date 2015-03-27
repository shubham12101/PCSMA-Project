package com.pcsma.ifhtt.locationService;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

 public class DatabaseHelper extends SQLiteOpenHelper {
	 
	 static final String TAG = "SNMP_DB";

		final private static String DATABASE_NAME = "snmp_db";
		final private static Integer DATABASE_VERSION = 1;

		final static String TABLE_NAME = "mylocations";
		final static String COLUMN_UID = "uid";
		final static String COLUMN_TIME = "time";
		final static String COLUMN_AP_ID = "ap_id";
		final static String COLUMN_LABEL = "label";
		final static String COLUMN_BUILDING = "building";
		final static String COLUMN_FLOOR = "floor";
		final static String COLUMN_WING = "wing";
		final static String COLUMN_ROOM = "room";
		final static String COLUMN_ID = "_id";
		final static String[] columns = { COLUMN_ID, COLUMN_UID, COLUMN_TIME, COLUMN_AP_ID, COLUMN_LABEL, COLUMN_BUILDING, COLUMN_FLOOR, COLUMN_WING, COLUMN_ROOM };

		final private static String DATABASE_CREATE =
		"CREATE TABLE mylocations (" + 
		COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
		COLUMN_UID + " INTEGER, " +  
		COLUMN_TIME + " TEXT, " +
		COLUMN_AP_ID + " TEXT, " +
		COLUMN_LABEL + " TEXT, " +
		COLUMN_BUILDING + " TEXT, " +
		COLUMN_FLOOR + " TEXT, " +
		COLUMN_WING + " TEXT, " +
		COLUMN_ROOM + " TEXT)";
		
		//final private static String DROP_TABLE_CMD = "DROP TABLE IF EXISTS " + TABLE_NAME;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		try {
			db.execSQL(DATABASE_CREATE);
			Log.i(TAG, "Database created");
		
		} catch (Exception e) {
			Log.i(TAG, "Error while creating database");
			e.printStackTrace();
		}

	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading databse fron version " + oldVersion + " to "
				+ newVersion + ", which will destroy all data");
		
		db.execSQL("DROP TABLE IF EXISTS mylocations");
		onCreate(db);

	}
}
