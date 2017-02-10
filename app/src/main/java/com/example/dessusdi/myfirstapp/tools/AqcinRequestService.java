package com.example.dessusdi.myfirstapp.tools;

import android.app.Activity;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dessusdi.myfirstapp.models.air_quality.GlobalObject;
import com.example.dessusdi.myfirstapp.models.search.SearchGlobalObject;
import com.google.gson.Gson;

/**
 * Created by dessusdi on 30/01/2017.
 * DESSUS Dimitri
 */
public class AqcinRequestService {

    private Activity mApplicationContext;

    public AqcinRequestService(Context context) {
        mApplicationContext = (Activity)context;
    }

    public void fetchCityID(String search, final SearchQueryCallback callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this.mApplicationContext);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, RequestBuilder.buildCityIdURL(search),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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

    public void fetchAirQuality(String identifier, final GlobalObjectCallback callback) {
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

    public interface GlobalObjectCallback {
        void onSuccess(GlobalObject globalObject);
    }

    public interface SearchQueryCallback {
        void onSuccess(SearchGlobalObject globalSearchObject);
    }
}
