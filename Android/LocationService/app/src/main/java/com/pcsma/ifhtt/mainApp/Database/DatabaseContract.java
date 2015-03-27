package com.pcsma.ifhtt.mainApp.Database;

import android.provider.BaseColumns;

/**
 * Created by vedantdasswain on 28/03/15.
 */
public class DatabaseContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public DatabaseContract() {
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";

    /* Inner class that defines the table contents */
    public static abstract class RecipeEntry implements BaseColumns {
        public static final String TABLE_NAME = "Recipe";
        public static final String LOCATION = "Location";
        public static final String START_TIME = "Start_Time";
        public static final String END_TIME = "End_Time";
        public static final String ACTION="Action";
        public static final String OPTION="Option";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + RecipeEntry.TABLE_NAME + " (" +
                        RecipeEntry._ID + " INTEGER PRIMARY KEY," +
                        RecipeEntry.LOCATION + TEXT_TYPE + COMMA_SEP +
                        RecipeEntry.START_TIME + REAL_TYPE + COMMA_SEP +
                        RecipeEntry.END_TIME + REAL_TYPE + COMMA_SEP +
                        RecipeEntry.ACTION + TEXT_TYPE + COMMA_SEP +
                        RecipeEntry.OPTION +TEXT_TYPE +
                        " )";
        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + RecipeEntry.TABLE_NAME;

    }
}
