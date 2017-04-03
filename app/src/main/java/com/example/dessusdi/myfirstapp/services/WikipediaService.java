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
import com.example.dessusdi.myfirstapp.models.wikipedia.ImageObject;
import com.example.dessusdi.myfirstapp.models.wikipedia.PageObject;
import com.example.dessusdi.myfirstapp.models.wikipedia.QueryDeserializer;
import com.example.dessusdi.myfirstapp.tools.RequestBuilder;

/**
 * Created by Dimitri on 02/03/2017.
 * DESSUS Dimitri
 */

public class WikipediaService {

    private final Activity mApplicationContext;
    private  RequestQueue queue;

    public WikipediaService(Context context) {
        mApplicationContext = (Activity)context;
    }

    /**
     * Fetch city information by using wikipedia API.
     * @param search city name
     * @param callback customized callback interface
     */
    public void fetchCityInformation(String search, final WikipediaService.cityInformationCallback callback) {

        // Instantiate the RequestQueue.
        if(queue == null)
            queue = Volley.newRequestQueue(this.mApplicationContext);

        // Create progress dialog
        final ProgressDialog mDialog = new ProgressDialog(this.mApplicationContext);
        mDialog.setMessage("Loading...");
        mDialog.setCancelable(false);
        mDialog.show();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, RequestBuilder.buildCityInformationURL(search),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mDialog.dismiss();
                        callback.onSuccess(QueryDeserializer.deserializePage(response, mApplicationContext));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mDialog.dismiss();
                error.printStackTrace();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    /**
     * Fetch city image by using wikipedia API
     * @param search city name
     * @param callback customized callback interface
     */
    public void fetchCityImage(String search, final WikipediaService.cityImageCallback callback) {
        // Instantiate the RequestQueue.
        if(queue == null)
            queue = Volley.newRequestQueue(this.mApplicationContext);

        // Create progress dialog
        final ProgressDialog mDialog = new ProgressDialog(this.mApplicationContext);
        mDialog.setMessage("Loading...");
        mDialog.setCancelable(false);
        mDialog.show();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, RequestBuilder.buildCityImageURL(search),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mDialog.dismiss();
                        callback.onSuccess(QueryDeserializer.deserializeImage(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mDialog.dismiss();
                error.printStackTrace();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Callback for PageObject
     * @see PageObject
     */
    public interface cityInformationCallback {
        void onSuccess(PageObject pageObject);
    }

    /**
     * Callback for ImageObject
     * @see ImageObject
     */
    public interface cityImageCallback {
        void onSuccess(ImageObject imageObject);
    }
}
