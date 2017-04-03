package com.example.dessusdi.myfirstapp.models.wikipedia;

import android.content.Context;

import com.example.dessusdi.myfirstapp.R;

/**
 * Created by Dimitri on 03/03/2017.
 * DESSUS Dimitri
 */

public class PageObject {
    private final String title;
    private final String extract;

    public PageObject(Context context) {
        this.title      = context.getResources().getString(R.string.city_empty_title);
        this.extract    = context.getResources().getString(R.string.city_empty_informations);
    }

    /**
     * @return City name
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return City description
     */
    public String getExtract() {
        return extract.replace("\n", "\n\n");
    }
}
