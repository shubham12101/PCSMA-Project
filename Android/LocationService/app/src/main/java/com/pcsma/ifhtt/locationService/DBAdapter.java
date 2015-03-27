package com.pcsma.ifhtt.locationService;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBAdapter {
	
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
	
	final Context context;
	DatabaseHelper DBHelper;
	SQLiteDatabase db;
	
	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);

	}
	 

	// opens the database
	public DBAdapter open() {
		db = DBHelper.getWritableDatabase();
		return this;	
	}
	
	// closes the database
	public void close(){
		DBHelper.close();
	}
	
	// insert an entry into the database
	public long insertEnrty(int uid, String time, String ap_id, String label, String building, String floor, String wing, String room){
		ContentValues insertVal = new ContentValues();
		insertVal.put(COLUMN_UID, uid);
		insertVal.put(COLUMN_TIME, time);
		insertVal.put(COLUMN_AP_ID, ap_id);
		insertVal.put(COLUMN_LABEL, label);
		insertVal.put(COLUMN_BUILDING, building);
		insertVal.put(COLUMN_FLOOR, floor);
		insertVal.put(COLUMN_WING, wing);
		insertVal.put(COLUMN_ROOM, room);
		return db.insert(TABLE_NAME, null, insertVal);
	}
	
	// delete a particular contact
	public boolean deleteEntry(long rowId){
		return db.delete(TABLE_NAME, COLUMN_ID + "=" + rowId , null) > 0;
	}
	
	public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
 
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
        	
        	do {
        		Contact contact = new Contact();
        		contact.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))));
                contact.setUID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_UID))));
                contact.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_TIME)));
                contact.setLabel(cursor.getString(cursor.getColumnIndex(COLUMN_LABEL)));
                contact.setAP_ID(cursor.getString(cursor.getColumnIndex(COLUMN_AP_ID)));
                
                contact.setBuilding(cursor.getString(cursor.getColumnIndex(COLUMN_BUILDING)));
                
                contact.setFloor(cursor.getString(cursor.getColumnIndex(COLUMN_FLOOR)));
                contact.setWing(cursor.getString(cursor.getColumnIndex(COLUMN_WING)));
                
                contact.setRoom(cursor.getString(cursor.getColumnIndex(COLUMN_ROOM)));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
 
        // return contact list
        return contactList;
    }


}
