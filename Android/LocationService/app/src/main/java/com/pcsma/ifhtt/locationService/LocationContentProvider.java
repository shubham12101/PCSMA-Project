package com.pcsma.ifhtt.locationService;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class LocationContentProvider extends ContentProvider {
	
	private DatabaseHelper database;
	
	@SuppressWarnings("unused")
	private static final String TAG = "LocContProvider";

	private static final UriMatcher pullURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	private static final int GET_LAST_RECORD = 100;
	private static final String AUTHORITY = "com.snmp.locationservice.LocationContentProvider";

	 private static final String BASE_PATH = "mylocationsTable";
	 public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

	  	
	static {
		pullURIMatcher.addURI(AUTHORITY, BASE_PATH, GET_LAST_RECORD);
	  }
	
	@Override
	public boolean onCreate() {
		database = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,	String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder mainQuery = new SQLiteQueryBuilder();
		//SQLiteQueryBuilder innerQuery = new SQLiteQueryBuilder();
		
		 mainQuery.setTables(DatabaseHelper.TABLE_NAME);
		 //innerQuery.setTables(DatabaseHelper.TABLE_NAME);
		 //String innerColumn[] = {"MAX(_id)"}; 
		 int uriType = pullURIMatcher.match(uri);
		 
		 switch (uriType) {
		 	case GET_LAST_RECORD:
		 		//mainQuery.buildQuery(null, null, null, null, "DESC", "1");
		 		
			    
		 		//innerQuery.buildQuery(innerColumn, null, null, null, null, null);
		 		//mainQuery.buildQuery(null, DatabaseHelper.COLUMN_ID + "=" + innerQuery, null, null, null, null);
		      break;
		    default:
		      throw new IllegalArgumentException("Unknown URI: " + uri);
		    }
		 
		///////////////////////////////////////////////////
		 SQLiteDatabase db = database.getReadableDatabase();
		 Cursor cursor = mainQuery.query(db, null, selection, selectionArgs, null, null, DatabaseHelper.COLUMN_ID + " DESC", "1");
		 return cursor;
		////////////////////////////////////////////////////
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}

}
