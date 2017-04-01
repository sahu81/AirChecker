package com.example.dessusdi.myfirstapp.ManualSQLiteClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.dessusdi.myfirstapp.models.air_quality.WaqiObject;
import com.example.dessusdi.myfirstapp.tools.Constants;

import java.util.List;

/**
 * Created by dessusdi on 10/02/2017.
 * DESSUS Dimitri
 */
public class AqcinDatabaseService {

    private final AqcinDatabaseHelper mDbHelper;
    private SQLiteDatabase db;

    public AqcinDatabaseService(Context context) {
        this.mDbHelper = new AqcinDatabaseHelper(context);
    }

    public void open() {
        mDbHelper.getWritableDatabase();
    }

    public long addCity(String identifier) {
        Log.d("DATABASE", "Adding city to db...");
        // Gets the data repository in write mode
        this.open();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        Log.d("DATABASE", identifier);
        values.put(Constants.Database.COLUMN_CITY_ID, identifier);

        this.close();
        // Insert the new row, returning the primary key value of the new row
        return db.insert(Constants.Database.TABLE_NAME, null, values);
    }

    public void removeCity(String identifier) {
        Log.d("DATABASE", "Removing city from db...");

        // Gets the data repository in write mode
        this.open();
        // Define 'where' part of query.
        String selection = Constants.Database.COLUMN_CITY_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { identifier };
        // Issue SQL statement.
        db.delete(Constants.Database.TABLE_NAME, selection, selectionArgs);
        this.close();
    }

    public List fetchSavedCities() {
        Log.d("DATABASE", "Fetching cities from db...");

        return WaqiObject.listAll(WaqiObject.class);

        /* BEFORE ADDING ORM
        db = mDbHelper.getReadableDatabase();


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
        this.close();
        return itemIds;
        */
    }

    public void close() {
        this.mDbHelper.close();
    }
}
