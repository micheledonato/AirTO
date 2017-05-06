package com.devmicheledonato.airto.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.devmicheledonato.airto.data.WeatherContract.WeatherEntry;

/**
 * Created by Michele on 09/04/2017.
 */

public class WeatherDbHelper extends SQLiteOpenHelper {

    /**
     * Name of database. Database name should be descriptive and
     * end with the .db extension.
     */
    public static final String DATABASE_NAME = "weather.db";

    /**
     * Version of current database. If you change the database schema, you must increment the database version
     * or the onUpgrade method will not be called.
     */
    private static final int DATABASE_VERSION = 1;

    public WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_WEATHER_TABLE =

                "CREATE TABLE " + WeatherEntry.TABLE_NAME + " (" +

                        WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        WeatherEntry.COLUMN_DATE + " INTEGER NOT NULL, " +

                        WeatherEntry.COLUMN_SUMMARY + " TEXT NOT NULL, " +

                        WeatherEntry.COLUMN_MIN_TEMP + " REAL NOT NULL, " +
                        WeatherEntry.COLUMN_MAX_TEMP + " REAL NOT NULL, " +

                        WeatherEntry.COLUMN_WEATHER_ICON + " TEXT NOT NULL, " +

                        WeatherEntry.COLUMN_IPQA + " TEXT, " +

                        "UNIQUE (" + WeatherEntry.COLUMN_DATE + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WeatherEntry.TABLE_NAME);
        onCreate(db);
    }
}
