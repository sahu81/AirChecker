package com.example.dessusdi.myfirstapp.models.wikipedia;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Dimitri on 03/03/2017.
 * DESSUS Dimitri
 */

public class QueryDeserializer {

    public static PageObject deserializePage(String rawJSON, Context context) {
        PageObject pageObject = new PageObject(context);

        try {
            JSONObject json         = new JSONObject(rawJSON);
            JSONObject query        = json.getJSONObject("query");
            JSONObject pages        = query.getJSONObject("pages");
            Iterator<String> keys   = pages.keys();
            if( keys.hasNext() ) {
                String key = keys.next();
                if(!key.equals("-1")) {
                    Gson gson = new Gson();
                    pageObject = gson.fromJson(pages.getString(key), PageObject.class);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return pageObject;
    }

    public static ImageObject deserializeImage(String rawJSON) {
        ImageObject imageObject = new ImageObject();

        try {
            JSONObject json         = new JSONObject(rawJSON);
            JSONObject query        = json.getJSONObject("query");
            JSONObject pages        = query.getJSONObject("pages");
            Iterator<String> keys   = pages.keys();
            if( keys.hasNext() ) {
                String key = keys.next();
                if(!key.equals("-1")) {
                    Gson gson = new Gson();
                    imageObject = gson.fromJson(pages.getString(key), ImageObject.class);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return imageObject;
    }
}
