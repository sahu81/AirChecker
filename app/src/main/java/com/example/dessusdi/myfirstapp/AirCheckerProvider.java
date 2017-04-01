package com.example.dessusdi.myfirstapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.dessusdi.myfirstapp.models.air_quality.WaqiObject;

import java.util.List;

/**
 * Created by Dimitri on 01/04/2017.
 */

public class AirCheckerProvider extends ContentProvider {
    private static final String TAG = "ContentProvider";

    public static final Uri CONTENT_URI = Uri.parse("content://com.dimitridessus.android.content.provider.airchecker");
    private static final String CONTENT_PROVIDER_MIME = "vnd.android.cursor.item/vnd.dimitridessus.android.content.provider.single";

    @Override
    public boolean onCreate() {
        Log.d(TAG, "Creating");
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return AirCheckerProvider.CONTENT_PROVIDER_MIME;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        int cityID = Integer.valueOf((String)values.get("identifier"));
        Log.d(TAG, "Saving city : " + cityID);

        WaqiObject cityObject = new WaqiObject(cityID);
        cityObject.save();

        return null;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // Nothing to update here
        return 0;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "Deleting city : " + selection);

        int state = 1;
        List<WaqiObject> waqiObjects = WaqiObject.find(WaqiObject.class, "identifier = ?", String.valueOf(selection));
        if(waqiObjects.size() > 0) {
            waqiObjects.get(0).delete();
            state = 0;
        }

        return state;

    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return null;
    }
}
