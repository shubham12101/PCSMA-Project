package com.pcsma.ifhtt.mainApp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.pcsma.ifhtt.mainApp.ActionObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by vedantdasswain on 15/04/15.
 */
public class DbFunctions {

    public static String TAG = "DbFunctions";

    public static long insert(Context context, ActionObject ao) {
        // Gets the data repository in write mode
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.RecipeEntry.ACTION, ao.getAction());
        values.put(DatabaseContract.RecipeEntry.LOCATION, ao.getLocation());
        values.put(DatabaseContract.RecipeEntry.START_TIME, ao.getStartTime());
        values.put(DatabaseContract.RecipeEntry.END_TIME, ao.getEndTime());
        values.put(DatabaseContract.RecipeEntry.OPTION_1, ao.getOption1());
        values.put(DatabaseContract.RecipeEntry.OPTION_2, ao.getOption2());

        long newRowId = db.insert(
                DatabaseContract.RecipeEntry.TABLE_NAME,
                null,
                values);

        db.close();

        return newRowId;

    }

    public static ArrayList<ActionObject> read(Context context){
        ArrayList<ActionObject> recipes=new ArrayList<ActionObject>();

        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String query = "SELECT * FROM " + DatabaseContract.RecipeEntry.TABLE_NAME;

        Cursor c = db.rawQuery(
                query, null);

        if (c != null && c.moveToFirst()) {
            while (c.isAfterLast() == false) {
                ActionObject ao = new ActionObject(c.getString(1), c.getString(2), c.getString(3),
                        c.getString(4), c.getString(5), c.getString(6));
                recipes.add(ao);
                c.moveToNext();
            }
        }
        
        return recipes;
    }

    public static ArrayList<ActionObject> getActions(Context context,String location){
        Log.d(TAG,"entered");
        ArrayList<ActionObject> actions=new ArrayList<ActionObject>();

        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long currTime=System.currentTimeMillis();
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
        String currTimeStr=sdf.format(currTime).toString();

//        String query1 = "SELECT * FROM " + DatabaseContract.RecipeEntry.TABLE_NAME;
//
//        Cursor c1 = db.rawQuery(query1,null);
//
//        if (c1 != null && c1.moveToFirst()) {
//            Log.d(TAG,"entered if ");
//            while (c1.isAfterLast() == false) {
//                Log.d(TAG,"in while");
//                Log.d(TAG,c1.getString(1)+ c1.getString(2)+ c1.getString(3)+
//                        c1.getString(4)+ c1.getString(5)+ c1.getString(6));
//
//                c1.moveToNext();
//            }
//        }


        String query = "SELECT * FROM " + DatabaseContract.RecipeEntry.TABLE_NAME
                +" WHERE "+DatabaseContract.RecipeEntry.LOCATION+" = "+"'"+location+"'";
        Log.d(TAG,"query:"+query);

        Cursor c = db.rawQuery(query, null);

        if (c != null && c.moveToFirst()) {
            Log.d(TAG,"entered if ");
            while (c.isAfterLast() == false) {
                Log.d(TAG,"in while");
                ActionObject ao = new ActionObject(c.getString(1), c.getString(2), c.getString(3),
                        c.getString(4), c.getString(5), c.getString(6));

                Log.d(TAG,c.getInt(0)+c.getString(1)+ c.getString(2)+ c.getString(3)+
                        c.getString(4)+ c.getString(5)+ c.getString(6));


                try {
                    long currHourMin=sdf.parse(currTimeStr).getTime();
                    long startTime=sdf.parse(ao.getStartTime()).getTime();
                    long endTime=sdf.parse(ao.getEndTime()).getTime();
                    Log.d(TAG,ao.getStartTime()+" "+ao.getEndTime());

                    if(currHourMin>=startTime && currHourMin<=endTime) {
                        actions.add(ao);
                        Log.d(TAG,"added action object");
                    }else {
                        Log.d(TAG,currHourMin+" "+startTime+" "+endTime);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                c.moveToNext();
            }
        }

        return actions;

    }
}