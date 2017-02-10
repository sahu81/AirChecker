package com.example.dessusdi.myfirstapp.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.dessusdi.myfirstapp.model.WaqiObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dessusdi on 10/02/2017.
 * DESSUS Dimitri
 */
public class AqcinDatabaseService {

    private AqcinDatabaseHelper mDbHelper;

    public AqcinDatabaseService(Context context) {
        this.mDbHelper = new AqcinDatabaseHelper(context);
    }

    public long addCity(String identifier) {
        Log.d("DATABASE", "Adding city to db...");
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        Log.d("DATABASE", identifier);
        values.put(Constants.Database.COLUMN_CITY_ID, identifier);

        // Insert the new row, returning the primary key value of the new row
        return db.insert(Constants.Database.TABLE_NAME, null, values);
    }

    public void removeCity(WaqiObject waqiObj) {
        Log.d("DATABASE", "Removing city from db...");
    }

    public List fetchSavedCities() {
        Log.d("DATABASE", "Fetching cities from db...");
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                Constants.Database._ID,
                Constants.Database.COLUMN_CITY_ID
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                Constants.Database.COLUMN_CITY_ID + " DESC";

        Cursor cursor = db.query(
                Constants.Database.TABLE_NAME,              // The table to query
                projection,                                 // The columns to return
                null,                                       // The columns for the WHERE clause
                null,                                       // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                sortOrder                                   // The sort order
        );

        List itemIds = new ArrayList<>();
        while(cursor.moveToNext()) {
            String cityId = cursor.getString(
                    cursor.getColumnIndexOrThrow(Constants.Database.COLUMN_CITY_ID));
            itemIds.add(cityId);
        }
        cursor.close();

        return itemIds;
    }
}
