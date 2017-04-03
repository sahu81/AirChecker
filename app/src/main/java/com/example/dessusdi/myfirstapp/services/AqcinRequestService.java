package com.example.dessusdi.myfirstapp.services;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dessusdi.myfirstapp.models.air_quality.GlobalObject;
import com.example.dessusdi.myfirstapp.models.air_quality_position.PositionGlobalObject;
import com.example.dessusdi.myfirstapp.models.search.SearchGlobalObject;
import com.example.dessusdi.myfirstapp.tools.RequestBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by dessusdi on 30/01/2017.
 * DESSUS Dimitri
 */
public class AqcinRequestService {

    private final Activity mApplicationContext;
    private static final String TAG = "Service";

    public AqcinRequestService(Context context) {
        mApplicationContext = (Activity)context;
    }

    /**
     * Fetch cities by name
     * @param search city identifier
     * @param callback customized callback interface
     */
    public void fetchCityID(String search, final SearchQueryCallback callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this.mApplicationContext);

        // Create progress dialog
        final ProgressDialog mDialog = new ProgressDialog(this.mApplicationContext);
        mDialog.setMessage("Loading...");
        mDialog.setCancelable(false);
        mDialog.show();

        // Encode space character
        search = search.replace(" ", "%20");

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, RequestBuilder.buildCityIdURL(search),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mDialog.dismiss();

                        SearchGlobalObject globalSearchObject = null;
                        Gson gson = new Gson();
                        try {
                            globalSearchObject = gson.fromJson(response, SearchGlobalObject.class);
                        } catch (IllegalStateException | JsonSyntaxException exception){
                            Log.d(TAG, "error when parsing SearchGlobalObject");
                        }

                        callback.onSuccess(globalSearchObject);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mDialog.dismiss();
                
                System.out.println(error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Fetch air quality by using unique city identifier
     * @param identifier city identifier
     * @param callback customized callback interface
     */
    public void fetchAirQuality(int identifier, final GlobalObjectCallback callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this.mApplicationContext);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, RequestBuilder.buildAirQualityURL(identifier),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        GlobalObject global = null;
                        Gson gson = new Gson();
                        try {
                            global = gson.fromJson(response, GlobalObject.class);
                        } catch (IllegalStateException | JsonSyntaxException exception){
                            Log.d(TAG, "error when parsing GlobalObject");
                        }
                        callback.onSuccess(global);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Retrieve cities around user's location
     * @param latitude user's latitude
     * @param longitude user's longitude
     * @param callback customized callback interface
     */
    public void fetchCitiesAroundPosition(double latitude, double longitude, final PositionQueryCallback callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this.mApplicationContext);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, RequestBuilder.buildCitiesAroundPositionURL(latitude, longitude),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PositionGlobalObject global = null;
                        Gson gson = new Gson();
                        try {
                            global = gson.fromJson(response, PositionGlobalObject.class);
                        } catch (IllegalStateException | JsonSyntaxException exception){
                            Log.d(TAG, "error when parsing PositionGlobalObject");
                        }
                        callback.onSuccess(global);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Callback for GlobalObject
     * @see GlobalObject
     */
    public interface GlobalObjectCallback {
        void onSuccess(GlobalObject globalObject);
    }

    /**
     * Callback for SearchGlobalObject
     * @see SearchGlobalObject
     */
    public interface SearchQueryCallback {
        void onSuccess(SearchGlobalObject globalSearchObject);
    }

    /**
     * Callback for PositionQueryCallback
     * @see PositionGlobalObject
     */
    public interface PositionQueryCallback {
        void onSuccess(PositionGlobalObject positionGlobalObject);
    }
}
