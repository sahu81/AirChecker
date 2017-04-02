package com.example.dessusdi.myfirstapp.services;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

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

/**
 * Created by dessusdi on 30/01/2017.
 * DESSUS Dimitri
 */
public class AqcinRequestService {

    private final Activity mApplicationContext;

    public AqcinRequestService(Context context) {
        mApplicationContext = (Activity)context;
    }

    /**
     * Fetch cities by name
     * @param search city identifier
     * @param callback
     */
    public void fetchCityID(String search, final SearchQueryCallback callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this.mApplicationContext);

        // Create progress dialog
        final ProgressDialog mDialog = new ProgressDialog(this.mApplicationContext);
        mDialog.setMessage("Loading...");
        mDialog.setCancelable(false);
        mDialog.show();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, RequestBuilder.buildCityIdURL(search),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mDialog.dismiss();
                        Gson gson = new Gson();
                        SearchGlobalObject globalSearchObject = gson.fromJson(response, SearchGlobalObject.class);
                        callback.onSuccess(globalSearchObject);
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
     * Fetch air quality by using unique city identifier
     * @param identifier city identifier
     * @param callback
     */
    public void fetchAirQuality(int identifier, final GlobalObjectCallback callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this.mApplicationContext);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, RequestBuilder.buildAirQualityURL(identifier),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        GlobalObject global = gson.fromJson(response, GlobalObject.class);
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
     * @param callback
     */
    public void fetchCitiesAroundPosition(double latitude, double longitude, final PositionQueryCallback callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this.mApplicationContext);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, RequestBuilder.buildCitiesAroundPositionURL(latitude, longitude),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        PositionGlobalObject global = gson.fromJson(response, PositionGlobalObject.class);

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
